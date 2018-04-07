<%@page import="hibernate.security.SecurityDAO"%>
<%@page import="java.util.List"%>
<%@page import="common.PopupENT"%>
<%@page import="java.util.ArrayList"%>
<%@taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib prefix="logic" uri="/WEB-INF/struts-logic.tld"%>
<%@taglib prefix="ams" uri="/WEB-INF/AMSTag.tld"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<script>
	$(document).ready(function() {
		refreshPlaceHolders();
		refreshGrid();
	});
</script>
</head>
<body>
<body>

	<html:form styleId="dataFilterGridMainPage" action="payment.do">
		<ams:message messageEntity="${message}"></ams:message>
		<input type="hidden" name="reqCode" id="reqCode"
			value="paymentManagement"> <input type="hidden"
			name="reqCodeGrid" id="reqCodeGrid" value="">
		<div class="ui-grid-solo" id="searchFilters">
			<html:text name="saleLST" property="searchSale.username"
				onkeyup="refreshGrid();" title="Username"></html:text> 
		</div>
		<div class="ui-grid-solo">
			<bean:define id="totalRows" name="saleLST" property="totalItems"
				type="java.lang.Integer"></bean:define>
			<bean:define id="first" name="saleLST" property="first"
				type="java.lang.Integer"></bean:define>
			<bean:define id="currentPage" name="saleLST"
				property="currentPage" type="java.lang.Integer"></bean:define>
			<bean:define id="pageSize" name="saleLST" property="pageSize"
				type="java.lang.Integer"></bean:define>
			<bean:define id="totalPages" name="saleLST" property="totalPages"
				type="java.lang.Integer"></bean:define>

			<ams:ajaxPaginate currentPage="<%=currentPage%>"
				pageSize="<%=pageSize%>" totalRows="<%=totalRows%>" align="center"
				columns="creatorUsername,DT_RowId,username,dateTime,currency,amount,status,paymentMethod"
				popupID="saleManagementSettingMenu"
				popupGridSettingItems="${gridMenuItem}"
				popupMenuSettingItems="${settingMenuItem}">
				<table id="gridList" class="display cell-border dt-body-center"
					cellspacing="0" width="100%">
					<thead>
						<tr>
							<th><input type="checkbox" id="checkAllHead"></th>
							<th data-priority="1">creatorUsername</th>
							<th data-priority="2">username</th>
							<th data-priority="3">dateTime</th>
							<th data-priority="4">currency</th>
							<th data-priority="5">amount</th>
							<th data-priority="6">status</th>
							<th data-priority="7">paymentMethod</th>
						</tr>
					</thead>
					<tfoot>
						<tr>
							<th><input type="checkbox" id="checkAllFoot"></th>
							<th data-priority="1">creatorUsername</th>
							<th data-priority="2">username</th>
							<th data-priority="3">dateTime</th>
							<th data-priority="4">currency</th>
							<th data-priority="5">amount</th>
							<th data-priority="6">status</th>
							<th data-priority="7">paymentMethod</th>
						</tr>
					</tfoot>
				</table>
			</ams:ajaxPaginate>
		</div>
	</html:form>
</body>
</html>