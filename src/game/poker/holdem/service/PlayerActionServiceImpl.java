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

import game.poker.holdem.dao.GameDaoImpl;
import game.poker.holdem.dao.HandDaoImpl;
import game.poker.holdem.dao.PlayerDaoImpl;
import game.poker.holdem.domain.HandEntity;
import game.poker.holdem.domain.Player;
import game.poker.holdem.domain.PlayerHand;
import game.poker.holdem.domain.PlayerHandStatus;
import game.poker.holdem.domain.PlayerStatus;
import game.poker.holdem.util.GameUtil;
import game.poker.holdem.util.PlayerUtil;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import common.game.poker.holdem.GameENT;
import tools.AMSException;
import webservices.PlayerServiceWS;

public class PlayerActionServiceImpl implements PlayerActionServiceInterface {

	private PlayerDaoImpl playerDao;
	private HandDaoImpl handDao;
	private GameDaoImpl gameDao;
	private PlayerServiceWS playerServiceWS;

	public PlayerActionServiceImpl() {
		super();
		playerDao = new PlayerDaoImpl();
		handDao = new HandDaoImpl();
		gameDao = new GameDaoImpl();
		playerServiceWS = new PlayerServiceWS();
	}

	public Player getPlayerById(String playerId) {
		return playerDao.findById(playerId, null);
	}

	public HandEntity fold(Player player, GameENT game, boolean forceFold) {
		HandEntity hand = game.getCurrentHand();
		if (!player.equals(hand.getCurrentToAct())) {
			return null;
		}

		Player next = PlayerUtil.getNextPlayerToAct(hand, player);
		if (!PlayerUtil.removePlayerFromHand(player, hand)) {
			return null;
		}
		handDao = new HandDaoImpl();
		hand.setCurrentToAct(next);
		hand.setGame(game);
		hand = handDao.merge(hand, null);
		game.setCurrentHand(hand);
		PokerHandServiceImpl phs = new PokerHandServiceImpl();
		int numberOfPH = 0;
		for (PlayerHand ph : hand.getPlayers())
			if (ph.getStatus() != PlayerHandStatus.FOLDED) {
				numberOfPH++;
			}
		if (numberOfPH <= 1)
			try {
				phs.endHand(game);
			} catch (AMSException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		return hand;
	}

	public HandEntity check(Player player, GameENT game) {
		HandEntity hand = game.getCurrentHand();
		hand.setGame(game);

		// Cannot check out of turn
		if (!player.equals(hand.getCurrentToAct())) {
			return null;
		}

		// Checking is not currently an option
		if (getPlayerStatus(player) != PlayerStatus.ACTION_TO_CHECK) {
			return null;
		}

		Player next = PlayerUtil.getNextPlayerToAct(hand, player);
		hand.setCurrentToAct(next);
		hand = handDao.merge(hand, null);
		return hand;
	}

	public HandEntity bet(Player player, GameENT game, int betAmount) {
		HandEntity hand = game.getCurrentHand();
		hand.setGame(game);
		if (!player.equals(hand.getCurrentToAct())) {
			return null;
		}

		// Bet must meet the minimum of twice the previous bet. Call bet amount
		// and raise exactly that amount or more
		// Alternatively, if there is no previous bet, the first bet must be at
		// least the big blind
		if (betAmount < hand.getLastBetAmount()
				|| betAmount < hand.getBlindLevel().getBigBlind()) {
			return null;
		}

		PlayerHand playerHand = null;
		for (PlayerHand ph : hand.getPlayers()) {// false
			if (ph.getPlayer() != null && ph.getPlayer().equals(player)) {
				playerHand = ph;
				break;
			}
		}

		int toCall = hand.getTotalBetAmount() - playerHand.getRoundBetAmount();
		int total = betAmount + toCall;
		if (total > player.getChips()) {
			total = player.getChips();
			betAmount = total - toCall;
		}

		playerHand.setRoundBetAmount(playerHand.getRoundBetAmount() + total);
		playerHand.setBetAmount(playerHand.getBetAmount() + total);
		handDao.updatePlayerHand(playerHand, null);
		player.setChips(player.getChips() - total);
		hand.setPot(hand.getPot() + total);
		hand.setLastBetAmount(betAmount);
		hand.setTotalBetAmount(hand.getTotalBetAmount() + betAmount);

		Player next = PlayerUtil.getNextPlayerToAct(hand, player);
		hand.setCurrentToAct(next);
		playerDao.merge(player, null);
		hand = handDao.merge(hand, null);
		return hand;
	}

	public HandEntity call(Player player, GameENT game) {
		HandEntity hand = game.getCurrentHand();
		hand.setGame(game);
		// Cannot call out of turn
		if (!player.equals(hand.getCurrentToAct())) {
			return null;
		}
		PlayerHand playerHand = null;
		for (PlayerHand ph : hand.getPlayers()) {// false
			if (ph.getPlayer().equals(player)) {
				playerHand = ph;
				break;
			}
		}

		int toCall = hand.getTotalBetAmount() - playerHand.getRoundBetAmount();
		toCall = Math.min(toCall, player.getChips());
		if (toCall <= 0) {
			return null;
		}
		playerHand.setRoundBetAmount(playerHand.getRoundBetAmount() + toCall);
		playerHand.setBetAmount(playerHand.getBetAmount() + toCall);
		handDao.updatePlayerHand(playerHand, null);
		player.setChips(player.getChips() - toCall);
		hand.setPot(hand.getPot() + toCall);

		Player next = PlayerUtil.getNextPlayerToAct(hand, player);
		hand.setCurrentToAct(next);
		hand.setGame(game);
		player.setGameId(game.getId());
		playerDao.merge(player, null);
		hand = handDao.merge(hand, null);
		return hand;
	}

	public void sitIn(Player player) {
		player.setSittingOut(false);
		playerDao.merge(player, null);
	}

	public PlayerStatus getPlayerStatus(Player player) {
		player = playerDao.findById(player.getId(), null);
		if (player == null) {
			return PlayerStatus.ELIMINATED;
		}

		if (player.isSittingOut()) {
			return PlayerStatus.SIT_OUT_GAME;
		}
		GameDaoImpl gdao = new GameDaoImpl();
		GameENT game = gdao.findById(player.getGameId(), null);
		if (!game.isStarted()) {
			return PlayerStatus.NOT_STARTED;
		}

		HandEntity hand = game.getCurrentHand();
		if (hand == null) {
			return PlayerStatus.SEATING;
		}
		PlayerHand playerHand = null;
		for (PlayerHand ph : hand.getPlayers()) {// true
			if (ph.getPlayer() != null && ph.getPlayer().equals(player)) {
				playerHand = ph;
				break;
			}
		}
		if (!hand.getPlayers().contains(playerHand)) {// true
			if (player.getChips() <= 0) {
				return PlayerStatus.ELIMINATED;
			}
			return PlayerStatus.SIT_OUT;
		}

		if (hand.getCurrentToAct() == null) {
			Set<PlayerHand> phs = hand.getPlayers();
			Set<PlayerHand> phsTMP = new HashSet<PlayerHand>();
			for (PlayerHand p : phs) {
				if (p.getStatus() == PlayerHandStatus.FOLDED) {
					phsTMP.add(playerHand);
				}
			}
			if (phs.size() - phsTMP.size() == 1) {
				for (PlayerHand p : phsTMP)
					if (p.getStatus() != PlayerHandStatus.FOLDED)
						return PlayerStatus.WON_HAND;
					else
						return PlayerStatus.LOST_HAND;
			}

			// Only one player, everyone else folded, player is the winner
			if (hand.getPlayers().size() == 1) {// true
				return PlayerStatus.WON_HAND;
			}
			// Get the list of players who won at least some amount of chips at
			// showdown
			Map<Player, Integer> winners = PlayerUtil
					.getAmountWonInHandForAllPlayers(hand);
			if (winners != null && winners.containsKey(player)) {
				// Player is contained in this collection, so the player was a
				// winner in the hand
				return PlayerStatus.WON_HAND;
			} else {
				// Hand is over but player lost at showdown.
				return PlayerStatus.LOST_HAND;
			}
		}

		if (player.getChips() <= 0) {
			return PlayerStatus.ALL_IN;
		}

		if (!player.equals(hand.getCurrentToAct())) {
			// Small and Big Blind to be determined later?
			// Let controller handle that status
			return PlayerStatus.WAITING;
		}

		if (hand.getTotalBetAmount() > playerHand.getRoundBetAmount()) {
			return PlayerStatus.ACTION_TO_CALL;
		} else if (playerHand.getRoundBetAmount() > 0) {
			// We have placed a bet but now our action is check? This means the
			// round of betting is over
			// TODO still problem when every player checks or BB. Need
			// additional info to solve this
			return PlayerStatus.ACTION_TO_CHECK;
		} else {
			return PlayerStatus.ACTION_TO_CHECK;
		}

	}

}
