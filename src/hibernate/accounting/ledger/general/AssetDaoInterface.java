package hibernate.accounting.ledger.general;

import common.accounting.ledger.general.AccountENT;
import tools.AMSException;

public interface AssetDaoInterface {
	
	public AccountENT getBank() throws AMSException  ;
	
          
}
