package game.poker.holdem.dao;

import java.sql.Connection;
import java.util.Set;

import game.poker.holdem.domain.Payment;
import game.poker.holdem.domain.Sale;

public interface FinanceDaoInterface {
	
         Payment savePayment(Payment ent ,Connection conn) ;
         Sale    saveSale(Sale ent ,Connection conn) ;
         Payment mergePayment(Payment ent ,Connection conn) ;
         Sale    mergeSale(Sale ent , Connection conn) ;
         Set<Payment> getAllPayments(long id, Connection conn) ;
         Set<Sale> getAllSale(long id , Connection conn) ;
          Payment updatePayment(Payment ent ,Connection conn) ;
          Sale    updateSale(Sale ent, Connection conn) ;
          Set<Payment> getUserPayments(String id, Connection conn) ;
          Set<Sale> getUserSales(String id , Connection conn) ;
          
        
         

}
