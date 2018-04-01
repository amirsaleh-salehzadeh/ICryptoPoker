package hibernate.accounting.ledger.general;

import common.accounting.ledger.general.AccountENT;
import tools.AMSException;

public interface LiabilityDAOInterface {
	  
	  public AccountENT getCommission() throws AMSException  ;
	  public AccountENT getAccountPayable() throws AMSException  ;
	  

}
