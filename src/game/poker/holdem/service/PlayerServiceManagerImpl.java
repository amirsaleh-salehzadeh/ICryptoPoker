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

import common.game.poker.holdem.GameENT;
import game.poker.holdem.dao.GameDaoImpl;
import game.poker.holdem.dao.HandDaoImpl;
import game.poker.holdem.domain.GameStatus;
import game.poker.holdem.domain.HandEntity;
import game.poker.holdem.domain.Player;
import game.poker.holdem.domain.PlayerHand;
import game.poker.holdem.domain.PlayerStatus;
import game.poker.holdem.holder.Hand;
import game.poker.holdem.util.GameUtil;
import game.poker.holdem.view.PlayerStatusObject;

public class PlayerServiceManagerImpl implements PlayerServiceManager {

//	private GameServiceImpl gameService;

	private PlayerActionServiceImpl playerActionService;

	private PokerHandServiceImpl handService;

	public PlayerStatusObject buildPlayerStatus(long gameId, String playerId) {
//		gameService = new GameServiceImpl();
		GameDaoImpl gameDao = new GameDaoImpl();
		GameENT game = gameDao.findById(gameId, null);

		playerActionService = new PlayerActionServiceImpl();
		Player player = playerActionService.getPlayerById(playerId);
		PlayerStatusObject results = new PlayerStatusObject();
		results.setName(player.getName());
		handService = new PokerHandServiceImpl();

		// ADDED THESE THREE LINES THERE WAS AN ERROR
		if (game.getCurrentHand() != null) {
			HandEntity handtmp = game.getCurrentHand();
			handtmp.setGame(game);
			game.setCurrentHand(handtmp);
		}

		// UNTIL HERE

		// Get the player status.
		// In the special case of preflop, player is not current to act, see if
		// the player is SB or BB
		PlayerStatus playerStatus = playerActionService.getPlayerStatus(player);
		if (playerStatus == PlayerStatus.WAITING
				&& GameUtil.getGameStatus(game) == GameStatus.PREFLOP) {
			if (player.equals(handService.getPlayerInSB(game.getCurrentHand()))) {
				playerStatus = PlayerStatus.POST_SB;
			} else if (player.equals(handService.getPlayerInBB(game
					.getCurrentHand()))) {
				playerStatus = PlayerStatus.POST_BB;
			}
		}
		results.setStatus(playerStatus);

		results.setChips(player.getChips());
		if (game.getGameStructure().getCurrentBlindLevel() != null) {
			results.setSmallBlind(game.getGameStructure()
					.getCurrentBlindLevel().getSmallBlind());
			results.setBigBlind(game.getGameStructure().getCurrentBlindLevel()
					.getBigBlind());
		}
		if (game.getCurrentHand() != null) {
			HandEntity hand = game.getCurrentHand();
			PlayerHand playerHand = null;
			for (PlayerHand ph : hand.getPlayers()) {//true
				if (ph.getPlayer() != null && ph.getPlayer().equals(player)) {
					playerHand = ph;
					break;
				}
			}
			if (playerHand != null) {
				results.setCard1(playerHand.getHand().getCard(0).toString());
				results.setCard2(playerHand.getHand().getCard(1).toString());
				results.setAmountBetRound(playerHand.getRoundBetAmount());

				int toCall = hand.getTotalBetAmount()
						- playerHand.getRoundBetAmount();
				toCall = Math.min(toCall, player.getChips());
				if (toCall > 0) {
					results.setAmountToCall(toCall);
				}
			}
		}
		results.setId(playerId);
		return results;
	}

}
