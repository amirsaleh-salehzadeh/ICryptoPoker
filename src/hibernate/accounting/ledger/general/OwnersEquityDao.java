package hibernate.accounting.ledger.general;

import common.accounting.ledger.general.AccountENT;
import hibernate.accounting.sale.SaleDao;
import hibernate.config.BaseHibernateDAO;
import tools.AMSException;

public class OwnersEquityDao extends BaseHibernateDAO implements OwnersEquityDaoInterface {

	@Override
	public AccountENT getSale() throws AMSException {
		// TODO Auto-generated method stub
		AccountENT sale = new AccountENT() ;
		SaleDao saledao = new SaleDao() ;
		sale.setCreditList(saledao.searchCriteria("sale")) ;
		sale.setAccountId("I001");
		sale.setName("Sales");
		return sale ;
	}

}
