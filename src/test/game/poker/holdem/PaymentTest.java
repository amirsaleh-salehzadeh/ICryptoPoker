package test.game.poker.holdem;

import static org.junit.Assert.*;

import java.util.Date;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import common.game.poker.holdem.PaymentENT;
import game.poker.holdem.dao.FinanceDaoImpl;

public class PaymentTest extends FinanceDaoImpl {

	
     
	@Test
	public void Savetest() {
		PaymentENT tester = testerObject() ;
	   PaymentENT cur =	super.savePayment(tester,null) ;
		assertNotNull(cur.getId());
		
	}
	@Test
	public void GetAllTest() {
		List temp = super.getAllPayments(null) ;
		assertEquals(1, temp.size());
		
	}
	@Test
	public void GetUserTest() {
        List<PaymentENT> temp = super.getUserPayments("amir",null) ;
        PaymentENT tester = testerObject() ;
        boolean check  = true;
        for(PaymentENT cur:temp) {
        	   if(!cur.getUsername().equals(tester.getUsername())){
        		   check = false ;
        	   }
        }
        assertEquals(true,check);
	
	}
	
	public PaymentENT testerObject() {
		PaymentENT ent = new PaymentENT() ;
		ent.setDateTime(new Date());
		ent.setUsername("amir");
		ent.setReason("game");
		ent.setBankResponse("pending");
		ent.setCurrency("rand");
		ent.setStatus(false);
		ent.setAmount(1000);
	    return ent ;
		
		
	}

}
