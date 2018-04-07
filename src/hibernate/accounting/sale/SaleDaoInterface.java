package hibernate.accounting.sale;

import tools.AMSException;

import java.util.List;


import common.accounting.sale.SaleENT;
import common.accounting.sale.SaleLST;

public interface SaleDaoInterface {

	public SaleENT saveUpdateSale(SaleENT ent) throws AMSException;

	public SaleENT getSaleENT(SaleENT ent) throws AMSException;

	public SaleLST getSaleLST(SaleLST lst) throws AMSException;

	public SaleENT removeSales(SaleENT sale) throws AMSException;

	public List<SaleENT> searchCriteria(String accountName) throws AMSException;

}
