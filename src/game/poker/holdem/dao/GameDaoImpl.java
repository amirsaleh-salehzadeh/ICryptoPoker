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
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

import tools.AMSException;

import com.mysql.jdbc.Statement;
import common.user.UserENT;

import game.poker.holdem.domain.Game;
import game.poker.holdem.domain.GameStructure;
import game.poker.holdem.domain.GameType;
import game.poker.holdem.domain.Player;
import hibernate.config.BaseHibernateDAO;

public class GameDaoImpl extends BaseHibernateDAO implements GameDao {

	@Override
	public Game findById(long id, Connection conn) {
		Game game = new Game();
		try {
			boolean isNewConn = false;
			if (conn == null)
				try {
					conn = getConnection();
					conn.setAutoCommit(false);
					isNewConn = true;
				} catch (AMSException e) {
					e.printStackTrace();
				}
			String query = "";
			query = "select * from game where game_id = " + id;
			PreparedStatement ps = conn.prepareStatement(query);
			ps.execute();
			ResultSet rs = ps.getResultSet();
			HandDaoImpl handDaoImpl = new HandDaoImpl();
			PlayerDaoImpl player = new PlayerDaoImpl();
			while (rs.next()) {
				game.setName(rs.getString("players_left"));
				if (rs.getString("game_type").equalsIgnoreCase("T"))
					game.setGameType(GameType.TOURNAMENT);
				else
					game.setGameType(GameType.CASH);
				game.setName(rs.getString("name"));
				game.setStarted(rs.getBoolean("is_started"));
				game.setCurrentHand(handDaoImpl.findById(
						rs.getLong("current_hand_id"), conn));
				game.setGameStructure(getGameStructure(id, null));
				game.setPlayerInBTN(player.findById(
						rs.getString("btn_player_id"), conn));
				game.setPlayers(getAllPlayersInGame(id, conn));
			}
			rs.close();
			ps.close();
			if (isNewConn) {
				conn.commit();
				conn.close();
			}
		} catch (SQLException e) {
			try {
				conn.rollback();
				conn.close();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			e.printStackTrace();
		}
		return game;
	}

	public Game save(Game game, Connection conn) {
		try {
			boolean isNewConn = false;
			if (conn == null)
				try {
					conn = getConnection();
					conn.setAutoCommit(false);
					isNewConn = true;
				} catch (AMSException e) {
					e.printStackTrace();
				}
			String query = "";
			query = "INSERT INTO `game` (`players_left`, `game_type`, `name`, `is_started`, `current_hand_id`, `game_structure_id`, `btn_player_id`) "
					+ "VALUES (?, ?, ?, ?, ?, ?, ?);";
			PreparedStatement ps = conn.prepareStatement(query,
					Statement.RETURN_GENERATED_KEYS);
			ps.setInt(1, game.getPlayersRemaining());
			ps.setString(2, game.getGameType().name());
			ps.setString(3, game.getName());
			if (game.isStarted()) {
				ps.setInt(4, 1);
			} else
				ps.setInt(4, 0);
			ps.setLong(5, game.getCurrentHand().getId());
			ps.setLong(6, game.getGameStructure().getId());
			ps.setString(7, game.getPlayerInBTN().getId());
			ps.executeUpdate();
			ResultSet rs = ps.getGeneratedKeys();
			GameDaoImpl gdi = new GameDaoImpl();
			if (rs.next()) {
				game.setId(rs.getLong(1));
			}
			rs.close();
			ps.close();
			if (isNewConn) {
				conn.commit();
				conn.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return game;
	}

	@Override
	public Game merge(Game game, Connection conn) {
		try {
			conn = getConnection();
			conn.setAutoCommit(false);
			String query = "";
			query = "UPDATE `game`  SET `players_left` = ?, `game_type` = ?, `name` =?, `is_started` = ?, "
					+ "`current_hand_id` = ?, `game_structure_id` =?, `btn_player_id` =? WHERE `game_id` = ? "
					+ "VALUES (?, ?, ?, ?, ?, ?, ?);";
			PreparedStatement ps = conn.prepareStatement(query,
					Statement.RETURN_GENERATED_KEYS);
			ps.setInt(1, game.getPlayersRemaining());
			ps.setString(2, game.getGameType().name());
			ps.setString(3, game.getName());
			if (game.isStarted()) {
				ps.setInt(4, 1);
			} else
				ps.setInt(4, 0);
			ps.setLong(5, game.getCurrentHand().getId());
			ps.setLong(6, game.getGameStructure().getId());
			ps.setString(7, game.getPlayerInBTN().getId());
			ps.setLong(8, game.getId());
			ps.executeUpdate();
			ResultSet rs = ps.getGeneratedKeys();
			if (rs.next()) {
				game.setId(rs.getLong(1));
			}
			rs.close();
			ps.close();
			conn.commit();
			conn.close();
		} catch (AMSException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return game;
	}

	@Override
	public Set<Player> getAllPlayersInGame(long id, Connection conn) {
		Set<Player> players = new HashSet<Player>();
		try {
			boolean isNewConn = false;
			if (conn == null)
				try {
					conn = getConnection();
					conn.setAutoCommit(false);
					isNewConn = true;
				} catch (AMSException e) {
					e.printStackTrace();
				}
			String query = "";
			query = "select * from player where game_id = " + id;
			PreparedStatement ps = conn.prepareStatement(query);
			ps.execute();
			ResultSet rs = ps.getResultSet();
			while (rs.next()) {
				Player p = new Player();
				p.setId(rs.getString("username"));
				p.setName(rs.getString("name"));
				p.setPassword(rs.getString("password"));
				p.setRegistrationDate(rs.getString("registeration_date	"));
				p.setGamePosition(rs.getInt("game_position"));
				p.setFinishPosition(rs.getInt("finished_place"));
				if (rs.getInt("finished_place") == 1)
					p.setSittingOut(true);
				else
					p.setSittingOut(false);
				p.setChips(rs.getInt("chips"));
				players.add(p);
			}
			rs.close();
			ps.close();
			if (isNewConn) {
				conn.commit();
				conn.close();
			}
		} catch (SQLException e) {
			try {
				conn.rollback();
				conn.close();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			e.printStackTrace();
		}
		return players;
	}

	public GameStructure getGameStructure(long id, Connection conn) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public GameStructure saveGameStructure(GameStructure gs, Connection conn) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public GameStructure mergeGameStructure(GameStructure gs, Connection conn) {
		// TODO Auto-generated method stub
		return null;
	}

}
