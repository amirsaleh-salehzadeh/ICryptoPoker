package hibernate.game.poker.holdem;

import java.sql.Connection;
import java.util.List;
import java.util.Set;

import org.hibernate.SessionFactory;

import common.game.poker.holdem.PaymentENT;
import common.game.poker.holdem.SaleEnt;

public interface FinanceDaoInterface {
	
         PaymentENT savePayment(PaymentENT ent ,SessionFactory conn) ;
         SaleEnt    saveSale(SaleEnt ent ,SessionFactory conn) ;
          List<PaymentENT> getAllPayments(SessionFactory conn) ;
          List<SaleEnt> getAllSale(SessionFactory conn) ;
          List<PaymentENT> getUserPayments(String id, SessionFactory conn) ;
          List<SaleEnt> getUserSales(String id , SessionFactory conn) ;
          
        
         

}
