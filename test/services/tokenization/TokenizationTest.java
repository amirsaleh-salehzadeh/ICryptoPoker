package services.tokenization;

import static org.junit.Assert.*;

import java.util.Date;

import javax.xml.bind.DatatypeConverter;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import game.poker.holdem.domain.Player;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

public class TokenizationTest {
	String token ;

	@Test
	public void testGetPublicKey() {
		Player ent = testerObject();
		Date targetTime = new Date(); // now
		targetTime.setMinutes(59);
        token = Tokenization.getPublicKey(ent.getId(), ent, targetTime) ;
        System.out.println(token);
        assertNotNull(token);
        assertEquals(true, Tokenization.verify(token,ent));
        
        
	}

	@Test
	public void testVerify() {
		Player ent = testerObject() ;
		Date targetTime = new Date(); // now
		targetTime.setMinutes(59);
		token = Tokenization.getPublicKey(ent.getId(), ent, targetTime) ;
		assertEquals("testing claim issue",true,Tokenization.verify(token,ent));
		ent.setName("me");
		assertEquals("testing claim issue",false,Tokenization.verify(token,ent));
		assertEquals("testing if giving non token",false,Tokenization.verify("hello",ent));
		targetTime.setMinutes(21);
		token = Tokenization.getPublicKey(ent.getId(), ent, targetTime) ;
		assertEquals("testing expiring",false, Tokenization.verify(token,ent)) ;
		  
		
	}

	private Player testerObject() {
		Player ent = new Player();
		ent.setId("amir");
		ent.setPassword("amir");
		ent.setName("saleh");
		return ent;

	}
	
}
