package hibernate.security;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import common.security.RoleENT;
import common.security.RoleLST;
import common.user.UserPassword;
import hibernate.config.BaseHibernateDAO;
import tools.AMSException;
import tools.AMSUtililies;
import tools.MD5Encryptor;

public class SecurityDAO extends BaseHibernateDAO implements
		SecurityDAOInterface {

	public static void main(String[] args) {
		SecurityDAO udao = new SecurityDAO();
		try {
			RoleENT r = udao.getRole(new RoleENT("SuperAdmin"));
			UserPassword test = new UserPassword();
			test.setUserName("testersss");
			test.setUserPassword("number");
//			udao.register(test);

			System.out.println(r.getComment());
		} catch (AMSException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println(e.getMessage());
		}
	}

	public RoleENT saveUpdateRole(RoleENT role, Connection conn)
			throws AMSException {
		boolean isnew = false;
		if (conn == null)
			try {
				conn = getConnection();
				isnew = true;
			} catch (AMSException e) {
				e.printStackTrace();
			}
		String query = "";
		query = "INSERT INTO roles (role_name, comment, category_role) "
				+ "VALUES(?, ?, ?) ON DUPLICATE KEY UPDATE  comment = ?, category_role = ? ";
		PreparedStatement ps;
		try {
			ps = conn.prepareStatement(query);
			ps.setString(1, role.getRoleName());
			ps.setString(2, role.getComment());
			ps.setString(3, role.getRoleCategory());
			ps.setString(4, role.getComment());
			ps.setString(5, role.getRoleCategory());
			ps.executeUpdate();
			ps.close();
			if (isnew)
				conn.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return role;
	}

	public RoleENT getRole(RoleENT role) throws AMSException {
		try {
			Connection conn = null;
			try {
				conn = getConnection();
			} catch (AMSException e) {
				e.printStackTrace();
			}
			String query = "Select * from roles r where ";
			query += "role_name = ? ";
			PreparedStatement ps = conn.prepareStatement(query);
			ps.setString(1, role.getRoleName());
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				role = new RoleENT(rs.getString("role_name"),
						rs.getString("category_role"), rs.getString("comment"));
			}
			rs.close();
			ps.close();
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return role;
	}

	public RoleLST getRolesList(RoleLST roleLST) throws AMSException {
		ArrayList<RoleENT> res = new ArrayList<RoleENT>();
		try {
			Connection conn = null;
			try {
				conn = getConnection();
			} catch (AMSException e) {
				e.printStackTrace();
			}
			String searchKey = roleLST.getRoleName();
			String query = "Select r.* from roles r where "
					+ "r.role_name like ? or r.category_role like ? ";
			query += " order by " + roleLST.getSortedByField();
			if (roleLST.isAscending())
				query += ", role_name Asc";
			else
				query += " Desc";
			query += " LIMIT ?, ? ";
			PreparedStatement ps = conn.prepareStatement(query);
			ps.setString(1, "%" + searchKey + "%");
			ps.setString(2, "%" + searchKey + "%");
			ps.setInt(3, roleLST.getFirst());
			ps.setInt(4, roleLST.getPageSize());
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				RoleENT r = new RoleENT(rs.getString("role_name"),
						rs.getString("category_role"), rs.getString("comment"));
				res.add(r);
			}
			rs.last();
			roleLST.setTotalItems(rs.getRow());
			rs.close();
			roleLST.setRoleENTs(res);
			ps.close();
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return roleLST;
	}


	public ArrayList<String> getAllRoleCategories(String filter) {
		ArrayList<String> res = new ArrayList<String>();
		try {
			Connection conn = null;
			try {
				conn = getConnection();
			} catch (AMSException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			String query = "Select distinct category_role from roles where category_role like '%"
					+ filter + "%'";
			PreparedStatement ps = conn.prepareStatement(query);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				res.add(rs.getString("category_role"));
			}
			rs.close();
			ps.close();
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return res;
	}


	public boolean deleteRoles(ArrayList<RoleENT> roles) throws AMSException {
		Connection con = null;
		Boolean happens = false;
		PreparedStatement ps = null;

		try {
			con = getConnection();
			for (RoleENT role : roles) {
				String query = " DELETE FROM roles where role_name = ?";
				ps = con.prepareStatement(query);
				ps.setString(1, role.getRoleName());
				happens = ps.execute();
				ps.close();
			}
			con.close();
		} catch (Exception e) {
			e.printStackTrace();
			throw getAMSException("", e);

		}

		return happens;

	}

	public ArrayList<RoleENT> getAllRolesForAGroup(int gid) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		ArrayList<RoleENT> res = new ArrayList<RoleENT>();
		try {
			// inner join query to get all the roles
			con = getConnection();
			String query = "SELECT g.* FROM group_roles g "
					+ "left join roles r on g.role_name = r.role_name"
					+ " where g.group_id = ? ";
			ps = con.prepareStatement(query);
			ps.setInt(1, gid);
			rs = ps.executeQuery();

			while (rs.next()) {
				res.add(new RoleENT(rs.getString("role_name")));

			}
			ps.close();
			rs.close();
			con.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return res;
	}


	public boolean changePassword(UserPassword ent) throws AMSException {
		// TODO Auto-generated method stub
		return false;
	}

	public static boolean isUserAuthorised(String username, String role)
			throws AMSException {
		boolean isnew = false;
		boolean res = false;
		try {
			Class.forName(DBDRIVER);
			Connection conn = DriverManager.getConnection(DBADDRESS, USERNAME,
					PASSWORD);
			isnew = true;
			String query = "SELECT * FROM player_roles where username = ? and role_name = ?";
			PreparedStatement ps = conn.prepareStatement(query);
			ps.setString(1, username);
			ps.setString(2, role);
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				res = true;
			}
			rs.close();
			ps.close();
			if (isnew)
				conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return res;
	}

}
