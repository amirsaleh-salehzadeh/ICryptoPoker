<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@taglib prefix="ams" uri="/WEB-INF/AMSTag.tld"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<script src="js/icryptopokermainscripts.js"></script>
</head>
<body>
	<div id='formContainer'>
		<ams:message messageEntity="${message}"></ams:message>
		<html:form styleId="dataFilterGridMainPage" action="sale.do" method="post">
			<input type="hidden" name="reqCode" value="saveUpdateSale">
			<div class="ui-block-solo">

				<%
					boolean view = ((request.getParameter("reqCode").equals("saleView")) ? true : false);
						if (view) {
				%>

				<bean:write name="saleENT" property="username" />
				<%
					} else {
				%>
				<label>Username</label>
				<html:text name="saleENT" property="username" title="UserName" />
				<%
					}
				%>
			</div>

			<div class="ui-block-solo">
				<label>Status</label>
				<html:text name="saleENT" property="status" styleId="status"
					title="Status" />
			</div>
			
			<div class="ui-block-solo">
				<label>Bank Response</label>
				<html:textarea name="saleENT" property="bankResponse"
					styleId="bankResponse" title="Bank Response" />
			</div>

			<div class="ui-block-solo">
				<label>Payment Method</label>
				<html:textarea name="saleENT" property="paymentMethod"
					styleId="paymentMethod" title="paymentMethod" />
			</div>

			<div class="ui-block-solo">
				<label>Amount</label>
				<html:textarea name="saleENT" property="amount" styleId="amount"
					title="Amount" />
			</div>
			<div class=ui-grid-a>
				<div class=ui-block-a>
					<a href="#" data-role="button" data-mini="true" class="cancel-icon"
						onclick="callAnAction('payment.do?reqCode=saleManagement');">Cancel</a>
				</div>
				<div class=ui-block-b>
					<a href="#" data-role="button" class="save-icon" data-mini="true"
						onclick='$("#dataFilterGridMainPage").submit();'>Save</a>
				</div>
			</div>
		</html:form>
	</div>
</body>

</html>