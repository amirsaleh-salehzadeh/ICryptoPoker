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
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import com.mysql.jdbc.Statement;

import tools.AMSException;
import tools.MD5Encryptor;

import game.poker.holdem.domain.Game;
import game.poker.holdem.domain.HandEntity;
import game.poker.holdem.domain.Player;
import hibernate.config.BaseHibernateDAO;

public class PlayerDaoImpl extends BaseHibernateDAO implements
		PlayerDaoInterface {

	@Override
	public Player save(Player game, Connection conn) {
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
			query = "INSERT INTO `player` (`username`, `game_id`,`chips`, `game_position`, `finished_place`,"
					+ " `sitting_out`, `password`, 'registeration_date','name') "
					+ "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?);";//'gender','dob','surname',
			PreparedStatement ps = conn.prepareStatement(query,
					Statement.RETURN_GENERATED_KEYS);
			ps.setString(1, game.getId());
			ps.setLong(2, game.getGameId());
			ps.setInt(3, game.getChips());
			ps.setInt(4, game.getGamePosition());
			ps.setLong(5, game.getFinishPosition());
			if (game.isSittingOut()) {
				ps.setInt(6, 1);
			} else
				ps.setInt(6, 0);
			ps.setString(7, MD5Encryptor.encode(game.getPassword()));
//			ps.setInt(8, game.getGender());
//			ps.setDate(9, game.getDob());
//			ps.setString(10, game.getSurname());
			ps.setString(11, game.getRegistrationDate());
			ps.setString(12, game.getName());
			ps.executeUpdate();
			ResultSet rs = ps.getGeneratedKeys();

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
	public Player merge(Player player, Connection conn) {
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
			query = "UPDATE `player`  SET `game_id` = ?,`chips` = ?, `game_position` = ?, `finished_place`= ?, "
					+ " `sitting_out` = ?, name= ?, total_chips = ? where username = ?";
			PreparedStatement ps = conn.prepareStatement(query);
			ps.setLong(1, player.getGameId());
			ps.setInt(2, player.getChips());
			ps.setInt(3, player.getGamePosition());
			ps.setLong(4, player.getFinishPosition());
			if (player.isSittingOut()) {
				ps.setInt(5, 1);
			} else
				ps.setInt(5, 0);
			ps.setString(6, player.getName());
			ps.setInt(7, player.getTotalChips());
			ps.setString(8, player.getId());
			ps.executeUpdate();
			ps.close();
			if (isNewConn) {
				conn.commit();
				conn.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
		return player;
	}

	@Override
	public Player findById(String playerId, Connection conn) {
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
			query = "select * from player where username =  ? ";
			PreparedStatement ps = conn.prepareStatement(query);
			ps.setString(1, playerId);
			ResultSet rs = ps.executeQuery();
			Player p = new Player();
			GameDaoImpl gdao = new GameDaoImpl();
			if (rs.next()) {
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
				p.setTotalChips(rs.getInt("total_chips"));
				p.setGameId(rs.getLong("game_id"));
//				p.setDob(rs.getDate("dob"));
//				p.setSurname(rs.getString("surname"));
//				p.setGender(rs.getInt("gender"));
			}
			rs.close();
			ps.close();
			if (isNewConn) {
				conn.commit();
				conn.close();
			}
			return p;
		} catch (SQLException e) {
			try {
				conn.rollback();
				conn.close();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			e.printStackTrace();
		}
		return null;
	}

}
