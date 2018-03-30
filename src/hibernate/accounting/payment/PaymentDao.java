package hibernate.accounting.payment;

import java.util.ArrayList;
import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.criterion.Restrictions;

import common.accounting.payment.PaymentENT;
import common.accounting.payment.PaymentLST;
import hibernate.accounting.payment.PaymentDaoInterface;
import hibernate.config.BaseHibernateDAO;

public class PaymentDao extends BaseHibernateDAO implements
		PaymentDaoInterface {

	static SessionFactory conn = null;

	@Override
	public PaymentENT savePayment(PaymentENT ent) {
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
	public PaymentLST getPaymentLST(PaymentLST paymentLSTSearch) {
		PaymentLST result = new PaymentLST();
		Session session = getSession();
		Transaction tx = null;
		ArrayList<PaymentENT> ents = null;
		try {
			tx = session.beginTransaction();
			ents = (ArrayList<PaymentENT>) session.createQuery("FROM PaymentENT")
					.list();
			result.setPaymentENTs(ents);
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
	public PaymentLST getUserPayments(PaymentLST searchENT) {
		PaymentLST result = new PaymentLST();
		Session session = getSession();
		Transaction tx = null;
		ArrayList<PaymentENT> ents = null;
		try {
			tx = session.beginTransaction();
			Criteria cr = session.createCriteria(PaymentENT.class);
			cr.add(Restrictions.gt("username", result.getPaymentENT().getId()));
			ents = (ArrayList<PaymentENT>) cr.list();
			tx.commit();
			result.setPaymentENTs(ents);
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