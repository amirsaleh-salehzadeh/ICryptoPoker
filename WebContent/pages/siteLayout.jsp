<%@page import="game.poker.holdem.domain.Player"%>
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
	href="css/themes/default/jquery.mobile.icons.min.css">
<link rel="stylesheet" href="css/themes/default/theme-classic.css">
<link rel="stylesheet"
	href="css/themes/default/jquery.mobile.structure-1.4.5.min.css">
<!-- <link rel="stylesheet" -->
<!-- 	href="css/themes/default/jquery.mobile-1.4.5.min.css"> -->
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
<script src="js/jquery/jquery.js"></script>
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
			textonly : false,
			html: "html"
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
	<%
		Player p = (Player) request.getAttribute("player");
	%>
	<div data-role="page">
		<div data-role="header" class="ui-bar" >
<!-- 		data-position="fixed" -->
			<a href="#mainMenu" data-role="button" role="button"
				class="ui-btn ui-btn-left ui-icon-bars ui-btn-icon-notext ui-shadow ui-corner-all"></a>
			<h1 aria-level="1" role="heading" class="ui-title">i Crypto
				Poker</h1>

			<a href="#playerPopupMenu" data-rel="popup" data-transition="slideup"
				class="ui-btn-right ui-btn ui-icon-user ui-btn-icon-notext ui-btn-right ui-shadow ui-corner-all"
				data-role="button" role="button"></a>
			<div data-role="popup" id="playerPopupMenu" data-theme="a">
				<ul data-role="listview" data-inset="true" style="min-width: 210px;">
					<li data-role="list-divider"><%=p.getId()%></li>
					<li>$<%=p.getTotalChips()%></li>
				</ul>
			</div>
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
