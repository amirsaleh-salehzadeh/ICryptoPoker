package hibernate.accounting.procurement;

import org.hibernate.SessionFactory;
import common.accounting.SaleENT;
import common.accounting.SaleLST;

public interface SaleDaoInterface {

	public SaleENT saveSale(SaleENT ent);

	public SaleLST getSaleLST(SessionFactory conn);

	public SaleLST getUserSales(SaleENT sale);

}
