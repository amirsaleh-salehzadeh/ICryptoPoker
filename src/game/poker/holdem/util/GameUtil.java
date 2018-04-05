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
package game.poker.holdem.util;

import tools.AMSException;
import common.game.poker.holdem.GameENT;
import game.poker.holdem.domain.GameStatus;
import game.poker.holdem.domain.HandEntity;
import game.poker.holdem.domain.Player;
import game.poker.holdem.domain.PlayerHand;
import game.poker.holdem.service.PokerHandServiceImpl;

/**
 * Static helper methods for controlling game functions and information
 * 
 * @author jacobhyphenated
 */
public class GameUtil {

	public static GameStatus getGameStatus(GameENT game) {
		if (!game.isStarted()) {
			return GameStatus.NOT_STARTED;
		}
		HandEntity hand = game.getCurrentHand();
		if (hand == null) {
			return GameStatus.SEATING;
		}
		if (hand.getCurrentToAct() == null) {
			return GameStatus.END_HAND;
		}
		if (hand.getBoard().getRiver() != null) {
			return GameStatus.RIVER;
		}
		if (hand.getBoard().getTurn() != null) {
			return GameStatus.TURN;
		}
		if (hand.getBoard().getFlop3() != null) {
			return GameStatus.FLOP;
		}
		return GameStatus.PREFLOP;
	}

	public static void goToNextStepOfTheGame(GameENT game, String playerId)
			throws AMSException {
		System.out.println("goToNextStepOfTheGame");
		if (!PokerHandServiceImpl.isActionResolved(game.getCurrentHand()))
			return;
		System.out.println("goToNextStepOfTheGame isActionResolved");
		PokerHandServiceImpl phs = new PokerHandServiceImpl();
		GameStatus gs = GameUtil.getGameStatus(game);
		if (gs.equals(GameStatus.PREFLOP))
			phs.flop(game);
		else if (gs.equals(GameStatus.FLOP))
			phs.turn(game);
		else if (gs.equals(GameStatus.TURN))
			phs.river(game);
		else if (gs.equals(GameStatus.RIVER))
			phs.endHand(game);
	}
}
