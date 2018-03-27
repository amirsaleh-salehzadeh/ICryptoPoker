package game.poker.holdem.domain;

import java.sql.Connection;
import java.util.Set;

import game.poker.holdem.dao.FinanceDaoInterface;
import hibernate.config.BaseHibernateDAO;

public class FinanceDaoImpl  extends BaseHibernateDAO implements FinanceDaoInterface {

	@Override
	public Payment savePayment(Payment ent, Connection conn) {
		
		
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Sale saveSale(Sale ent, Connection conn) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Payment mergePayment(Payment ent, Connection conn) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Sale mergeSale(Sale ent, Connection conn) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set<Payment> getAllPayments(long id, Connection conn) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set<Sale> getAllSale(long id, Connection conn) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Payment updatePayment(Payment ent, Connection conn) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Sale updateSale(Sale ent, Connection conn) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set<Payment> getUserPayments(String id, Connection conn) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set<Sale> getUserSales(String id, Connection conn) {
		// TODO Auto-generated method stub
		return null;
	}

}
