package hibernate.accounting.sale;

import org.hibernate.SessionFactory;

import common.accounting.sale.SaleENT;
import common.accounting.sale.SaleLST;

public interface SaleDaoInterface {

	public SaleENT saveSale(SaleENT ent);

	public SaleLST getSaleLST(SessionFactory conn);

	public SaleLST getUserSales(SaleENT sale);

}
