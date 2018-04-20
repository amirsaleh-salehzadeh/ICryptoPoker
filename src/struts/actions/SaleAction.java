/*
 * Generated by MyEclipse Struts
 * Template path: templates/java/JavaClass.vtl
 */
package struts.actions;

import hibernate.accounting.sale.SaleDaoInterface;
import hibernate.config.ICryptoPokerDAOManager;
import tools.AMSErrorHandler;
import tools.AMSException;
import tools.AMSUtililies;

import java.io.IOException;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.sql.*;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

import com.google.gson.Gson;

import common.MessageENT;
import common.PopupENT;
import common.accounting.payment.PaymentENT;
import common.accounting.payment.PaymentLST;
import common.accounting.sale.SaleENT;
import common.accounting.sale.SaleLST;
import common.security.RoleENT;
import common.security.RoleLST;

/**
 * MyEclipse Struts Creation date: 09-21-2010
 * 
 * XDoclet definition:
 * 
 * @struts.action parameter="reqCode" validate="true"
 * @struts.action-forward name="list" path="/jsp/farsi/news/newsList.jsp"
 */
public class SaleAction extends Action {
	private static String success = "";
	private static String error = "";
	private String reqCode = "";

	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {
		ActionForward af = null;
		success = "";
		error = "";
		reqCode = request.getParameter("reqCode");
		if (reqCode == null) {
			reqCode = "saleManagement";
		}
		System.out.println(reqCode);
		if (reqCode.equalsIgnoreCase("saleManagement") || reqCode.equals("gridJson")) {
			return saleManagement(request, mapping, form);
		} else if (reqCode.equals("saleEdit") || reqCode.equals("saleView") || reqCode.equals("saleNew")) {
			return editSale(request, mapping, form);
		} else if (reqCode.equals("saveUpdateSale")) {
			return saveUpdateSale(request, mapping, form);

		}
		return mapping.findForward(reqCode);
	}

	private ActionForward saleManagement(HttpServletRequest request, ActionMapping mapping, ActionForm form) {
		createMenusForSales(request);
		SaleLST saleLST = (SaleLST) form;
		try {
			saleLST = getSaleDAO().getSaleLST(saleLST) ;
		} catch (AMSException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		request.setAttribute("saleLST", saleLST);// we call it a bean named paymentLST
		Gson gson = new Gson();
		String json = AMSUtililies.prepareTheJSONStringForDataTable(saleLST.getCurrentPage(),
				saleLST.getTotalItems(), gson.toJson(saleLST.getSaleENTs()),"saleId", success, error);
		request.setAttribute("json", json);
		MessageENT m = new MessageENT(success, error);
		request.setAttribute("message", m);
		if (request.getParameter("reqCodeGrid") != null && request.getParameter("reqCodeGrid").equals("gridJson"))
			return mapping.findForward("gridJson");
		MessageENT mm = new MessageENT(success, error);
		request.setAttribute("message", mm);
		return mapping.findForward("saleManagement");
	}

	//
	private ActionForward editSale(HttpServletRequest request, ActionMapping mapping, ActionForm form) {
		SaleENT saleENT = new SaleENT();
		long saleId;
		SaleLST lst = (SaleLST) form;
		if (request.getParameter("saleId") == null) {
			return mapping.findForward("saleEdit");
		}
		// request.setAttribute("paymentENT", paymentENT);

		saleId = Long.parseLong(request.getParameter("saleId"));

		saleENT.setSaleId(saleId);
		// saveTheForm();
		try {
			saleENT = getSaleDAO().getSaleENT(saleENT) ;
			lst.setSaleENT(saleENT);
			request.setAttribute("paymentLST",lst);
		} catch (AMSException e) {
			error = e.getMessage();
			e.printStackTrace();
		}
		MessageENT m = new MessageENT(success, error);
		request.setAttribute("message", m);
		return mapping.findForward("saleEdit");

	}

	//
	private void createMenusForSales(HttpServletRequest request) {
		List<PopupENT> popupEnts = new ArrayList<PopupENT>();
		popupEnts.add(new PopupENT("hide-filters", "displaySearch();", "Show/Hide Search", "#"));
		popupEnts
				.add(new PopupENT("new-item", "callAnAction(\"sale.do?reqCode=saleEdit\");", "New Sale", "#"));
		popupEnts.add(new PopupENT("delete-item", "deleteSelectedItems(\"deleteSale\");", "Delete Selected", "#"));
		// popupEnts
		// .add(new PopupENT("edit-item",
		// "callAnAction(\"payment.do?reqCode=paymentEdit\");",
		// "Edit Payment", "#"));

		List<PopupENT> popupGridEnts = new ArrayList<PopupENT>();
		popupGridEnts.add(new PopupENT("hide-filters",
				"callAnAction(\"payment.do?reqCode=saleView&saleId=REPLACEME\");", "View Sale", "#"));
		popupGridEnts.add(new PopupENT("edit-item",
				"callAnAction(\"payment.do?reqCode=saleEdit&saleId=REPLACEME\");", "Edit Sale", "#"));

		popupGridEnts
				.add(new PopupENT("delete-item", "deleteAnItem(\"REPLACEME\", \"deleteSale\");", "Remove", "#"));

		request.setAttribute("settingMenuItem", popupEnts);
		request.setAttribute("gridMenuItem", popupGridEnts);
	}

	//
	private ActionForward saveUpdateSale(HttpServletRequest request, ActionMapping mapping, ActionForm form) {
		
		SaleLST lst = (SaleLST) form;
		SaleENT saleENT = lst.getSaleENT();
		try {
			if(saleENT.getDateTime()==null) {
			DateFormat df = new SimpleDateFormat("yyyy/mm/dd HH:mm:ss");
			Date date = new Date( ) ;
			
		    date.setTime(Calendar.getInstance().getTime().getTime());
		    
			saleENT.setDateTime(date);
			
			}
			saleENT = getSaleDAO().saveUpdateSale(saleENT);
			success = "The user '" + saleENT.getCreatorUsername() + "' saved successfully";
		} catch (AMSException e) {
			error = AMSErrorHandler.handle(request, this, e, "", "");
		}
		request.setAttribute("saleENT", saleENT);
		MessageENT m = new MessageENT(success, error);
		request.setAttribute("message", m);
		return mapping.findForward("saleEdit");
	}

	
	 private void deleteUser(HttpServletRequest request) {
	 String[] delID = request.getParameter("deleteID").split(",");
	 ArrayList<SaleENT> salesToDelete = new ArrayList<SaleENT>();
	 for (int i = 0; i < delID.length; i++) {
	 SaleENT sale = new SaleENT();
	 sale.setSaleId(Long.parseLong(delID[i]));
	 salesToDelete.add(sale);
	 try {
	 getSaleDAO().removeSales(salesToDelete);
	 success = "The sale(s) removed successfully";
	 } catch (AMSException e) {
	 e.printStackTrace();
	 error = AMSErrorHandler.handle(request, this, e, "", "");
	 }
	 }
	 MessageENT m = new MessageENT(success, error);
	 request.setAttribute("message", m);
	 }
	
	private SaleENT getSaleENT(HttpServletRequest request) {
		// date format for registration date
		DateFormat df = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
		SaleENT saleENT = new SaleENT();
		if (request.getParameter("saleId") != null)
			saleENT.setSaleId(Long.parseLong(request.getParameter("saleId")));
		else {

		}
		if (saleENT.getDateTime() == null)
		//	saleENT.setDateTime(Date.valueOf(df.format(Calendar.getInstance().getTime())));
		saleENT.setUsername(request.getParameter("userName"));
		saleENT.setCreatorUsername(request.getParameter("creatorUsername"));
		saleENT.setBankResponse(request.getParameter("bankResponse"));
		saleENT.setStatus(Integer.parseInt(request.getParameter("status")));
		saleENT.setAmount(Double.parseDouble(request.getParameter("amount")));
		saleENT.setPaymentMethod(Integer.parseInt(request.getParameter("paymentMethod")));
		

		return saleENT;
	}

	private static SaleDaoInterface getSaleDAO() {
		return ICryptoPokerDAOManager.getSaleDAOInterface();
	}
}