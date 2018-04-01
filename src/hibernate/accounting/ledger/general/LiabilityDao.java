package hibernate.accounting.ledger.general;

import common.accounting.ledger.general.AccountENT;
import hibernate.accounting.payment.PaymentDao;
import hibernate.accounting.sale.SaleDao;
import hibernate.config.BaseHibernateDAO;
import tools.AMSException;

public class LiabilityDao extends BaseHibernateDAO implements LiabilityDAOInterface {

	@Override
	public AccountENT getCommission() throws AMSException {
		// TODO Auto-generated method stub
		AccountENT commission = new AccountENT() ;
		SaleDao saledao = new SaleDao() ;
		PaymentDao paymentdao = new PaymentDao() ;
		commission.setCreditList(saledao.searchCriteria("commission")) ;
		commission.setDebitList(paymentdao.searchCriteria("commission"));
		commission.setAccountId("L002");
		commission.setName("Commission");
		return commission ;
		
	}

	@Override
	public AccountENT getAccountPayable() throws AMSException {
		// TODO Auto-generated method stub
		AccountENT accountpayable = new AccountENT() ;
		SaleDao saledao = new SaleDao() ;
		PaymentDao paymentdao = new PaymentDao() ;
		accountpayable.setCreditList(saledao.searchCriteria("game")) ;
		accountpayable.setDebitList(paymentdao.searchCriteria("game"));
		accountpayable.setAccountId("L001");
		accountpayable.setName("Account Payable");
		return accountpayable ;
	}

}
