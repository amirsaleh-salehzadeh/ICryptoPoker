package hibernate.security;

import java.sql.Connection;
import java.util.ArrayList;

import common.security.RoleENT;
import common.security.RoleLST;
import common.user.UserPassword;
import tools.AMSException;

public interface SecurityDAOInterface {

	public RoleLST getRolesList(RoleLST roleLST) throws AMSException;

	public ArrayList<String> getAllRoleCategories(String filter);

	public RoleENT saveUpdateRole(RoleENT role, Connection conn)
			throws AMSException;

	public RoleENT getRole(RoleENT role) throws AMSException;

	public boolean deleteRoles(ArrayList<RoleENT> roles) throws AMSException;

	public ArrayList<RoleENT> getAllRolesForAGroup(int gid);

}
