package hibernate.user;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.tomcat.util.security.MD5Encoder;

import com.mysql.jdbc.Statement;

import common.DropDownENT;
import common.security.RoleENT;
import common.user.UserENT;
import common.user.UserLST;
import common.user.UserPassword;
import hibernate.config.BaseHibernateDAO;
import tools.AMSException;
import tools.AMSUtililies;
import tools.MD5Encryptor;

public class UserDAO extends BaseHibernateDAO implements UserDAOInterface {
	public static void main(String[] args) {
		UserDAO udao = new UserDAO();
		// try {
		// for (int i = 14; i < 20; i++) {
		// TitleENT roles = new TitleENT();
		// roles = udao.getTitle(2);
		// EthnicENT eth = new EthnicENT();
		// eth = udao.getEthnic(2);
		// UserENT ent = new UserENT();
		// ent.setActive(true);
		// ent.setClientID(1);
		// ent.setDateOfBirth("dob");
		// ent.setGender(true);
		// ent.setEthnic(eth);
		// ent.setRegisterationDate("today");
		// ent.setSurName("surnamezzz");
		// ent.setName("namezzzz"+i);
		// ent.setTitle(roles);
		// ent.setUserName("userNamezzzz4");
		// UserPassword up = new UserPassword();
		// up.setUserPassword("passsssss"+i);
		// ent.setPassword("pass"+i);
		// ent = udao.saveUpdateUser(ent);

		UserLST l = new UserLST();
		// UserENT us = new UserENT();
		// ArrayList<RoleENT> ad = new ArrayList<RoleENT>();
		// UserENT u = new UserENT();
		// u.setRoleENTs(ad);

		try {
			// udao.registerNewUser(new UserPassword("amir", "1234"));
			UserENT user = udao.getUserLST(l).getUserENTs().get(1);
			user.setName("tester");
			user.setSurName("maninjwa");
			udao.updateUserProfile(user);
			udao.getUserLST(new UserLST());

		} catch (AMSException e) {
			e.printStackTrace();
		}
		// UDAO.DELETEUSER(US);

		System.out.println("done");
	}

	public UserPassword registerNewUser(UserPassword ent) throws AMSException {
		try {
			Connection conn = null;
			try {
				conn = getConnection();
				conn.setAutoCommit(false);
			} catch (AMSException e) {
				e.printStackTrace();
			}

			String query = "";
			query = "select * from users where username = ?";
			PreparedStatement ps = conn.prepareStatement(query);
			ps.setString(1, ent.getUserName());
			ResultSet rs = ps.executeQuery();
			if (rs.next())
				throw new AMSException("The username already exist");
			rs.close();
			ps.clearBatch();
			query = "insert into users (password,username)"
					+ " values (?,?)";
			ps = conn.prepareStatement(query);
			ps.setString(2, ent.getUserName());
			ps.setString(1, MD5Encryptor.encode(ent.getUserPassword()));
			ps.execute();
			ps.close();
			conn.commit();
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
			throw getAMSException("", e);
		}
		return ent;
	}

	public UserLST getUserLST(UserLST lst) throws AMSException {
		ArrayList<UserENT> userENTs = new ArrayList<UserENT>();
		try {
			Connection conn = null;
			try {
				conn = getConnection();
			} catch (AMSException e) {
				e.printStackTrace();
			}
			String query = "";
			query = "select * from player " ;
			PreparedStatement ps = conn.prepareStatement(query);
			ps.execute();
			ResultSet rs = ps.getResultSet();
			while (rs.next()) {
				UserENT userENT = new UserENT(rs.getString("username"));
				userENT.setName(rs.getString("name"));
				userENT.setSurName(rs.getString("surname"));
				userENT.setDateOfBirth(rs.getString("dob"));
				userENT.setGender(rs.getBoolean("gender"));
				userENT.setRegisterationDate(rs.getString("registeration_date"));
				userENT.setPassword(rs.getString("password"));
				userENTs.add(userENT);
			}
			rs.close();
			ps.close();
			conn.close();
			lst.setUserENTs(userENTs);
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return lst;
	}

	public boolean deleteUser(UserENT user) throws AMSException {
		try {
			Connection conn = null;
			try {
				conn = getConnection();
			} catch (AMSException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			String query = "delete from users where username = ?";
			PreparedStatement ps = conn.prepareStatement(query);
			ps.setString(1, user.getUserName());
			ps.execute();
			ps.close();
			conn.close();
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
			throw getAMSException("", e);
		}
	}


	public UserENT getUserENT(UserENT user) throws AMSException {
		UserENT userENT = new UserENT();
		try {
			Connection conn = null;
			try {
				conn = getConnection();
			} catch (AMSException e) {
				e.printStackTrace();
			}
			String query = "";
			query = "select * from player where username = ?";
			PreparedStatement ps = conn.prepareStatement(query);
			ps.setString(1, user.getUserName());
			ps.execute();
			ResultSet rs = ps.getResultSet();
			while (rs.next()) {
				userENT.setUserName(rs.getString("username"));
				userENT.setName(rs.getString("name"));
				userENT.setSurName(rs.getString("surname"));
				userENT.setDateOfBirth(rs.getString("date_of_birth"));
				userENT.setRegisterationDate(rs.getString("registeration_date"));
				userENT.setPassword(rs.getString("password"));
				if (rs.getInt("gender") == 1) {
					userENT.setGender(true);
				} else
					userENT.setGender(false);

				// Please set all attributes for a users here...
			}
			rs.close();
			ps.close();
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return userENT;
	}

	public ArrayList<RoleENT> getAllRolesUser(String username) {
		ArrayList<RoleENT> res = new ArrayList<RoleENT>();
		try {
			Connection conn = null;
			try {
				conn = getConnection();
			} catch (AMSException e) {
				e.printStackTrace();
			}
			String query = "Select * from player_roles where username = ?";
			PreparedStatement ps = conn.prepareStatement(query);
			ps.setString(1, username);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				RoleENT r = new RoleENT(rs.getString("role_name"));
				res.add(r);
			}
			rs.close();
			ps.close();
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return res;
	}

	public void saveUpdateUserRoles(UserENT user) throws AMSException {
		try {
			Connection conn = null;
			try {
				conn = getConnection();
				conn.setAutoCommit(false);
			} catch (AMSException e) {
				e.printStackTrace();
			}
			String query = "delete from player_roles where username = ?";
			PreparedStatement ps = conn.prepareStatement(query);
			ps.setString(1, user.getUserName());
			ps.execute();
			query = "insert into player_roles (username, role_name) values (?,?)";
			for (int i = 0; i < user.getRoleENTs().size(); i++) {
				ps = conn.prepareStatement(query);
				ps.setString(1, user.getUserName());
				ps.setString(2, user.getRoleENTs().get(i).getRoleName());
				ps.execute();
			}
			ps.close();
			conn.commit();
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
			throw getAMSException("", e);
		}
	}

	public boolean deleteUsers(ArrayList<UserENT> users) throws AMSException {
		for (int i = 0; i < users.size(); i++) {
			deleteUser(users.get(i));
		}
		return true;
	}

	public UserENT updateUserProfile(UserENT user) throws AMSException {
		String query = "";
		PreparedStatement ps = null;
		try {
			Connection conn = null;
			try {
				conn = getConnection();
			} catch (AMSException e) {
				e.printStackTrace();
			}
			query = "Insert into player ( date_of_birth, gender, name, surname, username, password)" +
					" values (?,?,?,?,?,?) on duplicate key " +
					"UPDATE date_of_birth = ? , gender = ?"
					+ ", name = ?, registeration_date = ?, surname = ? ";
			ps = conn.prepareStatement(query);
			ps.setString(1, user.getDateOfBirth());
			if (user.isGender()) {
				ps.setInt(2, 1);
			} else
				ps.setInt(2, 0);
			ps.setString(3, user.getName());
			ps.setString(4, user.getSurName());
			ps.setString(5, user.getUserName());
			ps.setString(6, MD5Encryptor.encode(user.getPassword()));
			ps.setString(7, user.getDateOfBirth());
			ps.setInt(8, 1);
			if (user.isGender()) {
				ps.setInt(15, 1);
			} else
				ps.setInt(13, 0);

			ps.setString(9, user.getName());
			ps.setString(10, user.getRegisterationDate());
			ps.setString(11, user.getSurName());
			ps.executeUpdate();
			ps.close();
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
			throw getAMSException("", e);
		}

		return user;

	}

}
