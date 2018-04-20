package hibernate.accounting.sale;

import tools.AMSException;

import java.util.ArrayList;
import java.util.List;

import common.accounting.payment.PaymentENT;
import common.accounting.sale.SaleENT;
import common.accounting.sale.SaleLST;

public interface SaleDaoInterface {

	public SaleENT saveUpdateSale(SaleENT ent) throws AMSException;

	public SaleENT getSaleENT(SaleENT ent) throws AMSException;

	public SaleLST getSaleLST(SaleLST lst) throws AMSException;

	public void removeSales(ArrayList<SaleENT> ents) throws AMSException;

	public List<SaleENT> searchCriteria(String accountName) throws AMSException;

}
