package hibernate.accounting.sale;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;

import tools.AMSException;

import common.accounting.payment.PaymentENT;
import common.accounting.sale.SaleENT;
import common.accounting.sale.SaleLST;
import hibernate.config.BaseHibernateDAO;

public class SaleDao extends BaseHibernateDAO implements SaleDaoInterface {

	@Override
	public SaleENT saveUpdateSale(SaleENT ent) throws AMSException {
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
	public SaleLST getSaleLST(SaleLST lst) throws AMSException  {
		Session session = getSession();
		Transaction tx = null;
		try {
			tx = session.beginTransaction();
			Criteria cr = session.createCriteria(PaymentENT.class);
			cr.add(Restrictions.gt("username", lst.getSaleENT().getSaleId()));
			lst.setTotalItems(cr.list().size());
			cr.setFirstResult(lst.getFirst());
			cr.setMaxResults(lst.getPageSize());
			lst.setSaleENTs((ArrayList<SaleENT>) cr.list());
			tx.commit();
		} catch (HibernateException e) {
			if (tx != null)
				tx.rollback();
			e.printStackTrace();
		} finally {
			session.close();
		}
		return lst;
	}

	@Override
	public SaleENT removeSales(SaleENT sale) throws AMSException {
		// TODO Auto-generated method stub
		return null;
	}
    
	@Override
	public List<SaleENT> searchCriteria(String accountName) throws AMSException {
		// TODO Auto-generated method stub
	    
		if(accountName==null) {
			   return getSaleLST(null).getSaleENTs() ;	
			}
		Session session = getSession();
		Transaction tx = null;
		ArrayList<SaleENT> ents = null;
		try {
			tx = session.beginTransaction();
			Criteria cr = session.createCriteria(SaleENT.class);
			cr.add(Restrictions.gt("status", 0));
			cr.add(Restrictions.gt("reason",accountName));
			ents =(ArrayList<SaleENT>) cr.list() ;
			tx.commit();
		} catch (HibernateException e) {
			if (tx != null)
				tx.rollback();
			e.printStackTrace();
		} finally {
			session.close();
		}
		return ents ;
		
	}

}