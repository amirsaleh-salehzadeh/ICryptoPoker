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
package game.poker.holdem.dao;

import java.sql.Connection;
import java.util.List;
import java.util.Set;

import game.poker.holdem.domain.Game;
import game.poker.holdem.domain.GameStructure;
import game.poker.holdem.domain.Player;

public interface GameDao {

	Game save(Game game, Connection conn);

	Game merge(Game game, Connection conn);

	Game findById(long id, Connection conn);

	List<Game> getAllGames(Connection conn);

	Set<Player> getAllPlayersInGame(long id, Connection conn);

	GameStructure getGameStructure(long id, Connection conn);

	GameStructure saveGameStructure(GameStructure gs, Connection conn);

	GameStructure mergeGameStructure(GameStructure gs, Connection conn);

}
