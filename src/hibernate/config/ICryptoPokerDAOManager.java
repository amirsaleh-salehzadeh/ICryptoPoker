package hibernate.config;

import hibernate.accounting.payment.PaymentDao;
import hibernate.accounting.payment.PaymentDaoInterface;
import hibernate.security.SecurityDAO;
import hibernate.security.SecurityDAOInterface;
import hibernate.user.UserDAO;
import hibernate.user.UserDAOInterface;

public class ICryptoPokerDAOManager {
	
	static UserDAOInterface _userDAOInterface ;
	static SecurityDAOInterface _securityDAOInterface ;
	static PaymentDaoInterface _paymentDAOInterface ;
	
	public static UserDAOInterface getUserDAOInterface(){
		if (_userDAOInterface == null) {
			_userDAOInterface = new UserDAO();
		}
		return _userDAOInterface; 
	}
	
	public static SecurityDAOInterface getSecuirtyDAOInterface(){
		if (_securityDAOInterface == null) {
			_securityDAOInterface = new SecurityDAO();
		}
		return _securityDAOInterface; 
	}
	
	public static PaymentDaoInterface getPaymentDAOInterface(){
		if (_paymentDAOInterface == null) {
			_paymentDAOInterface = new PaymentDao();
		}
		return _paymentDAOInterface; 
	}
	
	
	
	
}