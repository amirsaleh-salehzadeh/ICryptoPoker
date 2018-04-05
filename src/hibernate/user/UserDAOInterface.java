package hibernate.user;

import java.util.ArrayList;

import common.DropDownENT;
import common.accounting.payment.PaymentLST;
import common.security.RoleENT;
import common.user.UserENT;
import common.user.UserPassword;
import tools.AMSException;


public interface UserDAOInterface {
	public UserPassword registerNewUser(UserPassword ent) throws AMSException;
	public PaymentLST getUserLST(PaymentLST lst) throws AMSException;
	public UserENT getUserENT(UserENT user) throws AMSException;
	public boolean deleteUser(UserENT user) throws AMSException;
	public ArrayList<RoleENT> getAllRolesUser(String username);
	public void saveUpdateUserRoles(UserENT user) throws AMSException;
	public UserENT updateUserProfile(UserENT user) throws AMSException;
	public boolean deleteUsers(ArrayList<UserENT> users) throws AMSException;
	//activate user
}
