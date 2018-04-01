package hibernate.accounting.ledger.general;

import common.accounting.ledger.general.AccountENT;
import hibernate.accounting.payment.PaymentDao;
import hibernate.accounting.sale.SaleDao;
import hibernate.config.BaseHibernateDAO;
import tools.AMSException;

public class AssetDao extends BaseHibernateDAO implements AssetDaoInterface {

	@Override
	public AccountENT getBank() throws AMSException {
		// TODO Auto-generated method stub
		AccountENT bank = new AccountENT() ;
		SaleDao saledao = new SaleDao() ;
		PaymentDao paymentdao = new PaymentDao() ;
		bank.setCreditList(saledao.searchCriteria(null)) ;
		bank.setDebitList(paymentdao.searchCriteria(null));
		bank.setAccountId("A001");
		bank.setName("Bank");
		return bank ;
		
	}

}
