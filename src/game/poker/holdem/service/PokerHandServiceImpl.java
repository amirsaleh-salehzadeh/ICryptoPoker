package game.poker.holdem.service;

import game.poker.holdem.*;
import game.poker.holdem.dao.*;
import game.poker.holdem.domain.*;
import game.poker.holdem.util.GameUtil;
import game.poker.holdem.util.PlayerHandBetAmountComparator;
import game.poker.holdem.util.PlayerUtil;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import javax.print.attribute.standard.Finishings;

import common.game.poker.holdem.GameENT;
import services.websockets.TableWebsocket;
import tools.AMSException;

public class PokerHandServiceImpl implements PokerHandServiceInterface {

	private HandDaoImpl handDao;

	private GameDaoImpl gameDao;

	private PlayerDaoImpl playerDao;

	public HandEntity startNewHand(GameENT game) {
		gameDao = new GameDaoImpl();
		handDao = new HandDaoImpl();
		playerDao = new PlayerDaoImpl();
		HandEntity hand = new HandEntity();
		updateBlindLevel(game);
		hand.setBlindLevel(game.getGameStructure().getCurrentBlindLevel());
		hand.setGame(game);
		Deck d = new Deck(true);
		Set<PlayerHand> participatingPlayers = new HashSet<PlayerHand>();
		for (Player p : game.getPlayers()) {
			if (p.getChips() > 0) {
				PlayerHand ph = new PlayerHand();
				ph.setHandId(hand.getId());
				ph.setPlayer(p);
				ph.setCard1(d.dealCard());
				ph.setCard2(d.dealCard());
				participatingPlayers.add(ph);
			}
			// else
			// sitOutCurrentPlayer(game.getCurrentHand(), p);
		}
		hand.setPlayers(participatingPlayers);
		// Sort and get the next player to act (immediately after the big blind)
		List<PlayerHand> players = new ArrayList<PlayerHand>();
		players.addAll(participatingPlayers);
		Player nextToAct = PlayerUtil.getNextPlayerToAct(hand,
				this.getPlayerInBB(hand));
		hand.setCurrentToAct(nextToAct);
		// Register the Forced Small and Big Blind bets as part of the hand
		Player smallBlind = getPlayerInSB(hand);
		Player bigBlind = getPlayerInBB(hand);
		int sbBet = 0;
		int bbBet = 0;
		for (PlayerHand ph : hand.getPlayers()) {// false
			if (ph.getPlayer().equals(smallBlind)) {
				sbBet = Math.min(hand.getBlindLevel().getSmallBlind(),
						smallBlind.getChips());
				ph.setBetAmount(sbBet);
				ph.setRoundBetAmount(sbBet);
				smallBlind.setChips(smallBlind.getChips() - sbBet);
			} else if (ph.getPlayer().equals(bigBlind)) {
				bbBet = Math.min(hand.getBlindLevel().getBigBlind(),
						bigBlind.getChips());
				ph.setBetAmount(bbBet);
				ph.setRoundBetAmount(bbBet);
				bigBlind.setChips(bigBlind.getChips() - bbBet);
			}
		}
		playerDao.merge(smallBlind, null);
		playerDao.merge(bigBlind, null);
		hand.setTotalBetAmount(hand.getBlindLevel().getBigBlind());
		hand.setLastBetAmount(hand.getBlindLevel().getBigBlind());
		hand.setPot(sbBet + bbBet);
		BoardEntity b = new BoardEntity();
		hand.setBoard(b);
		hand.setCards(d.exportDeck());
		hand = handDao.save(hand, null);
		game.setCurrentHand(hand);
		gameDao.merge(game, null);
		hand.setGame(null);
		return hand;
	}

	public void endHand(GameENT g) throws AMSException {
		gameDao = new GameDaoImpl();
		handDao = new HandDaoImpl();
		playerDao = new PlayerDaoImpl();
		System.out.println("endHand");
		HandEntity hand = g.getCurrentHand();
		hand.setGame(g);
		if (!isActionResolved(hand)) {
			hand = handDao.merge(hand, null);
			river(g);
			return;
		}
		hand.setCurrentToAct(null);
		hand = determineWinner(hand);
		g.setCurrentHand(hand);
		finishHandAndGame(g);
	}

	public void finishHandAndGame(GameENT game) {
		// If players were eliminated this hand, set their finished position
		HandEntity hand = game.getCurrentHand();
		int counterToFindLastHand = 0;
		List<Player> players = new ArrayList<Player>();
		if (hand != null) {
			hand.setGame(game);
			List<PlayerHand> phs = new ArrayList<PlayerHand>();
			phs.addAll(hand.getPlayers());// true
			// Sort the list of PlayerHands so the one with the smallest chips
			// at
			// risk is first.
			// Use this to determine finish position if multiple players are
			// eliminated on the same hand.
			Collections.sort(phs, new PlayerHandBetAmountComparator());
			for (PlayerHand ph : phs) {
				if (ph.getPlayer().getChips() <= 0) {
					ph.getPlayer()
							.setFinishPosition(game.getPlayersRemaining());
					game.setPlayersRemaining(game.getPlayersRemaining() - 1);
					playerDao.merge(ph.getPlayer(), null);
					hand = sitOutCurrentPlayer(game, ph.getPlayer());
				}
			}

			// For all players in the game, remove any who are out of chips
			// (eliminated)
			for (Player p : game.getPlayers()) {
				if (p.isSittingOut())
					counterToFindLastHand++;
				if (p.getChips() != 0) {
					players.add(p);
				} else if (p.equals(game.getPlayerInBTN())) {
					// If the player on the Button has been eliminated, we still
					// need this player
					// in the list so that we calculate next button from its
					// position
					players.add(p);
				}
			}

			// Rotate Button. Use Simplified Moving Button algorithm (for ease
			// of
			// coding)
			// This means we always rotate button. Blinds will be next two
			// active
			// players. May skip blinds.
			Player nextButton = PlayerUtil.getNextPlayerInGameOrder(players,
					game.getPlayerInBTN());
			game.setPlayerInBTN(nextButton);
			game = gameDao.merge(game, null);

			// Remove Deck from database. No need to keep that around anymore
			hand.setCards(new ArrayList<Card>());
			hand.setCurrentToAct(null);
			hand.setGame(game);
			handDao.merge(hand, null);
		}
		final long gameId = game.getId();
		final ScheduledExecutorService scheduler = Executors
				.newScheduledThreadPool(1);
		int timer = 10;
		// if the second last player leaves
		if (game.getPlayers().size() == counterToFindLastHand + 1)
			timer = 1;
		System.out.println("finishHandAndGame before thread");
		ScheduledFuture<?> countdown = scheduler.schedule(new Runnable() {
			@Override
			public void run() {
				System.out.println("finishHandAndGame inside the thread");
				GameDaoImpl gameDao = new GameDaoImpl();
				GameENT game = gameDao.findById(gameId, null);
				game.setCurrentHand(null);
				game.setStarted(false);
				game = gameDao.merge(game, null);
				scheduler.shutdownNow();
				for (Table table : TableWebsocket.games) {
					if (table.getGame().getId() == gameId) {
						table.sendToAll("");
					}
				}
			}
		}, timer, TimeUnit.SECONDS);
		// }
	}

	public HandEntity getHandById(long id) {
		handDao = new HandDaoImpl();
		return handDao.findById(id, null);
	}

	public HandEntity flop(GameENT game) throws IllegalStateException {
		handDao = new HandDaoImpl();
		HandEntity hand = game.getCurrentHand();
		if (hand.getBoard().getFlop1() != null) {
			throw new IllegalStateException("Unexpected Flop.");
		}
		hand.setGame(game);
		if (!isActionResolved(hand)) {
			hand = handDao.merge(hand, null);
			return hand;
		}
		Deck d = new Deck(hand.getCards());
		d.shuffleDeck();
		BoardEntity board = hand.getBoard();
		board.setFlop1(d.dealCard());
		board.setFlop2(d.dealCard());
		board.setFlop3(d.dealCard());
		hand.setCards(d.exportDeck());
		resetRoundValues(hand);
		PlayerHand playerHand = null;
		for (PlayerHand ph : hand.getPlayers()) {// false
			if (ph.getPlayer() != null
					&& ph.getPlayer().equals(hand.getCurrentToAct())) {
				playerHand = ph;
				break;
			}
		}
		if (playerHand != null
				&& hand.getTotalBetAmount() > playerHand.getRoundBetAmount()) {
			PlayerUtil.removePlayerFromHand(hand.getCurrentToAct(), hand);
		}
		hand = handDao.merge(hand, null);
		if (goToNextAction(hand)) {
			game.setCurrentHand(hand);
			return turn(game);
		}
		return hand;
	}

	private boolean goToNextAction(HandEntity hand) {
		int counter = 0;
		for (PlayerHand ph : hand.getPlayers()) {
			if (ph.getPlayer().getChips() <= 0
					|| ph.getStatus() == PlayerHandStatus.FOLDED)
				counter++;
		}
		if (hand.getPlayers().size() - counter <= 1)
			return true;
		else
			return false;
	}

	public HandEntity turn(GameENT game) throws IllegalStateException {
		HandEntity hand = game.getCurrentHand();
		if (hand.getBoard().getFlop1() == null
				|| hand.getBoard().getTurn() != null) {
			return flop(game);
			// throw new IllegalStateException("Unexpected Turn.");
		}
		handDao = new HandDaoImpl();
		hand.setGame(game);
		if (!isActionResolved(hand)) {
			hand = handDao.merge(hand, null);
			return hand;
		}
		Deck d = new Deck(hand.getCards());
		d.shuffleDeck();
		BoardEntity board = hand.getBoard();
		board.setTurn(d.dealCard());
		hand.setCards(d.exportDeck());
		resetRoundValues(hand);
		PlayerHand playerHand = null;
		for (PlayerHand ph : hand.getPlayers()) {// false
			if (ph.getPlayer() != null
					&& ph.getPlayer().equals(hand.getCurrentToAct())) {
				playerHand = ph;
				break;
			}
		}
		if (playerHand != null
				&& hand.getTotalBetAmount() > playerHand.getRoundBetAmount()) {
			PlayerUtil.removePlayerFromHand(hand.getCurrentToAct(), hand);
		}
		hand = handDao.merge(hand, null);
		if (goToNextAction(hand)) {
			game.setCurrentHand(hand);
			return river(game);
		}
		return hand;
	}

	public HandEntity river(GameENT game) throws IllegalStateException {
		HandEntity hand = game.getCurrentHand();
		if (hand.getBoard().getFlop1() == null
				|| hand.getBoard().getTurn() == null
				|| hand.getBoard().getRiver() != null) {
			return turn(game);
			// throw new IllegalStateException("Unexpected River.");
		}
		handDao = new HandDaoImpl();
		hand.setGame(game);
		if (!isActionResolved(hand)) {
			hand = handDao.merge(hand, null);
			return hand;
		}

		Deck d = new Deck(hand.getCards());
		d.shuffleDeck();
		BoardEntity board = hand.getBoard();
		board.setRiver(d.dealCard());
		hand.setCards(d.exportDeck());
		resetRoundValues(hand);
		PlayerHand playerHand = null;
		for (PlayerHand ph : hand.getPlayers()) {// false
			if (ph.getPlayer() != null
					&& ph.getPlayer().equals(hand.getCurrentToAct())) {
				playerHand = ph;
				break;
			}
		}
		if (playerHand != null
				&& hand.getTotalBetAmount() > playerHand.getRoundBetAmount()) {
			PlayerUtil.removePlayerFromHand(hand.getCurrentToAct(), hand);
		}
		hand = handDao.merge(hand, null);
		if (goToNextAction(hand)) {
			game.setCurrentHand(hand);
			try {
				endHand(game);
			} catch (AMSException e) {
				e.printStackTrace();
			}
		}
		return hand;
	}

	public HandEntity sitOutCurrentPlayer(GameENT game, Player currentPlayer) {
		HandEntity hand = game.getCurrentHand();
		handDao = new HandDaoImpl();
		playerDao = new PlayerDaoImpl();
		if (currentPlayer == null) {
			return null;
		}
		// Refund the chips into the main account
		// currentPlayer.setTotalChips(currentPlayer.getChips() +
		// currentPlayer.getTotalChips());
		currentPlayer.setSittingOut(true);
		currentPlayer = playerDao.merge(currentPlayer, null);
		PlayerHand playerHand = null;
		for (PlayerHand ph : hand.getPlayers()) {// false
			if (ph.getPlayer().equals(currentPlayer)) {
				playerHand = ph;
				break;
			}
		}
		// Player next = PlayerUtil.getNextPlayerToAct(hand, currentPlayer);
		if (playerHand != null
				&& hand.getTotalBetAmount() > playerHand.getRoundBetAmount()) {
			PlayerUtil.removePlayerFromHand(currentPlayer, hand);
		}
		// hand.setCurrentToAct(next);
		hand.setGame(game);
		hand = handDao.merge(hand, null);
		return hand;
	}

	@Override
	public Player getPlayerInSB(HandEntity hand) {
		Player button = hand.getGame().getPlayerInBTN();
		// Heads up the Button is the Small Blind
		if (hand.getPlayers().size() == 2) {// false
			return button;
		}
		List<PlayerHand> players = new ArrayList<PlayerHand>();
		players.addAll(hand.getPlayers());// false
		return PlayerUtil.getNextPlayerInGameOrderPH(players, button);
	}

	@Override
	public Player getPlayerInBB(HandEntity hand) {
		Player button = hand.getGame().getPlayerInBTN();
		List<PlayerHand> players = new ArrayList<PlayerHand>();
		for (PlayerHand ph : hand.getPlayers()) {// false
			if (ph.getPlayer() != null && !ph.getPlayer().isSittingOut())
				players.add(ph);
		}
		Player leftOfButton = PlayerUtil.getNextPlayerInGameOrderPH(players,
				button);
		// Heads up, the player who is not the Button is the Big blind
		if (hand.getPlayers().size() == 2) {// false
			return leftOfButton;
		}
		return PlayerUtil.getNextPlayerInGameOrderPH(players, leftOfButton);
	}

	private void updateBlindLevel(GameENT game) {
		if (game.getGameStructure().getCurrentBlindEndTime() == null) {
			// Start the blind
			setNewBlindEndTime(game);
		} else if (game.getGameStructure().getCurrentBlindEndTime()
				.before(new Date())) {
			// Time has expired, next blind level
			List<BlindLevel> blinds = game.getGameStructure().getBlindLevels();
			Collections.sort(blinds);
			boolean nextBlind = false;
			for (BlindLevel blind : blinds) {
				if (nextBlind) {
					game.getGameStructure().setCurrentBlindLevel(blind);
					setNewBlindEndTime(game);
					break;
				}
				if (blind == game.getGameStructure().getCurrentBlindLevel()) {
					nextBlind = true;
				}
			}
		}
	}

	private void setNewBlindEndTime(GameENT game) {
		Calendar c = Calendar.getInstance();
		c.add(Calendar.MINUTE, game.getGameStructure().getBlindLength());
		game.getGameStructure().setCurrentBlindEndTime(c.getTime());
	}

	private void resetRoundValues(HandEntity hand) {
		hand.setTotalBetAmount(0);
		hand.setLastBetAmount(0);
		handDao = new HandDaoImpl();
		List<Player> playersInHand = new ArrayList<Player>();
		for (PlayerHand ph : hand.getPlayers()) {// false
			ph.setRoundBetAmount(0);
			if (ph.getPlayer() != null) {
				playersInHand.add(ph.getPlayer());
				handDao.updatePlayerHand(ph, null);
			}
		}
		// Next player is to the left of the button. Given that the button may
		// have been eliminated
		// In a round of betting, we need to put the button back to determine
		// relative position.
		Player btn = hand.getGame().getPlayerInBTN();
		if (!playersInHand.contains(btn)) {
			playersInHand.add(btn);
		}

		Player next = PlayerUtil.getNextPlayerInGameOrder(playersInHand, btn);
		Player firstNext = next;

		// Skip all in players and players that are sitting out
		while (next.getChips() <= 0 || next.isSittingOut()) {
			next = PlayerUtil.getNextPlayerInGameOrder(playersInHand, next);
			if (next.equals(firstNext)) {
				// Exit condition if all players are all in.
				break;
			}
		}
		// hand.setCurrentToAct(next);
		handDao.merge(hand, null);
	}

	private HandEntity determineWinner(HandEntity hand) {
		// if only one PH left, everyone else folded
		if (hand.getPlayers().size() == 1) {// false
			Player winner = hand.getPlayers().iterator().next()// false
					.getPlayer();
			winner.setChips(winner.getChips() + hand.getPot());
			playerDao.merge(winner, null);
			hand = handDao.findById(hand.getId(), null);
		} else {
			hand = refundOverbet(hand);
			Map<Player, Integer> winners = PlayerUtil
					.getAmountWonInHandForAllPlayers(hand);
			if (winners == null) {
				return hand;
			}
			for (Map.Entry<Player, Integer> entry : winners.entrySet()) {
				Player player = entry.getKey();
				player.setChips(player.getChips() + entry.getValue());
				playerDao.merge(player, null);
			}
		}
		return hand;
	}

	private HandEntity refundOverbet(HandEntity hand) {
		List<PlayerHand> phs = new ArrayList<PlayerHand>();
		phs.addAll(hand.getPlayers());// false
		// Sort from most money contributed, to the least
		Collections.sort(phs, new PlayerHandBetAmountComparator());
		Collections.reverse(phs);
		// If there are at least 2 players, and the top player contributed more
		// to the pot than the next player

		if (phs.size() >= 2
				&& (phs.get(0).getBetAmount() > phs.get(1).getBetAmount())) {
			// Refund that extra amount contributed. Remove from pot, add back
			// to player
			int diff = phs.get(0).getBetAmount() - phs.get(1).getBetAmount();
			phs.get(0).setBetAmount(phs.get(1).getBetAmount());
			phs.get(0).getPlayer()
					.setChips(phs.get(0).getPlayer().getChips() + diff);
			hand.setPot(hand.getPot() - diff);
			handDao.merge(hand, null);
			handDao.updatePlayerHand(phs.get(0), null);
			playerDao.merge(phs.get(0).getPlayer(), null);
		}
		return hand;
	}

	// Helper method to see if there are any outstanding actions left in a
	// betting round
	public static boolean isActionResolved(HandEntity hand) {
		int roundBetAmount = hand.getTotalBetAmount();
		for (PlayerHand ph : hand.getPlayers()) {// true
			// All players should have paid the roundBetAmount or should be all
			// in
			if (ph.getPlayer() != null
					&& ph.getRoundBetAmount() != roundBetAmount
					&& ph.getPlayer().getChips() > 0) {
				return false;
			}
		}
		return true;
	}

}
