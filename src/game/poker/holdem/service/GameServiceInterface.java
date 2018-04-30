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

import java.util.Map;

import common.game.poker.holdem.GameENT;
import tools.AMSException;
import game.poker.holdem.domain.Player;

/**
 * Service to handle the overall game operations
 * 
 * @author jacobhyphenated
 */
public interface GameServiceInterface {


	/**
	 * Start a game. This begins the current game tracking. Setup of the game is
	 * completed. If it is a tournament, all players should be registered at
	 * this time.<br />
	 * <br />
	 * 
	 * This will assign starting positions to all of the players. This will not
	 * start the blind level, that will happen at the start of the first hand.
	 * 
	 * @param game
	 * @return
	 */
	public GameENT startGame(GameENT game) throws AMSException;
	
	
	/**
	 * Add a new player to an existing game
	 * 
	 * @param game
	 *            game to add the player to
	 * @param player
	 *            {@link Player} to add to the game
	 * @return Player with persisted context. Null if the game is not accepting
	 *         new players.
	 */
	public GameENT addNewPlayerToGame(GameENT game, Player player);

	/**
	 * Persist any changes to a {@link Player} domain object. Or create a new
	 * one.
	 * 
	 * @param player
	 *            Player to be saved
	 * @return Player attached to the persistent context
	 */
	// TODO List active games
	// Games that are not started, games that are cash games and still in
	// progress
	// Need a way to weed out old/expired/inactive games

	// TODO Player add Chips method - for cash games and rebuy tournaments
	// Do this in service layer to enforce tournament logic.
	// Cash games could theoretically be done in the controller and call save

	public String getGameStatusJSON(GameENT game, Map<String, Object> results,
			String playerId);
	
	public void closeTheGame(GameENT eitherGame, Player eitherPlayer);
	
	public void leaveTheGame(Player player, GameENT game);
}
