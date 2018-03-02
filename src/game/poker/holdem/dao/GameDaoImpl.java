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

import tools.AMSException;

import com.mysql.jdbc.Statement;
import common.user.UserENT;

import game.poker.holdem.domain.Game;
import hibernate.config.BaseHibernateDAO;

public class GameDaoImpl extends BaseHibernateDAO implements GameDao {

	@Override
	public Game findById(long id) {
		Game game = new Game();
		try {
			Connection conn = null;
			try {
				conn = getConnection();
			} catch (AMSException e) {
				e.printStackTrace();
			}
			String query = "";
			query = "select * from game where game_id = " + id ;
			PreparedStatement ps = conn.prepareStatement(query);
			ps.execute();
			ResultSet rs = ps.getResultSet();
			while (rs.next()) {
				game.setName(rs.getString("players_left"));
				game.setGameType(rs.getString("game_type"));
				game.setName(rs.getString("name"));
				game.setStarted(rs.getBoolean("is_started"));
				game.setCurrentHand(rs.getString("current_hand_id"));
				game.setGameStructure(rs.getString("game_structure_id"));
				game.setPlayerInBTN(rs.getString("btn_player_id"));
			}
			rs.close();
			ps.close();
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return game;
	}

	@Override
	public Game save(Game game) {
		try {
			Connection conn = getConnection();
			conn = getConnection();
			conn.setAutoCommit(false);
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
	public Game merge(Game game) {
		try {
			Connection conn = getConnection();
			conn = getConnection();
			conn.setAutoCommit(false);
			String query = "";
			query = "UPDATE `game`  SET `players_left` = ?, `game_type` = ?, `name` =?, `is_started` = ?, " +
					"`current_hand_id` = ?, `game_structure_id` =?, `btn_player_id` =? WHERE `game_id` = ? "
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
		return null;
	}

}
