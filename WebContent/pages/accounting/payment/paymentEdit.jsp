<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@taglib prefix="ams" uri="/WEB-INF/AMSTag.tld"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
</head>
<body>
	<div id='formContainer'>
		<ams:message messageEntity="${message}"></ams:message>
		<form id="dataFilterGridMainPage" action="payment.do"
			autocomplete="off">
			<input type="hidden" name="reqCode" value="saveUpdatePayment">
			<div class="ui-block-solo">
				<%
					boolean view = ((request.getParameter("reqCode").equals("paymentView")) ? true : false);
					if (view) {
				%>
				<bean:write name="paymentENT" property="username"  />
				<%
					} else {
				%>
				<html:text name="paymentENT" property="username" title="UserName" />
				<%
					}
				%>
			</div>

			<div class="ui-block-solo">
				<html:text name="paymentENT" property="status" styleId="status"
					title="Status" />
			</div>

			<div class="ui-block-solo">
				<html:textarea name="paymentENT" property="reason" styleId="reason"
					title="Reason" />
			</div>

			<div class="ui-block-solo">
				<html:textarea name="paymentENT" property="bankResponse"
					styleId="bankResponse" title="Bank Response" />
			</div>

			<div class="ui-block-solo">
				<html:textarea name="paymentENT" property="currency"
					styleId="currency" title="Currency" />
			</div>

			<div class="ui-block-solo">
				<html:textarea name="paymentENT" property="amount" styleId="amount"
					title="Amount" />
			</div>
			<div class=ui-grid-a>
				<div class=ui-block-a>
					<a href="#" data-role="button" data-mini="true" class="cancel-icon"
						onclick="callAnAction('payment.do?reqCode=paymentManagement');">Cancel</a>
				</div>
				<div class=ui-block-b>
					<a href="#" data-role="button" class="save-icon" data-mini="true"
						onclick="saveTheForm();">Save</a>
				</div>
			</div>
		</form>
	</div>
</body>

</html>