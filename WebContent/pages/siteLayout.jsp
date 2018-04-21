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
<link rel="stylesheet"
	href="css/themes/default/jquery.mobile.icons.min.css">
<link rel="stylesheet" href="css/themes/default/theme-classic.css">
<link rel="stylesheet"
	href="css/jquery-mobile/jquery.dataTables.min.css">
<link rel="stylesheet"
	href="css/jquery-mobile/jquery.mobile.datepicker.css">
<link rel="stylesheet"
	href="css/jquery-mobile/jquery.mobile.datepicker.theme.css">
<link rel="stylesheet" href="css/icryptopokermaincss.css">
<script src="js/icryptopokermainscripts.js"></script>
<script src="js/jquery/jquery.min.js"></script>
<script src="js/jquery.form.js"></script>
<script src="js/index.js"></script>
<script src="js/jquery/jquery.mobile-1.4.5.min.js"></script>
<script src="js/jquery.dataTables.min.js"></script>
<script src="js/dataTables.bootstrap.min.js"></script>
<script src="js/dataTables.select.min.js"></script>
<script src="js/jquery/jquery.mobile.datepicker.js"></script>
<script src="js/jquery/datepicker.js"></script>
<script type="text/javascript">
	function ShowLoadingScreen() {
		$.mobile.loading("show", {
			text : "Loading",
			textVisible : true,
			theme : "b",
			textonly : false
		});
	}
	function HideLoadingScreen() {
		$.mobile.loading("hide");
	}
</script>
<%
	UserENT u = new UserENT();
	if (request.getRemoteUser() != null) {
		UserDAO dao = new UserDAO();
		u = dao.getUserENT(new UserENT(request.getRemoteUser()));
	}
%>
</head>
<body dir="ltr">
	<div data-role="page">
		<div data-role="header" class="ui-bar">
			<h1 class="ui-title" role="heading" aria-level="1">i Crypto Poker</h1>
			<a href="#mainMenu"
				class="ui-btn-left ui-btn ui-icon-bars ui-btn-icon-notext ui-shadow ui-corner-all"></a>
		</div>
		<div data-role="content" id="mainBodyContents">
			<tiles:insert attribute="body" />
		</div>
		<!-- 		<div id="mainMenu" style="display: none;"> -->
		<%-- 			<tiles:insert attribute="menu" /> --%>
		<!-- 		</div> -->
		<div data-role="panel" id="mainMenu" data-display="overlay">
			<tiles:insert attribute="menu" />
		</div>
	</div>
</body>
</html>
