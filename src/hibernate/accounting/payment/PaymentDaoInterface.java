package hibernate.accounting.payment;
import tools.AMSException;

import java.util.ArrayList;
import java.util.List;


import common.accounting.payment.PaymentENT;
import common.accounting.payment.PaymentLST;

public interface PaymentDaoInterface {

	public PaymentENT savePayment(PaymentENT ent) throws AMSException;

	public PaymentLST  getPaymentLST(PaymentLST searchENT) throws AMSException;

	public void removePayment(ArrayList<PaymentENT> ents) throws AMSException;
	
	public List<PaymentENT> searchCriteria(String accountName) throws AMSException ;
	
   public  PaymentENT getPaymentENT(PaymentENT ent) throws AMSException ;

}
