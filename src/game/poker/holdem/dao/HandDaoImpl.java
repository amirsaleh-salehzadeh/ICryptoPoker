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

import com.mysql.jdbc.Statement;

import tools.AMSException;
import game.poker.holdem.Card;
import game.poker.holdem.domain.BlindLevel;
import game.poker.holdem.domain.Game;
import game.poker.holdem.domain.HandEntity;
import game.poker.holdem.domain.Player;
import game.poker.holdem.domain.PlayerHand;
import hibernate.config.BaseHibernateDAO;

public class HandDaoImpl extends BaseHibernateDAO implements HandDao {

	@Override
	public HandEntity save(HandEntity game, Connection conn) {
		// TODO Auto-generated method stub
		
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
			query = "INSERT INTO `hand` (`board_id`, `game_id`, `player_to_act_id`, `blind_level`, `pot`," +
					" `bet_amount`, `total_bet_amount`) "
					+ "VALUES (?, ?, ?, ?, ?, ?, ?);";
			PreparedStatement ps = conn.prepareStatement(query,
					Statement.RETURN_GENERATED_KEYS);
			
			ps.setLong(1, game.getBoard().getId());
			ps.setLong(2, game.getGame().getId());
			ps.setString(3, game.getCurrentToAct().getId());
		    ps.setString(4, game.getBlindLevel().toString()) ;
			ps.setInt(5, game.getPot());
			ps.setInt(6, game.getLastBetAmount());
			ps.setInt(7, game.getTotalBetAmount());
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
	public HandEntity merge(HandEntity game, Connection conn) {
		// TODO Auto-generated method stub
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
			query = "UPDATE `hand`  SET ``board_id` = ?, `game_id` =?, `player_to_act_id`  =?, `blind_level`  =?, `pot`  =?," +
					" `bet_amount` =?, `total_bet_amount' =?"
					+ "VALUES (?, ?, ?, ?, ?, ?, ?);" ;
				
			PreparedStatement ps = conn.prepareStatement(query,
					Statement.RETURN_GENERATED_KEYS);
			
			ps.setLong(1, game.getBoard().getId());
			ps.setLong(2, game.getGame().getId());
			ps.setString(3, game.getCurrentToAct().getId());
		    ps.setString(4, game.getBlindLevel().toString()) ;
			ps.setInt(5, game.getPot());
			ps.setInt(6, game.getLastBetAmount());
			ps.setInt(7, game.getTotalBetAmount());
			
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
	public HandEntity findById(long id, Connection conn) {
		HandEntity hand = new HandEntity();
		try {
			try {
				conn = getConnection();
			} catch (AMSException e) {
				e.printStackTrace();
			}
			String query = "";
			query = "select * from hand where hand_id = " + id;
			PreparedStatement ps = conn.prepareStatement(query);
			ps.execute();
			ResultSet rs = ps.getResultSet();
			while (rs.next()) {
				// hand.setBlindLevel(blindLevel)Name(rs.getString("players_left"));
				// hand.setGameType(rs.getString("game_type"));
				// hand.setName(rs.getString("name"));
				// hand.setStarted(rs.getBoolean("is_started"));
				// hand.setCurrentHand(rs.getString("current_hand_id"));
				// hand.setGameStructure(rs.getString("game_structure_id"));
				// hand.setPlayerInBTN(rs.getString("btn_player_id"));
			}
			rs.close();
			ps.close();
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return hand;
	}

	@Override
	public Set<PlayerHand> getAllPlayerHands(long handId, Connection conn) {
		// TODO Auto-generated method stub
		  
		Set<PlayerHand> players = new HashSet<PlayerHand>();
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
			query = "select * from player_hand where hand_id = " + handId;
			PreparedStatement ps = conn.prepareStatement(query);
			ps.execute();
			ResultSet rs = ps.getResultSet();
			while (rs.next()) {
				PlayerHand p = new PlayerHand() ;
				p.setId(rs.getLong("player_hand_id"));
				p.setPlayer(new PlayerDaoImpl().findById((rs.getString("player_id")),null));
				p.setHandEntity(findById((rs.getLong("password")),null));
				p.setCard1((Card) Card.class.getField(rs.getString("card1")).get(null)) ;
				p.setCard2((Card) Card.class.getField(rs.getString("card2")).get(null)) ;
				p.setBetAmount(rs.getInt("bet_amount"));
				p.setRoundBetAmount(rs.getInt("round_bet_amount"));
				players.add(p) ;
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
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchFieldException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return players;
	}

}
