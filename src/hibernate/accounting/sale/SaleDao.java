package hibernate.accounting.sale;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
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
		SaleLST results = new SaleLST() ;
		ArrayList<SaleENT> ents = new ArrayList<SaleENT>() ;
		try {
			tx = session.beginTransaction();
			
			Criteria cr = session.createCriteria(SaleENT.class);
			
			if(lst.getUsername()==null) {
			
				 ents = ((ArrayList<SaleENT>) session.createQuery("FROM SaleENT").list());
				
			} else {
			cr.add(Restrictions.ilike("username", lst.getUsername()));
			lst.setTotalItems(cr.list().size());
		
			 ents= ((ArrayList<SaleENT>) cr.list());
			}
			results.setSaleENTs(ents);
			tx.commit();
		} catch (HibernateException e) {
			if (tx != null)
				tx.rollback();
			e.printStackTrace();
		} finally {
			session.close();
		}
		return results ;
	}

	@Override
	public void removeSales(ArrayList<SaleENT> ents) throws AMSException {
		// TODO Auto-generated method stub
		Session session = getSession();
		Transaction tx = null;
		
		try {
			session.flush();
			session.clear();
			tx = session.beginTransaction();
			Query query = null;
			for (SaleENT ent: ents) {
				query = session.createQuery("DELETE SaleENT  WHERE saleId IN (:ids)");
				query.setParameter("ids", ent.getSaleId());
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

	@Override
	public SaleENT getSaleENT(SaleENT ent) throws AMSException {
		// TODO Auto-generated method stub
		Session session = getSession();
		Transaction tx = null;
		
		try {
			tx = session.beginTransaction();
			  ent =  (SaleENT)session.get(SaleENT.class, ent.getSaleId()) ;
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

}