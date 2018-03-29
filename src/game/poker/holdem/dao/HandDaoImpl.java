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

import common.game.poker.holdem.GameENT;
import tools.AMSException;
import game.poker.holdem.Card;
import game.poker.holdem.Deck;
import game.poker.holdem.domain.BlindLevel;
import game.poker.holdem.domain.BoardEntity;
import game.poker.holdem.domain.HandEntity;
import game.poker.holdem.domain.Player;
import game.poker.holdem.domain.PlayerHand;
import hibernate.config.BaseHibernateDAO;

public class HandDaoImpl extends BaseHibernateDAO implements HandDaoInterface {

	@Override
	public HandEntity save(HandEntity hand, Connection conn) {
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
			String query = "INSERT INTO `board` (`board_id`, `flop1`, `flop2`, `flop3`, `turn`, `river`) "
					+ "VALUES (NULL, NULL, NULL, NULL, NULL, NULL);";
			PreparedStatement ps = conn.prepareStatement(query,
					Statement.RETURN_GENERATED_KEYS);
			ps.executeUpdate();
			ResultSet rs = ps.getGeneratedKeys();
			if (rs.next()) {
				BoardEntity board = new BoardEntity();
				board.setId(rs.getLong(1));
				hand.setBoard(board);
			}
			ps.close();
			rs.close();
			query = "INSERT INTO `hand` (`board_id`, `game_id`, `player_to_act_id`, `blind_level`, `pot`,"
					+ " `bet_amount`, `total_bet_amount`) "
					+ "VALUES (?, ?, ?, ?, ?, ?, ?);";
			ps = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
			ps.setLong(1, hand.getBoard().getId());
			ps.setLong(2, hand.getGame().getId());
			ps.setString(3, hand.getCurrentToAct().getId());
			ps.setString(4, hand.getBlindLevel().toString());
			ps.setInt(5, hand.getPot());
			ps.setInt(6, hand.getLastBetAmount());
			ps.setInt(7, hand.getTotalBetAmount());
			ps.executeUpdate();
			rs = ps.getGeneratedKeys();
			Set<PlayerHand> newPHs = new HashSet<PlayerHand>();
			if (rs.next()) {
				hand.setId(rs.getLong(1));
				query = "INSERT INTO `player_hand` (`player_hand_id`, `player_id`, `hand_id`, `card1`, `card2`, `bet_amount`, `round_bet_amount`, action_status) "
						+ "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
				for (PlayerHand ph : hand.getPlayers()) {
					PreparedStatement ps2 = conn.prepareStatement(query,
							Statement.RETURN_GENERATED_KEYS);
					ps2.setLong(1, ph.getId());
					ps2.setString(2, ph.getPlayer().getId());
					ps2.setLong(3, hand.getId());
					ps2.setString(4, ph.getCard1().name());
					ps2.setString(5, ph.getCard2().name());
					ps2.setInt(6, ph.getBetAmount());
					ps2.setInt(7, ph.getRoundBetAmount());
					ps2.setInt(8, ph.getStatus());
					ps2.executeUpdate();
					ResultSet rs2 = ps.getGeneratedKeys();
					if (rs2.next())
						ph.setId(rs.getLong(1));
					newPHs.add(ph);
					ps2.close();
					rs2.close();
				}
			}
			hand.setPlayers(newPHs);
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
		return hand;

	}

	@Override
	public HandEntity merge(HandEntity hand, Connection conn) {
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
			query = "UPDATE `hand` SET `board_id` = ?, `game_id` = ?, `player_to_act_id` = ?, `blind_level` = ?, `pot` = ?,"
					+ " `bet_amount` = ?, `total_bet_amount` = ? where hand_id = ?";
			PreparedStatement ps = conn.prepareStatement(query);

			ps.setLong(1, hand.getBoard().getId());
			ps.setLong(2, hand.getGame().getId());
			if (hand.getCurrentToAct() == null)
				ps.setString(3, null);
			else
				ps.setString(3, hand.getCurrentToAct().getId());
			ps.setString(4, hand.getBlindLevel().toString());
			ps.setInt(5, hand.getPot());
			ps.setInt(6, hand.getLastBetAmount());
			ps.setInt(7, hand.getTotalBetAmount());
			ps.setLong(8, hand.getId());
			updateBoard(hand.getBoard(), conn);
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
		return hand;
	}

	private void updateBoard(BoardEntity board, Connection conn) {
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
			query = "UPDATE `board` SET `flop1` = ?, `flop2` = ?, `flop3` = ?, `turn` = ?, `river` = ? WHERE `board_id` = ?";
			PreparedStatement ps = conn.prepareStatement(query);
			if (board.getFlop1() == null)
				ps.setString(1, null);
			else
				ps.setString(1, board.getFlop1().name());
			if (board.getFlop2() == null)
				ps.setString(2, null);
			else
				ps.setString(2, board.getFlop2().name());
			if (board.getFlop3() == null)
				ps.setString(3, null);
			else
				ps.setString(3, board.getFlop3().name());
			if (board.getTurn() == null)
				ps.setString(4, null);
			else
				ps.setString(4, board.getTurn().name());
			if (board.getRiver() == null)
				ps.setString(5, null);
			else
				ps.setString(5, board.getRiver().name());
			ps.setLong(6, board.getId());
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
	}

	@Override
	public HandEntity findById(long id, Connection conn) {
		HandEntity hand = new HandEntity();
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
			query = "select * from hand where hand_id = " + id;
			PreparedStatement ps = conn.prepareStatement(query);
			ps.execute();
			ResultSet rs = ps.getResultSet();
			if (rs.next()) {
				hand.setBlindLevel(BlindLevel.valueOf(rs
						.getString("blind_level")));
				hand.setBoard(getBoard(rs.getLong("board_id"), conn));
				PlayerDaoImpl pdao = new PlayerDaoImpl();
				if (rs.getString("player_to_act_id") != null)
					hand.setCurrentToAct(pdao.findById(
							rs.getString("player_to_act_id"), conn));
				Set<PlayerHand> phs = getAllPlayerHands(id, conn);
				hand.setPlayers(phs);
				hand.setId(id);
				hand.setLastBetAmount(rs.getInt("bet_amount"));
				hand.setPot(rs.getInt("pot"));
				hand.setTotalBetAmount(rs.getInt("total_bet_amount"));
				Deck d = new Deck(true);
				hand.setCards(d.getRemainingCardsInDeck(phs, hand.getBoard()));
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
		return hand;
	}

	private BoardEntity getBoard(long id, Connection conn) {
		BoardEntity board = new BoardEntity();
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
			query = "select * from board where board_id = " + id;
			PreparedStatement ps = conn.prepareStatement(query);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				board.setId(id);
				if (rs.getString("flop1") == null)
					continue;
				board.setFlop1(Card.valueOf(rs.getString("flop1")));
				board.setFlop2(Card.valueOf(rs.getString("flop2")));
				board.setFlop3(Card.valueOf(rs.getString("flop3")));
				if (rs.getString("turn") == null)
					continue;
				board.setTurn(Card.valueOf(rs.getString("turn")));
				if (rs.getString("river") == null)
					continue;
				board.setRiver(Card.valueOf(rs.getString("river")));

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
		return board;
	}

	@Override
	public Set<PlayerHand> getAllPlayerHands(long handId, Connection conn) {
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
			query = "select * from player_hand ph left join player p on p.username = ph.player_id where hand_id = "
					+ handId + " order by p.game_position asc";
			PreparedStatement ps = conn.prepareStatement(query);
			ps.execute();
			ResultSet rs = ps.getResultSet();
			while (rs.next()) {
				PlayerHand p = new PlayerHand();
				p.setId(rs.getLong("player_hand_id"));
				p.setPlayer(new PlayerDaoImpl().findById(
						(rs.getString("player_id")), null));
				p.setHandId(rs.getLong("hand_id"));
				p.setCard1((Card) Card.class.getField(rs.getString("card1"))
						.get(null));
				p.setCard2((Card) Card.class.getField(rs.getString("card2"))
						.get(null));
				p.setBetAmount(rs.getInt("bet_amount"));
				p.setRoundBetAmount(rs.getInt("round_bet_amount"));
				p.setStatus(rs.getInt("action_status"));
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

	@Override
	public PlayerHand updatePlayerHand(PlayerHand ph, Connection conn) {
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
			String query = "update `player_hand` set `bet_amount` = ?, `round_bet_amount` = ?, action_status = ? where `player_hand_id` = ?";
			PreparedStatement ps2 = conn.prepareStatement(query);
			ps2.setInt(1, ph.getBetAmount());
			ps2.setInt(2, ph.getRoundBetAmount());
			ps2.setInt(3, ph.getStatus());
			ps2.setLong(4, ph.getId());
			ps2.executeUpdate();
			ps2.close();
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
		return ph;

	}

}
