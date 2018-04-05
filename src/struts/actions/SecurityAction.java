/*
 * Generated by MyEclipse Struts
 * Template path: templates/java/JavaClass.vtl
 */
package struts.actions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


import hibernate.security.SecurityDAOInterface;
import hibernate.user.UserDAOInterface;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

import tools.AMSErrorHandler;
import tools.AMSException;
import tools.AMSUtililies;
import tools.NVL;
import common.MessageENT;
import common.PopupENT;
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
public class SecurityAction extends Action {
//	private static String success = "";
//	private static String error = "";
//	private String reqCode = "";
//
//	public ActionForward execute(ActionMapping mapping, ActionForm form,
//			HttpServletRequest request, HttpServletResponse response) {
//		// TODO Auto-generated method stub
//		ActionForward af = null;
//		success = "";
//		error = "";
//		reqCode = request.getParameter("reqCode");
//		if (reqCode == null)
//			reqCode = "roleManagement";
//		if (reqCode.equalsIgnoreCase("deleteRole")) {
//			deleteRole(request);
//			reqCode = "roleManagement";
//		}
//		if (reqCode.equalsIgnoreCase("saveUpdateGroupRole")) {
//			saveUpdateGroupRole(request, mapping);
//			reqCode = "groupRoleView";
//		}
//		if (reqCode.equalsIgnoreCase("deleteGroup")) {
//			deleteGroup(request);
//			reqCode = "groupManagement";
//		}
//		if (reqCode.equalsIgnoreCase("saveUpdateUserBuildings")) {
//			saveUpdateUserBuildings(request);
//			reqCode = "userBuildings";
//		}
//		if (reqCode.equalsIgnoreCase("roleManagement")) {
//			return roleManagement(request, mapping);
//		} else if (reqCode.equals("roleEdit")) {
//			return editRole(request, mapping, form);
//		} else if (reqCode.equals("saveUpdateRole")) {
//			return saveUpdateRole(request, mapping);
//		} else if (reqCode.equalsIgnoreCase("groupManagement")) {
//			return groupManagement(request, mapping);
//		} else if (reqCode.equals("groupEdit")) {
//			return editGroup(request, mapping, form);
//		} else if (reqCode.equals("saveUpdateGroup")) {
//			return saveUpdateGroup(request, mapping);
//		} else if (reqCode.equalsIgnoreCase("groupRoleView")) {
//			return groupRoleView(request, mapping);
//		} else if (reqCode.equalsIgnoreCase("userBuildings")) {
//			return userBuildings(request, mapping);
//		}
//		return af;
//	}
//
//	private void saveUpdateUserBuildings(HttpServletRequest request) {
//		String[] t = request.getParameterValues("locationIDs");
//		long[] locationIds = new long[t.length];
//		for (int i = 0; i < t.length; i++) {
//			locationIds[i] = Long.parseLong(t[i]);
//		}
//		try {
//			getSecurityDAO().addLocationUser(request.getParameter("userName"), locationIds);
//		} catch (AMSException e) {
//			e.printStackTrace();
//		}
//		success = "saved successfully";
//		MessageENT m = new MessageENT(success, error);
//		request.setAttribute("message", m);
//		
//	}
//
//	private ActionForward userBuildings(HttpServletRequest request,
//			ActionMapping mapping) {
//		request.setAttribute("userName", request.getParameter("userName"));
//		long[] locationIds;
//		try {
//			request.setAttribute("userLocationIds", getSecurityDAO().getLocationUser(
//					request.getParameter("userName")));
//		} catch (AMSException e) {
//			e.printStackTrace();
//		}
//		return mapping.findForward("userBuildings");
//
//	}
//
//	private void saveUpdateGroupRole(HttpServletRequest request,
//			ActionMapping mapping) {
//		String[] t = request.getParameterValues("groupRoleName");
//		GroupENT u = new GroupENT();
//		u.setGroupID(NVL.getInt(request.getParameter("groupID")));
//		ArrayList<RoleENT> roles = new ArrayList<RoleENT>();
//		if (t != null && t.length > 0)
//			for (int i = 0; i < t.length; i++) {
//				RoleENT r = new RoleENT(t[i]);
//				roles.add(r);
//			}
//		u.setGroupRoles(roles);
//		try {
//			getSecurityDAO().saveGroupRole(u);
//			success = "Roles saved successfully";
//		} catch (AMSException e) {
//			e.printStackTrace();
//			error = AMSErrorHandler.handle(request, this, e, "", "");
//		}
//		MessageENT m = new MessageENT(success, error);
//		request.setAttribute("message", m);
//	}
//
//	private ActionForward groupRoleView(HttpServletRequest request,
//			ActionMapping mapping) {
//		String searchKey = "";
//		int gid = Integer.parseInt(request.getParameter("groupID"));
//		if (request.getParameter("searchRole.roleName") != null) {
//			searchKey = request.getParameter("searchRole.roleName");
//		}
//		try {
//			request.setAttribute("groupENT",
//					getSecurityDAO().getGroup(new GroupENT(gid)));
//		} catch (NumberFormatException e) {
//			e.printStackTrace();
//		} catch (AMSException e) {
//			e.printStackTrace();
//		}
//		request.setAttribute("groupENTRoles", getSecurityDAO()
//				.getAllRolesForAGroup(gid));
//		request.setAttribute("roleLST", getRoleLST(request).getRoleENTs());
//		return mapping.findForward("groupRole");
//	}
//
//	private ActionForward saveUpdateRole(HttpServletRequest request,
//			ActionMapping mapping) {
//		try {
//			request.setAttribute("clientENTs", getClientDAO()
//					.getClientsDropDown());
//		} catch (AMSException e1) {
//			// TODO Auto-generated catch block
//			e1.printStackTrace();
//		}
//		RoleENT roleENT = getRoleENT(request);
//		try {
//			roleENT = getSecurityDAO().saveUpdateRole(roleENT, null);
//			success = "The role '" + roleENT.getRoleName()
//					+ "' saved successfully";
//		} catch (AMSException e) {
//			error = AMSErrorHandler.handle(request, this, e, "", "");
//		}
//		request.setAttribute("roleENT", roleENT);
//		MessageENT m = new MessageENT(success, error);
//		request.setAttribute("message", m);
//		return mapping.findForward("roleEdit");
//	}
//
//	private ActionForward saveUpdateGroup(HttpServletRequest request,
//			ActionMapping mapping) {
//		try {
//			request.setAttribute("clientENTs", getClientDAO()
//					.getClientsDropDown());
//		} catch (AMSException e1) {
//			// TODO Auto-generated catch block
//			e1.printStackTrace();
//		}
//		GroupENT groupENT = getGroupENT(request);
//		try {
//			groupENT = getSecurityDAO().saveUpdateGroup(groupENT, null);
//			success = "The group '" + groupENT.getGroupName()
//					+ "' saved successfully";
//		} catch (AMSException e) {
//			error = AMSErrorHandler.handle(request, this, e, "", "");
//		}
//		request.setAttribute("groupENT", groupENT);
//		MessageENT m = new MessageENT(success, error);
//		request.setAttribute("message", m);
//		return mapping.findForward("groupEdit");
//	}
//
//	private ActionForward editRole(HttpServletRequest request,
//			ActionMapping mapping, ActionForm form) {
//		RoleENT roleENT = new RoleENT();
//		if (request.getParameter("roleName") == null) {
//			request.setAttribute("roleENT", roleENT);
//			return mapping.findForward("roleEdit");
//		}
//		roleENT.setRoleName(request.getParameter("roleName"));
//		try {
//			request.setAttribute("roleENT", getSecurityDAO().getRole(roleENT));
//		} catch (AMSException e) {
//			error = e.getMessage();
//			e.printStackTrace();
//		}
//		MessageENT m = new MessageENT(success, error);
//		request.setAttribute("message", m);
//		return mapping.findForward("roleEdit");
//	}
//
//	private ActionForward editGroup(HttpServletRequest request,
//			ActionMapping mapping, ActionForm form) {
//		GroupENT groupENT = new GroupENT();
//		int groupId = 0;
//		try {
//			request.setAttribute("clientENTs", getClientDAO()
//					.getClientsDropDown());
//		} catch (AMSException e) {
//			e.printStackTrace();
//		}
//		if (request.getParameter("groupID") != null)
//			groupId = Integer.parseInt(request.getParameter("groupID"));
//		else {
//			request.setAttribute("groupENT", groupENT);
//			return mapping.findForward("groupEdit");
//		}
//		groupENT.setGroupID(groupId);
//		try {
//			request.setAttribute("groupENT", getSecurityDAO()
//					.getGroup(groupENT));
//		} catch (AMSException e) {
//			error = e.getMessage();
//			e.printStackTrace();
//		}
//		MessageENT m = new MessageENT(success, error);
//		request.setAttribute("message", m);
//		return mapping.findForward("groupEdit");
//	}
//
//	private void deleteRole(HttpServletRequest request) {
//		String[] delId = request.getParameter("deleteID").split(",");
//		ArrayList<RoleENT> rolesToDelete = new ArrayList<RoleENT>();
//		for (int i = 0; i < delId.length; i++) {
//			RoleENT role = new RoleENT(delId[i]);
//			rolesToDelete.add(role);
//		}
//		try {
//			getSecurityDAO().deleteRoles(rolesToDelete);
//			success = "The role(s) removed successfully";
//		} catch (AMSException e) {
//			e.printStackTrace();
//			error = AMSErrorHandler.handle(request, this, e, "", "");
//		}
//	}
//
//	private void deleteGroup(HttpServletRequest request) {
//		String[] delId = request.getParameter("deleteID").split(",");
//		ArrayList<GroupENT> groupsToDelete = new ArrayList<GroupENT>();
//		for (int i = 0; i < delId.length; i++) {
//			GroupENT group = new GroupENT(Integer.parseInt(delId[i]));
//			groupsToDelete.add(group);
//		}
//		try {
//			getSecurityDAO().deleteGroups(groupsToDelete);
//			success = "The group(s) removed successfully";
//		} catch (AMSException e) {
//			e.printStackTrace();
//			error = AMSErrorHandler.handle(request, this, e, "", "");
//		}
//	}
//
//	private ActionForward roleManagement(HttpServletRequest request,
//			ActionMapping mapping) {
//		try {
//			createMenusForRole(request);
//			request.setAttribute("clientENTs", getClientDAO()
//					.getClientsDropDown());
//			RoleLST roleLST = getRoleLST(request);
//			request.setAttribute("roleLST", roleLST);
//			ObjectMapper objectMapper  ;
//			ObjectMapper mapper = new ObjectMapper();
//			String json = "";
//			try {
//				json = mapper.writeValueAsString(roleLST.getRoleENTs());
//			} catch (JsonGenerationException e) {
//				e.printStackTrace();
//			} catch (JsonMappingException e) {
//				e.printStackTrace();
//			} catch (IOException e) {
//				e.printStackTrace();
//			}
//			json = AMSUtililies.prepareTheJSONStringForDataTable(
//					roleLST.getCurrentPage(), roleLST.getTotalItems(), json,
//					"roleName", success, error);
//			request.setAttribute("json", json);
//			if (request.getParameter("reqCodeGrid") != null
//					&& request.getParameter("reqCodeGrid").equals("gridJson"))
//				return mapping.findForward("gridJson");
//		} catch (AMSException e) {
//			e.printStackTrace();
//		}
//		MessageENT m = new MessageENT(success, error);
//		request.setAttribute("message", m);
//		return mapping.findForward("roleManagement");
//	}
//
//	private ActionForward groupManagement(HttpServletRequest request,
//			ActionMapping mapping) {
//		try {
//			createMenusForGroup(request);
//			request.setAttribute("clientENTs", getClientDAO()
//					.getClientsDropDown());
//			GroupLST groupLST = getGroupLST(request);
//			request.setAttribute("groupLST", groupLST);
//			ObjectMapper mapper = new ObjectMapper();
//			String json = "";
//			try {
//				json = mapper.writeValueAsString(groupLST.getGroupENTs());
//			} catch (JsonGenerationException e) {
//				e.printStackTrace();
//			} catch (JsonMappingException e) {
//				e.printStackTrace();
//			} catch (IOException e) {
//				e.printStackTrace();
//			}
//			json = AMSUtililies.prepareTheJSONStringForDataTable(
//					groupLST.getCurrentPage(), groupLST.getTotalItems(), json,
//					"groupID", success, error);
//			request.setAttribute("json", json);
//			if (request.getParameter("reqCodeGrid") != null
//					&& request.getParameter("reqCodeGrid").equals("gridJson"))
//				return mapping.findForward("gridJson");
//		} catch (AMSException e) {
//			e.printStackTrace();
//		}
//		MessageENT m = new MessageENT(success, error);
//		request.setAttribute("message", m);
//		return mapping.findForward("groupManagement");
//	}
//
//	private void createMenusForRole(HttpServletRequest request) {
//		List<PopupENT> popupEnts = new ArrayList<PopupENT>();
//		popupEnts.add(new PopupENT("hide-filters", "displaySearch();",
//				"Show/Hide Search", "#"));
//		popupEnts.add(new PopupENT("new-item",
//				"callAnAction(\"security.do?reqCode=roleEdit\");", "New Role",
//				"#"));
//		popupEnts
//				.add(new PopupENT("delete-item",
//						"deleteSelectedItems(\"deleteRole\");",
//						"Delete Selected", "#"));
//		List<PopupENT> popupGridEnts = new ArrayList<PopupENT>();
//		// popupGridEnts
//		// .add(new PopupENT(
//		// "edit-item",
//		// "callAnAction(\"security.do?reqCode=roleEdit&roleName=REPLACEME\");",
//		// "Edit Role", "#"));
//		popupGridEnts.add(new PopupENT("delete-item",
//				"deleteAnItem(\"REPLACEME\", \"deleteRole\");", "Remove", "#")); //
//		request.setAttribute("settingMenuItem", popupEnts);
//		request.setAttribute("gridMenuItem", popupGridEnts);
//	}
//
//	private void createMenusForGroup(HttpServletRequest request) {
//		List<PopupENT> popupEnts = new ArrayList<PopupENT>();
//		popupEnts.add(new PopupENT("hide-filters", "displaySearch();",
//				"Show/Hide Search", "#"));
//		popupEnts.add(new PopupENT("new-item",
//				"callAnAction(\"security.do?reqCode=groupEdit\");",
//				"New Group", "#"));
//		popupEnts
//				.add(new PopupENT("delete-item",
//						"deleteSelectedItems(\"deleteGroup\");",
//						"Delete Selected", "#"));
//		List<PopupENT> popupGridEnts = new ArrayList<PopupENT>();
//		popupGridEnts
//				.add(new PopupENT(
//						"edit-item",
//						"callAnAction(\"security.do?reqCode=groupEdit&groupID=REPLACEME\");",
//						"Edit Group", "#"));
//		popupGridEnts.add(new PopupENT("delete-item",
//				"deleteAnItem(REPLACEME, \"deleteGroup\");", "Remove", "#")); //
//		request.setAttribute("settingMenuItem", popupEnts);
//		request.setAttribute("gridMenuItem", popupGridEnts);
//	}
//
//	private RoleENT getRoleENT(HttpServletRequest request) {
//		RoleENT roleENT = new RoleENT();
//		if (request.getParameter("roleName") != null)
//			roleENT.setRoleName(request.getParameter("roleName"));
//		else
//			roleENT.setRoleName("");
//		roleENT.setRoleCategory(request.getParameter("roleCategory"));
//		roleENT.setRoleName(request.getParameter("roleName"));
//		roleENT.setComment(request.getParameter("comment"));
//		return roleENT;
//	}
//
//	private GroupENT getGroupENT(HttpServletRequest request) {
//		GroupENT groupENT = new GroupENT();
//		if (request.getParameter("clientID") != null)
//			groupENT.setClientID(Integer.parseInt(request
//					.getParameter("clientID")));
//		if (request.getParameter("groupID") != null)
//			groupENT.setGroupID(Integer.parseInt(request
//					.getParameter("groupID")));
//		else
//			groupENT.setGroupID(0);
//		groupENT.setGroupName(request.getParameter("groupName"));
//		groupENT.setComment(request.getParameter("comment"));
//		return groupENT;
//	}
//
//	private RoleLST getRoleLST(HttpServletRequest request) {
//		String search = request.getParameter("searchRole.roleName");
//		if (search == null)
//			search = "";
//		int pageNo = 1;
//		int pageSize = 10;
//		if (request.getParameter("currentPage") != null)
//			pageNo = Integer.parseInt(request.getParameter("currentPage"));
//		if (request.getParameter("pageSize") != null)
//			pageSize = Integer.parseInt(request.getParameter("pageSize"));
//		RoleENT roleENT = new RoleENT(search, "", search);
//		RoleLST roleLST = new RoleLST(roleENT, pageNo, pageSize, true,
//				"category_role");
//		try {
//			roleLST = getSecurityDAO().getRolesList(roleLST);
//		} catch (AMSException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		return roleLST;
//	}
//
//	private GroupLST getGroupLST(HttpServletRequest request) {
//		String search = request.getParameter("searchGroup.groupName");
//		if (search == null)
//			search = "";
//		int pageNo = 1;
//		int pageSize = 10;
//		int clientID = 0;
//		if (request.getParameter("currentPage") != null)
//			pageNo = Integer.parseInt(request.getParameter("currentPage"));
//		if (request.getParameter("pageSize") != null)
//			pageSize = Integer.parseInt(request.getParameter("pageSize"));
//		if (request.getParameter("clientID") != null
//				&& !request.getParameter("clientID").equals(""))
//			clientID = Integer.parseInt(request.getParameter("clientID"));
//		GroupENT groupENT = new GroupENT(clientID, search, search);
//		GroupLST groupLST = new GroupLST(groupENT, pageNo, pageSize, true,
//				"group_name");
//		try {
//			groupLST = getSecurityDAO().getGroupList(groupLST);
//		} catch (AMSException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		return groupLST;
//	}
//
//	// /////calls a DAO containg methods for the security management
//	private static SecurityDAOInterface getSecurityDAO() {
//		return NMMUMobileDAOManager.getSecuirtyDAOInterface();
//	}
//
//	// /////calls a DAO containg methods for the client management
//	private static ClientDAOInterface getClientDAO() {
//		return NMMUMobileDAOManager.getClientDAOInterface();
//	}
//
//	private static UserDAOInterface getUserDAO() {
//		return NMMUMobileDAOManager.getUserDAOInterface();
//	}
}