package test.game.poker.holdem;

import static org.junit.Assert.*;

import java.util.Date;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import common.game.poker.holdem.SaleEnt;
import game.poker.holdem.dao.FinanceDaoImpl;
import common.game.poker.holdem.SaleEnt;

public class SaleTest extends FinanceDaoImpl {

	
	@Test
	public void Savetest() {
		SaleEnt tester = testerObject() ;
	   SaleEnt cur =	super.saveSale(tester, null) ;
		assertNotNull(cur.getId());
		
	}
	@Test
	public void GetAllTest() {
		List<SaleEnt> temp = super.getAllSale(null) ;
		assertEquals(1, temp.size());
		
	}
	@Test
	public void GetUserTest() {
        List<SaleEnt> temp = super.getUserSales("amir",null) ;
        SaleEnt tester = testerObject() ;
        boolean check  = true;
        for(SaleEnt cur:temp) {
        	   if(!cur.getUsername().equals(tester.getUsername())){
        		   check = false ;
        	   }
        }
        assertEquals(true,check);
	
	}
	
	public SaleEnt testerObject() {
		SaleEnt ent = new SaleEnt();
		ent.setUsername("amir");
		ent.setStatus(false);
		ent.setDateTime(new Date());
		ent.setAmount(1000);
		ent.setBankResponse("pending");
		ent.setCurrency("rand");
	
	    return ent ;
		
		
	}

}
