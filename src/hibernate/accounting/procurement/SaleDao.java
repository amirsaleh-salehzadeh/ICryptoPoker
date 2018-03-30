package hibernate.accounting.procurement;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.criterion.Restrictions;

import common.accounting.PaymentENT;
import common.accounting.SaleENT;
import common.accounting.SaleLST;
import hibernate.config.BaseHibernateDAO;

public class SaleDao extends BaseHibernateDAO implements SaleDaoInterface {

	@Override
	public SaleENT saveSale(SaleENT ent) {
		Session session = getSession();
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
	public SaleLST getSaleLST(SessionFactory conn) {
		SaleLST result = new SaleLST();
		Session session = getSession();
		Transaction tx = null;
		ArrayList<SaleENT> ents = null;
		try {
			tx = session.beginTransaction();
			ents = (ArrayList<SaleENT>) session.createQuery("FROM SaleENT")
					.list();
			result.setSaleENTs(ents);
			tx.commit();
		} catch (HibernateException e) {
			if (tx != null)
				tx.rollback();
			e.printStackTrace();
		} finally {
			session.close();
		}
		return result;
	}

	@Override
	public SaleLST getUserSales(SaleENT sale) {
		SaleLST result = new SaleLST();
		Session session = getSession();
		Transaction tx = null;
		List<SaleENT> ents = null;
		try {
			tx = session.beginTransaction();
			Criteria cr = session.createCriteria(PaymentENT.class);
			cr.add(Restrictions.gt("username", sale.getId()));
			ents = cr.list();
			tx.commit();
		} catch (HibernateException e) {
			if (tx != null)
				tx.rollback();
			e.printStackTrace();
		} finally {
			session.close();
		}
		return result;
	}

}