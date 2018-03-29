package hibernate.game.poker.holdem;

import java.sql.Connection;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.criterion.Restrictions;

import common.game.poker.holdem.PaymentENT;
import common.game.poker.holdem.SaleEnt;
import hibernate.config.BaseHibernateDAO;

public class FinanceDaoImpl extends BaseHibernateDAO implements FinanceDaoInterface {

	static SessionFactory conn = null;

	public static void main(String[] args) {

		try {
			conn = new Configuration().configure().buildSessionFactory();
		} catch (Throwable ex) {
			System.err.println("Failed to create sessionFactory object." + ex);
			throw new ExceptionInInitializerError(ex);
		}

		Session session = conn.openSession();
		Transaction tx = null;
		Long id = null;

		try {
			tx = session.beginTransaction();
			SaleEnt ent = new SaleEnt();
			ent.setUsername("amir");

			ent.setStatus(false);
			ent.setDateTime(new Date());
			session.saveOrUpdate(ent);
			System.out.println(ent.getId());
			tx.commit();
		} catch (HibernateException e) {
			if (tx != null)
				tx.rollback();
			e.printStackTrace();
		} finally {
			session.close();
		}

	}

	@Override
	public PaymentENT savePayment(PaymentENT ent, SessionFactory conn) {
		// TODO Auto-generated method stub
		try {
			if (conn== null) {
				conn = new Configuration().configure().buildSessionFactory();
			}
		} catch (Throwable ex) {
			System.err.println("Failed to create sessionFactory object." + ex);
			throw new ExceptionInInitializerError(ex);
		}

		Session session = conn.openSession();
		Transaction tx = null;
		try {
			tx = session.beginTransaction();
			session.saveOrUpdate(ent);
			tx.commit();
		} catch (HibernateException e) {
			if (tx != null)
				tx.rollback();
			e.printStackTrace();
		} finally {
			session.close();
		}
		return ent;
	}

	@Override
	public SaleEnt saveSale(SaleEnt ent, SessionFactory conn) {
		// TODO Auto-generated method stub

		try {
			if (conn == null)
				conn = new Configuration().configure().buildSessionFactory();
		} catch (Throwable ex) {
			System.err.println("Failed to create sessionFactory object." + ex);
			throw new ExceptionInInitializerError(ex);
		}

		Session session = conn.openSession();
		Transaction tx = null;
		try {
			tx = session.beginTransaction();
			session.saveOrUpdate(ent);
			tx.commit();
		} catch (HibernateException e) {
			if (tx != null)
				tx.rollback();
			e.printStackTrace();
		} finally {
			session.close();
		}
		return ent;
	}

	@Override
	public List<PaymentENT> getAllPayments(SessionFactory conn) {
		// TODO Auto-generated method stub
		try {
			if (conn == null)
				conn = new Configuration().configure().buildSessionFactory();
		} catch (Throwable ex) {
			System.err.println("Failed to create sessionFactory object." + ex);
			throw new ExceptionInInitializerError(ex);
		}
		Session session = conn.openSession();
		Transaction tx = null;
		List<PaymentENT> ents = null;
		try {
			tx = session.beginTransaction();
			ents = session.createQuery("FROM PaymentENT").list();
			tx.commit();
		} catch (HibernateException e) {
			if (tx != null)
				tx.rollback();
			e.printStackTrace();
		} finally {
			session.close();
		}
		return ents;
	}

	@Override
	public List<SaleEnt> getAllSale(SessionFactory conn) {
		// TODO Auto-generated method stub
		try {
			if (conn == null)
				conn = new Configuration().configure().buildSessionFactory();
		} catch (Throwable ex) {
			System.err.println("Failed to create sessionFactory object." + ex);
			throw new ExceptionInInitializerError(ex);
		}
		Session session = conn.openSession();
		Transaction tx = null;
		List<SaleEnt> ents = null;
		try {
			tx = session.beginTransaction();
			ents = session.createQuery("FROM SaleEnt").list();
			tx.commit();
		} catch (HibernateException e) {
			if (tx != null)
				tx.rollback();
			e.printStackTrace();
		} finally {
			session.close();
		}
		return ents;
	}

	@Override
	public List<PaymentENT> getUserPayments(String id, SessionFactory conn) {
		// TODO Auto-generated method stub
		try {
			if (conn == null)
				conn = new Configuration().configure().buildSessionFactory();
		} catch (Throwable ex) {
			System.err.println("Failed to create sessionFactory object." + ex);
			throw new ExceptionInInitializerError(ex);
		}
		Session session = conn.openSession();
		Transaction tx = null;
		List<PaymentENT> ents = null;
		try {
			tx = session.beginTransaction();
			Criteria cr = session.createCriteria(PaymentENT.class);
			cr.add(Restrictions.gt("username", id));
			ents = cr.list();
			tx.commit();
		} catch (HibernateException e) {
			if (tx != null)
				tx.rollback();
			e.printStackTrace();
		} finally {
			session.close();
		}
		return ents;
	}

	@Override
	public List<SaleEnt> getUserSales(String id, SessionFactory conn) {
		// TODO Auto-generated method stub
		try {
			if (conn == null)
				conn = new Configuration().configure().buildSessionFactory();
		} catch (Throwable ex) {
			System.err.println("Failed to create sessionFactory object." + ex);
			throw new ExceptionInInitializerError(ex);
		}
		Session session = conn.openSession();
		Transaction tx = null;
		List<SaleEnt> ents = null;
		try {
			tx = session.beginTransaction();
			Criteria cr = session.createCriteria(PaymentENT.class);
			cr.add(Restrictions.gt("username", id));
			ents = cr.list();
			tx.commit();
		} catch (HibernateException e) {
			if (tx != null)
				tx.rollback();
			e.printStackTrace();
		} finally {
			session.close();
		}
		return ents;
	}

}
