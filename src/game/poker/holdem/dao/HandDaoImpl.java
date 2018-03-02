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
import game.poker.holdem.domain.Game;
import game.poker.holdem.domain.HandEntity;
import hibernate.config.BaseHibernateDAO;

public class HandDaoImpl extends BaseHibernateDAO implements HandDao{

	@Override
	public HandEntity save(HandEntity game) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public HandEntity merge(HandEntity game) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public HandEntity findById(long id) {
		HandEntity hand = new HandEntity();
		try {
			Connection conn = null;
			try {
				conn = getConnection();
			} catch (AMSException e) {
				e.printStackTrace();
			}
			String query = "";
			query = "select * from hand where hand_id = " + id ;
			PreparedStatement ps = conn.prepareStatement(query);
			ps.execute();
			ResultSet rs = ps.getResultSet();
			while (rs.next()) {
				hand.setBlindLevel(blindLevel)Name(rs.getString("players_left"));
				hand.setGameType(rs.getString("game_type"));
				hand.setName(rs.getString("name"));
				hand.setStarted(rs.getBoolean("is_started"));
				hand.setCurrentHand(rs.getString("current_hand_id"));
				hand.setGameStructure(rs.getString("game_structure_id"));
				hand.setPlayerInBTN(rs.getString("btn_player_id"));
			}
			rs.close();
			ps.close();
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return game;
	}

}
