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
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import tools.AMSException;

import com.mysql.jdbc.Statement;

import game.poker.holdem.domain.BlindLevel;
import game.poker.holdem.domain.Game;
import game.poker.holdem.domain.GameStructure;
import game.poker.holdem.domain.GameType;
import game.poker.holdem.domain.HandEntity;
import game.poker.holdem.domain.Player;
import game.poker.holdem.domain.PlayerHand;
import hibernate.config.BaseHibernateDAO;

public class GameDaoImpl extends BaseHibernateDAO implements GameDaoInterface {

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
			ResultSet rs = ps.executeQuery();
			HandDaoImpl handDaoImpl = new HandDaoImpl();
			PlayerDaoImpl player = new PlayerDaoImpl();
			if (rs.next()) {
				game.setPlayersRemaining(rs.getInt("players_left"));
				if (rs.getString("game_type").equalsIgnoreCase("T"))
					game.setGameType(GameType.TOURNAMENT);
				else
					game.setGameType(GameType.CASH);
				game.setName(rs.getString("name"));
				game.setStarted(rs.getBoolean("is_started"));
				if (rs.getLong("current_hand_id") != 0)
					game.setCurrentHand(handDaoImpl.findById(
							rs.getLong("current_hand_id"), conn));
				game.setGameStructure(getGameStructure(
						rs.getLong("game_structure_id"), conn));
				if (rs.getString("btn_player_id") != null)
					game.setPlayerInBTN(player.findById(
							rs.getString("btn_player_id"), conn));
				game.setPlayers(getAllPlayersInGame(id, conn));
			}
			game.setId(id);
			rs.close();
			ps.close();
			if (isNewConn) {
				conn.commit();
				conn.close();
			}
		} catch (SQLException e) {
			try {
				if (!conn.isClosed()) {
					conn.rollback();
					conn.close();
				}
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			e.printStackTrace();
		}
		return game;
	}

	public static void main(String[] args) {
		Game g = new Game();
		g.setGameType(GameType.TOURNAMENT);
		g.setName("naaameTest");
		GameStructure gs = new GameStructure();
		gs.setBlindLength(1);
		List<BlindLevel> blinds = new ArrayList<>();
		Collections.sort(blinds);
		gs.setBlindLevels(blinds);
		// gs.setCurrentBlindEndTime(currentBlindEndTime)
		gs.setCurrentBlindLevel(BlindLevel.BLIND_15_30);
		gs.setStartingChips(15);
		g.setGameStructure(gs);
		g.setCurrentHand(new HandEntity());
		g.setPlayerInBTN(new Player());
		g.setStarted(false);
		GameDaoImpl gdi = new GameDaoImpl();
		gdi.save(g, null);
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
			query = "INSERT INTO `game` (`players_left`, `game_type`, `name`, `is_started`) "
					+ "VALUES (?, ?, ?, ?);";
			PreparedStatement ps = conn.prepareStatement(query,
					Statement.RETURN_GENERATED_KEYS);
			ps.setInt(1, game.getPlayersRemaining());
			ps.setString(2, game.getGameType().name());
			ps.setString(3, game.getName());
			if (game.isStarted()) {
				ps.setInt(4, 1);
			} else
				ps.setInt(4, 0);
			// if(game.getCurrentHand())
			// ps.setLong(5, game.getCurrentHand().getId());
			// ps.setLong(6, game.getGameStructure().getId());
			// ps.setString(5, game.getPlayerInBTN().getId());
			ps.executeUpdate();
			ResultSet rs = ps.getGeneratedKeys();
			GameDaoImpl gdi = new GameDaoImpl();
			if (rs.next()) {
				game.setId(rs.getLong(1));
				game.setGameStructure(gdi.saveGameStructure(
						game.getGameStructure(), conn));
				query = "update `game` set game_structure_id = "
						+ game.getGameStructure().getId() + " where game_id = "
						+ rs.getLong(1);
				ps = conn.prepareStatement(query);
				ps.executeUpdate();
			}
			game.setPlayers(new HashSet<Player>());
			rs.close();
			ps.close();
			if (isNewConn) {
				conn.commit();
				conn.close();
			}
		} catch (SQLException e) {
			try {
				if (!conn.isClosed()) {
					conn.rollback();
					conn.close();
				}
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			e.printStackTrace();
		}
		return game;
	}

	@Override
	public Game merge(Game game, Connection conn) {
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
			query = "UPDATE `game` SET `players_left` = ?, `game_type` = ?, `name` =?, `is_started` = ?, "
					+ "`current_hand_id` = ?, `game_structure_id` =?, `btn_player_id` = ? WHERE `game_id` = ? ";
			// + "VALUES (?, ?, ?, ?, ?, ?, ?);";
			PreparedStatement ps = conn.prepareStatement(query);
			ps.setInt(1, game.getPlayers().size());
			ps.setString(2, game.getGameType().name());
			ps.setString(3, game.getName());
			if (game.isStarted()) {
				ps.setInt(4, 1);
			} else
				ps.setInt(4, 0);
			if (game.getCurrentHand() == null)
				ps.setString(5, null);
			else
				ps.setLong(5, game.getCurrentHand().getId());
			ps.setLong(6, game.getGameStructure().getId());
			ps.setString(7, game.getPlayerInBTN().getId());
			ps.setLong(8, game.getId());
			ps.executeUpdate();
			ps.close();
			if (isNewConn) {
				conn.commit();
				conn.close();
			}
		} catch (SQLException e) {
			try {
				if (!conn.isClosed()) {
					conn.rollback();
					conn.close();
				}
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
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
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				Player p = new Player();
				p.setId(rs.getString("username"));
				p.setName(rs.getString("name"));
				p.setPassword(rs.getString("password"));
				p.setRegistrationDate(rs.getString("registeration_date"));
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
				if (!conn.isClosed()) {
					conn.rollback();
					conn.close();
				}
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			e.printStackTrace();
		}
		return players;
	}

	public GameStructure getGameStructure(long id, Connection conn) {
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
			query = "select * from game_structure where game_structure_id = "
					+ id;
			PreparedStatement ps = conn.prepareStatement(query);
			ps.execute();
			ResultSet rs = ps.getResultSet();
			GameStructure g = new GameStructure();
			if (rs.next()) {
				g.setId(rs.getLong("game_structure_id"));
				g.setBlindLength(rs.getInt("blind_length"));
				BlindLevel temp = (BlindLevel) BlindLevel.class.getField(
						rs.getString("current_blind_level")).get(null);
				g.setCurrentBlindLevel(temp);
				g.setCurrentBlindEndTime(rs.getDate("current_blind_ends"));
				g.setPuaseStartTime(rs.getDate("pause_start_time"));
				g.setStartingChips(rs.getInt("starting_chips"));
			}
			rs.close();
			ps.close();
			if (isNewConn) {
				conn.commit();
				conn.close();
			}
			return g;
		} catch (SQLException | IllegalArgumentException
				| IllegalAccessException | NoSuchFieldException
				| SecurityException e) {
			try {
				if (!conn.isClosed()) {
					conn.rollback();
					conn.close();
				}
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			e.printStackTrace();
		}
		return null;

	}

	@Override
	public GameStructure saveGameStructure(GameStructure gs, Connection conn) {
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
			query = "INSERT INTO `game_structure` (`current_blind_level`, `blind_length`, `current_blind_ends`, `pause_start_time`, `starting_chips`) "
					+ "VALUES (?, ?, ?, ?, ?);";
			PreparedStatement ps = conn.prepareStatement(query,
					Statement.RETURN_GENERATED_KEYS);
			ps.setString(1, gs.getCurrentBlindLevel().toString());
			ps.setInt(2, gs.getBlindLength());
			// ps.setDate(3, new
			// java.sql.Date(gs.getCurrentBlindEndTime().getTime()));
			ps.setDate(3, null);
			// ps.setDate(4, new
			// java.sql.Date(gs.getPuaseStartTime().getTime()));
			ps.setDate(4, null);
			ps.setInt(5, gs.getStartingChips());
			ps.executeUpdate();
			ResultSet rs = ps.getGeneratedKeys();
			if (rs.next()) {
				gs.setId(rs.getLong(1));
				ps.close();
				query = "INSERT INTO `game_blind` (`game_structure_id`, `blind`)"
						+ "VALUES (?, ?);";
				ps = conn.prepareStatement(query);
				ps.setLong(1, gs.getId());
				ps.setString(2, gs.getCurrentBlindLevel().name());
			}
			rs.close();
			ps.close();
			if (isNewConn) {
				conn.commit();
				conn.close();
			}
		} catch (SQLException e) {
			try {
				if (!conn.isClosed()) {
					conn.rollback();
					conn.close();
				}
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			e.printStackTrace();
		}
		return gs;

	}

	@Override
	public GameStructure mergeGameStructure(GameStructure gs, Connection conn) {
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
			query = "UPDATE `game_structure`  SET `current_blind_level` = ?, `blind_level` = ?, `current_blind_ends` =?, `pause_start_time` = ?, "
					+ "`starting chips` = ?" + "VALUES (?, ?, ?, ?, ?);";

			PreparedStatement ps = conn.prepareStatement(query,
					Statement.RETURN_GENERATED_KEYS);

			ps.setString(1, gs.getCurrentBlindLevel().toString());
			ps.setInt(2, gs.getBlindLength());
			ps.setString(3, gs.getCurrentBlindEndTime().toString());
			ps.setString(4, gs.getPuaseStartTime().toString());
			ps.setInt(5, gs.getStartingChips());

			ps.executeUpdate();
			ResultSet rs = ps.getGeneratedKeys();
			if (rs.next()) {
				gs.setId(rs.getLong(1));
			}
			rs.close();
			ps.close();
			if (isNewConn) {
				conn.commit();
				conn.close();
			}
		} catch (SQLException e) {
			try {
				if (!conn.isClosed()) {
					conn.rollback();
					conn.close();
				}
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			e.printStackTrace();
		}
		return gs;
	}

	@Override
	public List<Game> getAllGames(Connection conn) {
		List<Game> games = new ArrayList<>();
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
			query = "select * from game ";
			PreparedStatement ps = conn.prepareStatement(query);
			ps.execute();
			ResultSet rs = ps.getResultSet();

			while (rs.next()) {
				Game game = new Game();
				HandDaoImpl handDaoImpl = new HandDaoImpl();
				PlayerDaoImpl player = new PlayerDaoImpl();
				game.setId(rs.getLong("game_id"));
				game.setName(rs.getString("players_left"));
				if (rs.getString("game_type").equalsIgnoreCase("T"))
					game.setGameType(GameType.TOURNAMENT);
				else
					game.setGameType(GameType.CASH);
				game.setName(rs.getString("name"));
				game.setStarted(rs.getBoolean("is_started"));
				game.setCurrentHand(handDaoImpl.findById(
						rs.getLong("current_hand_id"), conn));
				game.setGameStructure(getGameStructure(
						rs.getLong("game_structure_id"), conn));
				if (rs.getString("btn_player_id") != null)
					game.setPlayerInBTN(player.findById(
							rs.getString("btn_player_id"), conn));
				game.setPlayers(getAllPlayersInGame(game.getId(), conn));
				games.add(game);

			}
			rs.close();
			ps.close();
			if (isNewConn) {
				conn.commit();
				conn.close();
			}
		} catch (SQLException e) {
			try {
				if (!conn.isClosed()) {
					conn.rollback();
					conn.close();
				}
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			e.printStackTrace();
		}
		return games;
	}

	// @Override
	// public Game addOnePlayerToGame(Game game, Connection conn) {
	// // TODO Auto-generated method stub
	// GameDaoImpl dao = new GameDaoImpl();
	// Game gameTMP = dao.findById(game.getId(), conn);
	// gameTMP.setPlayersRemaining(game.getPlayersRemaining());
	// return dao.merge(gameTMP, conn);
	// try {
	// boolean isNewConn = false;
	// if (conn == null)
	// try {
	// conn = getConnection();
	// conn.setAutoCommit(false);
	// isNewConn = true;
	// } catch (AMSException e) {
	// e.printStackTrace();
	// }
	//
	// String query = "";
	// query = "UPDATE `game`  SET `players_left` = ?," + "VALUES (?);";
	//
	// PreparedStatement ps = conn.prepareStatement(query,
	// Statement.RETURN_GENERATED_KEYS);
	// game.setPlayersRemaining(game.getPlayersRemaining()+1) ;
	// ps.setInt(1, game.getPlayersRemaining());
	//

	// ps.executeUpdate();
	// ResultSet rs = ps.getGeneratedKeys();
	// if (rs.next()) {
	// game.setId(rs.getLong(1));
	// }
	// rs.close();
	// ps.close();
	// if (isNewConn) {
	// conn.commit();
	// conn.close();
	// }
	// } catch (SQLException e) {
	// e.printStackTrace();
	// }
	// return game ;
	// }
	//
	// @Override
	// public Game updatePlayerLeft(Game game, Connection conn) {
	// GameDaoImpl dao = new GameDaoImpl();
	// Game gameTMP = dao.findById(game.getId(), conn);
	// gameTMP.setPlayersRemaining(game.getPlayersRemaining());
	// return dao.merge(gameTMP, conn);
	// try {
	// boolean isNewConn = false;
	// if (conn == null)
	// try {
	// conn = getConnection();
	// conn.setAutoCommit(false);
	// isNewConn = true;
	// } catch (AMSException e) {
	// e.printStackTrace();
	// }
	//
	// String query = "";
	// query = "UPDATE `game`  SET `players_left` = ?," + "VALUES (?);";
	//
	// PreparedStatement ps = conn.prepareStatement(query,
	// Statement.RETURN_GENERATED_KEYS);
	// game.setPlayersRemaining(game.getPlayersRemaining()-1) ;
	// ps.setInt(1, game.getPlayersRemaining());
	//
	//
	// ps.executeUpdate();
	// ResultSet rs = ps.getGeneratedKeys();
	// if (rs.next()) {
	// game.setId(rs.getLong(1));
	// }
	// rs.close();
	// ps.close();
	// if (isNewConn) {
	// conn.commit();
	// }
	// } catch (SQLException e) {
	// e.printStackTrace();
	// }
	// return game ;

	// }

}
