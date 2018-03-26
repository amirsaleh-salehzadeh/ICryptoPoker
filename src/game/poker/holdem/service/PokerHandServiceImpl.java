/*
The MIT License (MIT)

Copyright (c) 2013 Jacob Kanipe-Illig

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in
all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
THE SOFTWARE.
 */
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

import services.websockets.TableWebsocket;
import tools.AMSException;

public class PokerHandServiceImpl implements PokerHandServiceInterface {

	private HandDaoImpl handDao;

	private GameDaoImpl gameDao;

	private PlayerDaoImpl playerDao;

<<<<<<< HEAD
import tools.AMSException;

public class PokerHandServiceImpl implements PokerHandServiceInterface {

	private HandDaoImpl handDao;

	private GameDaoImpl gameDao;

	private PlayerDaoImpl playerDao;

	public HandEntity startNewHand(Game game) {
		System.out.println("1 "+System.currentTimeMillis());
		gameDao = new GameDaoImpl();
		handDao = new HandDaoImpl();
=======
	public HandEntity startNewHand(Game game) {

		gameDao = new GameDaoImpl();
		handDao = new HandDaoImpl();
		playerDao = new PlayerDaoImpl();
>>>>>>> origin/AmirV1
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
<<<<<<< HEAD
				p.setGameId(game.getId());
=======
>>>>>>> origin/AmirV1
				ph.setPlayer(p);
				ph.setCard1(d.dealCard());
				ph.setCard2(d.dealCard());
				participatingPlayers.add(ph);
			}
		}
		hand.setPlayers(participatingPlayers);
<<<<<<< HEAD
		System.out.println("2 "+System.currentTimeMillis());
=======
>>>>>>> origin/AmirV1
		// Sort and get the next player to act (immediately after the big blind)
		List<PlayerHand> players = new ArrayList<PlayerHand>();
		players.addAll(participatingPlayers);
		Player nextToAct = PlayerUtil.getNextPlayerToAct(hand,
<<<<<<< HEAD
				this.getPlayerInBB(hand));
		hand.setCurrentToAct(nextToAct);
		System.out.println("3 "+System.currentTimeMillis());
=======
				this.getPlayerInBB(hand), PlayerHandStatus.NORMAL);
		hand.setCurrentToAct(nextToAct);
>>>>>>> origin/AmirV1
		// Register the Forced Small and Big Blind bets as part of the hand
		Player smallBlind = getPlayerInSB(hand);
		Player bigBlind = getPlayerInBB(hand);
		System.out.println("4 "+System.currentTimeMillis());
		int sbBet = 0;
		int bbBet = 0;
<<<<<<< HEAD
		for (PlayerHand ph : hand.getPlayers()) {
=======
		for (PlayerHand ph : hand.getPlayers()) {// false
>>>>>>> origin/AmirV1
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
<<<<<<< HEAD
		System.out.println("5 "+System.currentTimeMillis());
=======
		playerDao.merge(smallBlind, null);
		playerDao.merge(bigBlind, null);
>>>>>>> origin/AmirV1
		hand.setTotalBetAmount(hand.getBlindLevel().getBigBlind());
		hand.setLastBetAmount(hand.getBlindLevel().getBigBlind());
		hand.setPot(sbBet + bbBet);
		BoardEntity b = new BoardEntity();
		hand.setBoard(b);
		hand.setCards(d.exportDeck());
		hand = handDao.save(hand, null);
<<<<<<< HEAD
		System.out.println("6 "+System.currentTimeMillis());
=======
>>>>>>> origin/AmirV1
		game.setCurrentHand(hand);
		gameDao.merge(game, null);
		hand.setGame(null);
		return hand;
	}

<<<<<<< HEAD
	public void endHand(HandEntity hand) throws AMSException {
		gameDao = new GameDaoImpl();
		handDao = new HandDaoImpl();
		playerDao = new PlayerDaoImpl();
		if (!isActionResolved(hand)) {
			throw new AMSException(
					"There are unresolved betting actions");
		}

		Game game = hand.getGame();
=======
	public void endHand(Game g) throws AMSException {
		gameDao = new GameDaoImpl();
		handDao = new HandDaoImpl();
		playerDao = new PlayerDaoImpl();
		// HandEntity hand = handDao.findById(g.getCurrentHand().getId(), null);
		HandEntity hand = g.getCurrentHand();
		if (!isActionResolved(hand)) {
			river(g);
			return;
			// throw new AMSException("There are unresolved betting actions");
		}
>>>>>>> origin/AmirV1

		// Game game = hand.getGame();
		Game game = g;
		hand.setCurrentToAct(null);
<<<<<<< HEAD

		determineWinner(hand);

		// If players were eliminated this hand, set their finished position
		List<PlayerHand> phs = new ArrayList<PlayerHand>();
		phs.addAll(hand.getPlayers());
=======

		hand = determineWinner(hand);

		// If players were eliminated this hand, set their finished position
		List<PlayerHand> phs = new ArrayList<PlayerHand>();
		phs.addAll(hand.getPlayers());// true
>>>>>>> origin/AmirV1
		// Sort the list of PlayerHands so the one with the smallest chips at
		// risk is first.
		// Use this to determine finish position if multiple players are
		// eliminated on the same hand.
		Collections.sort(phs, new PlayerHandBetAmountComparator());
		for (PlayerHand ph : phs) {
			if (ph.getPlayer().getChips() <= 0) {
				ph.getPlayer().setFinishPosition(game.getPlayersRemaining());
				game.setPlayersRemaining(game.getPlayersRemaining() - 1);
				playerDao.merge(ph.getPlayer(), null);
			}
		}

		// For all players in the game, remove any who are out of chips
		// (eliminated)
		List<Player> players = new ArrayList<Player>();
		for (Player p : game.getPlayers()) {
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
<<<<<<< HEAD
		if (game.getPlayersRemaining() < 2) {
			// TODO end game. Move to start hand?
		}
=======
>>>>>>> origin/AmirV1

		// Rotate Button. Use Simplified Moving Button algorithm (for ease of
		// coding)
		// This means we always rotate button. Blinds will be next two active
		// players. May skip blinds.
		Player nextButton = PlayerUtil.getNextPlayerInGameOrder(players,
				game.getPlayerInBTN());
		game.setPlayerInBTN(nextButton);
<<<<<<< HEAD

		gameDao.merge(game, null);
=======
		game = gameDao.merge(game, null);
>>>>>>> origin/AmirV1

		// Remove Deck from database. No need to keep that around anymore
		hand.setCards(new ArrayList<Card>());
		hand.setCurrentToAct(null);
		hand.setGame(game);
		handDao.merge(hand, null);
		final long gameId = game.getId();
		final ScheduledExecutorService scheduler = Executors
				.newScheduledThreadPool(1);
		ScheduledFuture<?> countdown = scheduler.schedule(new Runnable() {
			@Override
			public void run() {
				GameDaoImpl gameDao = new GameDaoImpl();
				Game game = gameDao.findById(gameId, null);
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
		}, 10, TimeUnit.SECONDS);
		// }
	}

	public HandEntity getHandById(long id) {
		handDao = new HandDaoImpl();
		return handDao.findById(id, null);
	}

<<<<<<< HEAD
	public HandEntity flop(HandEntity hand) throws IllegalStateException {
		handDao = new HandDaoImpl();
		if (hand.getBoard().getFlop1() != null) {
			throw new IllegalStateException("Unexpected Flop.");
		}
		// Re-attach to persistent context for this transaction (Lazy Loading
		// stuff)
		hand = handDao.merge(hand, null);
		if (!isActionResolved(hand)) {
			throw new IllegalStateException(
					"There are unresolved preflop actions");
		}

=======
	public HandEntity flop(Game game) throws IllegalStateException {
		handDao = new HandDaoImpl();
		HandEntity hand = game.getCurrentHand();
		if (hand.getBoard().getFlop1() != null) {
			throw new IllegalStateException("Unexpected Flop.");
		}
		hand.setGame(game);
		Player next = new Player();
		if (!isActionResolved(hand)) {
			next = PlayerUtil.getNextPlayerToAct(hand, hand.getCurrentToAct(),
					PlayerHandStatus.FLOPEND);
//			hand.setCurrentToAct(next);
			hand = handDao.merge(hand, null);
			return hand;
			// throw new IllegalStateException(
			// "There are unresolved preflop actions");
		}
>>>>>>> origin/AmirV1
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
		next = PlayerUtil.getNextPlayerToAct(hand, hand.getCurrentToAct(),
				PlayerHandStatus.FLOPEND);
		if (playerHand != null
				&& hand.getTotalBetAmount() > playerHand.getRoundBetAmount()
				&& playerHand.getStatus() != PlayerHandStatus.FOLDED) {
			PlayerUtil.removePlayerFromHand(hand.getCurrentToAct(), hand);
		}
//		hand.setCurrentToAct(next);
		hand = handDao.merge(hand, null);
		return hand;
	}

<<<<<<< HEAD
	public HandEntity turn(HandEntity hand) throws IllegalStateException {
=======
	public HandEntity turn(Game game) throws IllegalStateException {
		HandEntity hand = game.getCurrentHand();
>>>>>>> origin/AmirV1
		if (hand.getBoard().getFlop1() == null
				|| hand.getBoard().getTurn() != null) {
			throw new IllegalStateException("Unexpected Turn.");
		}
		handDao = new HandDaoImpl();
		// Re-attach to persistent context for this transaction (Lazy Loading
		// stuff)
<<<<<<< HEAD
		hand = handDao.merge(hand, null);
		if (!isActionResolved(hand)) {
			throw new IllegalStateException("There are unresolved flop actions");
		}

=======
		// hand = handDao.findById(hand.getId(), null);
		hand.setGame(game);
		Player next = new Player();
		if (!isActionResolved(hand)) {
			next = PlayerUtil.getNextPlayerToAct(hand, hand.getCurrentToAct(),
					PlayerHandStatus.FLOPEND);
//			hand.setCurrentToAct(next);
			hand = handDao.merge(hand, null);
			return hand;
			// throw new IllegalStateException(
			// "There are unresolved preflop actions");
		}
>>>>>>> origin/AmirV1
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
		next = PlayerUtil.getNextPlayerToAct(hand,
				hand.getCurrentToAct(), PlayerHandStatus.TURNEND);
		if (playerHand != null
				&& hand.getTotalBetAmount() > playerHand.getRoundBetAmount()
				&& playerHand.getStatus() != PlayerHandStatus.FOLDED) {
			PlayerUtil.removePlayerFromHand(hand.getCurrentToAct(), hand);
		}
//		hand.setCurrentToAct(next);
		hand = handDao.merge(hand, null);
		return hand;
	}

<<<<<<< HEAD
	public HandEntity river(HandEntity hand) throws IllegalStateException {
=======
	public HandEntity river(Game game) throws IllegalStateException {
		HandEntity hand = game.getCurrentHand();
>>>>>>> origin/AmirV1
		if (hand.getBoard().getFlop1() == null
				|| hand.getBoard().getTurn() == null
				|| hand.getBoard().getRiver() != null) {
			throw new IllegalStateException("Unexpected River.");
		}
		handDao = new HandDaoImpl();
		// Re-attach to persistent context for this transaction (Lazy Loading
		// stuff)
<<<<<<< HEAD
		hand = handDao.merge(hand, null);
		if (!isActionResolved(hand)) {
			throw new IllegalStateException("There are unresolved turn actions");
=======
		// hand = handDao.findById(hand.getId(), null);
		hand.setGame(game);
		Player next = new Player();
		if (!isActionResolved(hand)) {
			next = PlayerUtil.getNextPlayerToAct(hand, hand.getCurrentToAct(),
					PlayerHandStatus.FLOPEND);
//			hand.setCurrentToAct(next);
			hand = handDao.merge(hand, null);
			return hand;
			// throw new IllegalStateException(
			// "There are unresolved preflop actions");
>>>>>>> origin/AmirV1
		}

		Deck d = new Deck(hand.getCards());
		d.shuffleDeck();
		BoardEntity board = hand.getBoard();
		board.setRiver(d.dealCard());
		hand.setCards(d.exportDeck());
		resetRoundValues(hand);
<<<<<<< HEAD
		return handDao.merge(hand, null);
	}

	public boolean sitOutCurrentPlayer(HandEntity hand) {
		handDao = new HandDaoImpl();
		playerDao = new PlayerDaoImpl();
=======
		PlayerHand playerHand = null;
		for (PlayerHand ph : hand.getPlayers()) {// false
			if (ph.getPlayer() != null
					&& ph.getPlayer().equals(hand.getCurrentToAct())) {
				playerHand = ph;
				break;
			}
		}
		next = PlayerUtil.getNextPlayerToAct(hand,
				hand.getCurrentToAct(), PlayerHandStatus.RIVEREND);
		if (playerHand != null
				&& hand.getTotalBetAmount() > playerHand.getRoundBetAmount()
				&& playerHand.getStatus() != PlayerHandStatus.FOLDED) {
			PlayerUtil.removePlayerFromHand(hand.getCurrentToAct(), hand);
		}
//		hand.setCurrentToAct(next);
>>>>>>> origin/AmirV1
		hand = handDao.merge(hand, null);
		// for (PlayerHand ph : hand.getPlayers(false)) {
		// handDao.updatePlayerHand(ph, null);
		// }
		return hand;
	}

	public boolean sitOutCurrentPlayer(HandEntity hand) {
		handDao = new HandDaoImpl();
		playerDao = new PlayerDaoImpl();
		Player currentPlayer = hand.getCurrentToAct();
		if (currentPlayer == null) {
			return false;
		}
		currentPlayer.setSittingOut(true);
		playerDao.merge(currentPlayer, null);
		PlayerHand playerHand = null;
<<<<<<< HEAD
		for (PlayerHand ph : hand.getPlayers()) {
=======
		for (PlayerHand ph : hand.getPlayers()) {// false
>>>>>>> origin/AmirV1
			if (ph.getPlayer().equals(currentPlayer)) {
				playerHand = ph;
				break;
			}
		}
<<<<<<< HEAD

		// Move action to the next player
		Player next = PlayerUtil.getNextPlayerToAct(hand, currentPlayer);
		// If the player being sat out needs to call, that player is folded
		if (playerHand != null
				&& hand.getTotalBetAmount() > playerHand.getRoundBetAmount()) {
			PlayerUtil.removePlayerFromHand(currentPlayer, hand);
		}

		hand.setCurrentToAct(next);
		handDao.save(hand, null);
=======
		Player next = PlayerUtil.getNextPlayerToAct(hand, currentPlayer,
				PlayerHandStatus.SITOUT);
		if (playerHand != null
				&& hand.getTotalBetAmount() > playerHand.getRoundBetAmount()
				&& playerHand.getStatus() != PlayerHandStatus.FOLDED) {
			PlayerUtil.removePlayerFromHand(currentPlayer, hand);
		}
//		hand.setCurrentToAct(next);
		handDao.merge(hand, null);
>>>>>>> origin/AmirV1
		return true;
	}

	@Override
	public Player getPlayerInSB(HandEntity hand) {
		Player button = hand.getGame().getPlayerInBTN();
		// Heads up the Button is the Small Blind
<<<<<<< HEAD
		if (hand.getPlayers().size() == 2) {
=======
		if (hand.getPlayers().size() == 2) {// false
>>>>>>> origin/AmirV1
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
<<<<<<< HEAD
		players.addAll(hand.getPlayers());
		Player leftOfButton = PlayerUtil.getNextPlayerInGameOrderPH(players,
				button);
		// Heads up, the player who is not the Button is the Big blind
		if (hand.getPlayers().size() == 2) {
=======
		for (PlayerHand ph : hand.getPlayers()) {// false
			if (ph.getPlayer() != null)
				players.add(ph);
		}
		Player leftOfButton = PlayerUtil.getNextPlayerInGameOrderPH(players,
				button);
		// Heads up, the player who is not the Button is the Big blind
		if (hand.getPlayers().size() == 2) {// false
>>>>>>> origin/AmirV1
			return leftOfButton;
		}
		return PlayerUtil.getNextPlayerInGameOrderPH(players, leftOfButton);
	}

	private void updateBlindLevel(Game game) {
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

	private void setNewBlindEndTime(Game game) {
		Calendar c = Calendar.getInstance();
		c.add(Calendar.MINUTE, game.getGameStructure().getBlindLength());
		game.getGameStructure().setCurrentBlindEndTime(c.getTime());
	}

	private void resetRoundValues(HandEntity hand) {
		hand.setTotalBetAmount(0);
		hand.setLastBetAmount(0);
<<<<<<< HEAD

		List<Player> playersInHand = new ArrayList<Player>();
		for (PlayerHand ph : hand.getPlayers()) {
=======
		handDao = new HandDaoImpl();
		List<Player> playersInHand = new ArrayList<Player>();
		for (PlayerHand ph : hand.getPlayers()) {// false
>>>>>>> origin/AmirV1
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
//		hand.setCurrentToAct(next);
		handDao.merge(hand, null);
	}

<<<<<<< HEAD
	private void determineWinner(HandEntity hand) {
		// if only one PH left, everyone else folded
		if (hand.getPlayers().size() == 1) {
			Player winner = hand.getPlayers().iterator().next().getPlayer();
			winner.setChips(winner.getChips() + hand.getPot());
			playerDao.merge(winner, null);
		} else {
			// Refund all in overbet player if applicable before determining
			// winner
			refundOverbet(hand);

			// Iterate through map of players to there amount won. Persist.
			Map<Player, Integer> winners = PlayerUtil
					.getAmountWonInHandForAllPlayers(hand);
			if (winners == null) {
				return;
=======
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
>>>>>>> origin/AmirV1
			}
			for (Map.Entry<Player, Integer> entry : winners.entrySet()) {
				Player player = entry.getKey();
				player.setChips(player.getChips() + entry.getValue());
				playerDao.merge(player, null);
			}
		}
		return hand;
	}

<<<<<<< HEAD
	private void refundOverbet(HandEntity hand) {
		List<PlayerHand> phs = new ArrayList<PlayerHand>();
		phs.addAll(hand.getPlayers());
=======
	private HandEntity refundOverbet(HandEntity hand) {
		List<PlayerHand> phs = new ArrayList<PlayerHand>();
		phs.addAll(hand.getPlayers());// false
>>>>>>> origin/AmirV1
		// Sort from most money contributed, to the least
		Collections.sort(phs, new PlayerHandBetAmountComparator());
		Collections.reverse(phs);
		// If there are at least 2 players, and the top player contributed more
		// to the pot than the next player
<<<<<<< HEAD
=======

>>>>>>> origin/AmirV1
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
	private boolean isActionResolved(HandEntity hand) {
		int roundBetAmount = hand.getTotalBetAmount();
<<<<<<< HEAD
		for (PlayerHand ph : hand.getPlayers()) {
			// All players should have paid the roundBetAmount or should be all
			// in
			if (ph.getRoundBetAmount() != roundBetAmount
=======
		for (PlayerHand ph : hand.getPlayers()) {// true
			// All players should have paid the roundBetAmount or should be all
			// in
			if (ph.getPlayer() != null
					&& ph.getRoundBetAmount() != roundBetAmount
>>>>>>> origin/AmirV1
					&& ph.getPlayer().getChips() > 0) {
				return false;
			}
		}
		return true;
	}

}
