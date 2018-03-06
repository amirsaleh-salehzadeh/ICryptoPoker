<%@page import="hibernate.user.UserDAO"%>
<%@page import="common.user.UserENT"%>
<%@ page language="java" import="java.util.*"%>
<%@ page contentType="text/html; charset=UTF-8"%>
<%@taglib prefix="tiles" uri="http://struts.apache.org/tags-tiles"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<meta http-equiv="Cache-Control"
	content="no-cache, no-store, must-revalidate" />
<meta http-equiv="Pragma" content="no-cache" />
<meta http-equiv="Expires" content="0" />
<link rel="shortcut icon" href="favicon.ico" type="image/x-icon">
<meta name="viewport" content="width=device-width, initial-scale=1">
<link rel="stylesheet"
	href="css/themes/default/jquery.mobile-1.4.5.min.css">
<link rel="stylesheet" href="css/jquery-mobile/jqm-demos.css">
<link rel="stylesheet"
	href="css/jquery-mobile/jquery.dataTables.min.css">
<link rel="stylesheet" href="css/icryptopokermaincss.css">
<script src="js/nmmu.main.scripts.js"></script>
<script src="js/jquery.min.js"></script>
<script src="js/jquery.form.js"></script>
<script src="js/index.js"></script>
<script src="js/jquery.mobile-1.4.5.min.js"></script>
<script src="js/jquery.dataTables.min.js"></script>
<script src="js/dataTables.bootstrap.min.js"></script>
<script src="js/dataTables.select.min.js"></script>
<script type="text/javascript">
	function showHideMainMenu() {
		if ($("#mainMenu").css("display") == "none")
			$("#mainMenu").css("display", "block");
		else
			$("#mainMenu").css("display", "none");
	}
	$(document).ready(function() {
		$("#mainMenu").css("top", $(".jqm-header").height());
	});
</script>
<%
	UserENT u = new UserENT();
	if (request.getRemoteUser() != null) {
		UserDAO dao = new UserDAO();
		u = dao.getUserENT(new UserENT(request.getRemoteUser()));

	}
%>
<style type="text/css">
#userInfoContainer {
	color: rgb(248, 182, 36);
	position: absolute;
	right: 2;
	top: 2;
	text-shadow: none;
}

div.jqm-header>h2 {
	color: #b00000;
	text-shadow: none;
	letter-spacing: 11px;
	font-size: 66px !important;
	padding: 0 !important;
}

.mainLogoIcon {
	width: 66 !important;
	height: 66 !important;
}
</style>
</head>
<body dir="ltr">
	<div data-role="page" class="jqm-demos jqm-home">
		<div data-role="header" class="jqm-header">
			<h2>
				iCrypt <img src="images/chipLogo.png" alt="ICryptoPoker" width="66" height="66" class="mainLogoIcon"> P <img
					src="images/chipLogo.png" width="66" height="66" alt="ICryptoPoker" class="mainLogoIcon"> ker
			</h2>
			<a href="#"
				class="menu-icon jqm-navmenu-link ui-btn ui-corner-all ui-btn-left"
				onclick="showHideMainMenu()"></a>
			<%
				if (u.getUserName() != null && u.getUserName().length() > 1) {
			%>
			<span id="userInfoContainer"> Welcome <%=u.getName()%>&nbsp;<%=u.getSurName()%></span>
			<%
				}
			%>
		</div>
		<div data-role="content" id="mainBodyContents" class="jqm-content">
			<tiles:insert attribute="body" />
		</div>
		<div id="mainMenu" style="display: none;">
			<tiles:insert attribute="menu" />
		</div>
	</div>
</body>
</html>
