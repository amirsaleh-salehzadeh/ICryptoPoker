package hibernate.accounting.payment;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.criterion.Expression;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;

import tools.AMSException;

import common.accounting.payment.PaymentENT;
import common.accounting.payment.PaymentLST;
import common.accounting.sale.SaleENT;
import hibernate.accounting.payment.PaymentDaoInterface;
import hibernate.config.BaseHibernateDAO;

public class PaymentDao extends BaseHibernateDAO implements PaymentDaoInterface {

	static SessionFactory conn = null;

	@Override
	public PaymentENT savePayment(PaymentENT ent) throws AMSException {
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
	public PaymentLST getPaymentLST(PaymentLST paymentLSTSearch)
			throws AMSException {
		PaymentLST result = new PaymentLST();
		Session session = getSession();
		Transaction tx = null;
		ArrayList<PaymentENT> ents = null;
		Criteria cr = session.createCriteria(PaymentENT.class);
		try {
			tx = session.beginTransaction();
			// if (paymentLSTSearch.getPaymentENT() != null) {
			ents = (ArrayList<PaymentENT>) session.createQuery(
					"FROM PaymentENT").list();
			// } else {
			cr.add(Restrictions.like("username",
					paymentLSTSearch.getSearchUsername(), MatchMode.ANYWHERE));
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			try {
				cr.add(Expression.ge("dateTime",
						sdf.parse(paymentLSTSearch.getSearchFromDate())));
				cr.add(Expression.lt("dateTime",
						sdf.parse(paymentLSTSearch.getSearchToDate())));
			} catch (ParseException e) {
				e.printStackTrace();
			}
			paymentLSTSearch.setTotalItems(cr.list().size());
			cr.setFirstResult(paymentLSTSearch.getFirst());
			cr.setMaxResults(paymentLSTSearch.getPageSize());
			ents = ((ArrayList<PaymentENT>) cr.list());
			// }
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
	public List<PaymentENT> searchCriteria(String accountName)
			throws AMSException {
		// TODO Auto-generated method stub

		if (accountName == null) {
			return getPaymentLST(null).getPaymentENTs();
		}
		Session session = getSession();
		Transaction tx = null;
		ArrayList<PaymentENT> ents = null;
		try {
			tx = session.beginTransaction();
			Criteria cr = session.createCriteria(PaymentENT.class);
			cr.add(Restrictions.gt("status", 0));
			cr.add(Restrictions.gt("reason", accountName));
			ents = (ArrayList<PaymentENT>) cr.list();
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
	public PaymentENT getPaymentENT(PaymentENT ent) throws AMSException {
		// TODO Auto-generated method stub
		Session session = getSession();
		Transaction tx = null;

		try {
			tx = session.beginTransaction();
			ent = (PaymentENT) session
					.get(PaymentENT.class, ent.getPaymentId());
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
	public void removePayment(ArrayList<PaymentENT> ents) throws AMSException {
		// TODO Auto-generated method stub

		Session session = getSession();
		Transaction tx = null;

		try {
			session.flush();
			session.clear();
			tx = session.beginTransaction();
			Query query = null;
			for (PaymentENT ent : ents) {
				query = session
						.createQuery("DELETE PaymentENT p WHERE paymentId IN (:ids)");
				query.setParameter("ids", ent.getPaymentId());
				query.executeUpdate();
				tx.commit();
			}
		} catch (HibernateException e) {
			if (tx != null)
				tx.rollback();
			e.printStackTrace();
		} finally {
			session.close();
		}

	}

}