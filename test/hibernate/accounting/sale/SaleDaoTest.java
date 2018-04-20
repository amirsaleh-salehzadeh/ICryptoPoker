package hibernate.accounting.sale;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Test;

import common.accounting.sale.SaleLST;
import tools.AMSException;
import common.accounting.sale.SaleENT ;

public class SaleDaoTest {

	@Test
	public void testSaveUpdateSale() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetSaleLST() {
		SaleLST saleLst = new SaleLST() ;
		SaleDao saleDao = new SaleDao() ;
		try {
			saleLst.setUsername("neil");
			saleLst = saleDao.getSaleLST(saleLst) ;
		} catch (AMSException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		for(SaleENT ent : saleLst.getSaleENTs()) {
			System.out.println(ent.getUsername());
		}
		assertNotNull(saleLst.getSaleENT());
	}

	@Test
	public void testRemoveSales() {
		
	}

}
