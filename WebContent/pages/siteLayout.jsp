<%@page import="game.poker.holdem.domain.BlindLevel"%>
<%@page import="game.poker.holdem.dao.PlayerDaoImpl"%>
<%@page import="game.poker.holdem.domain.Player"%>
<%@page import="hibernate.user.UserDAO"%>
<%@page import="common.user.UserENT"%>
<%@ page language="java" import="java.util.*"%>
<%@ page contentType="text/html; charset=UTF-8"%>
<%@taglib prefix="tiles" uri="http://struts.apache.org/tags-tiles"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<meta name="theme-color" content="#222222">
<meta http-equiv="Cache-Control"
	content="no-cache, no-store, must-revalidate" />
<meta http-equiv="Pragma" content="no-cache" />
<meta http-equiv="Expires" content="0" />
<link rel="shortcut icon" href="favicon.ico" type="image/x-icon">
<meta name="viewport"
	content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=0" />
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
<script src="js/jquery/jquery.min.js"></script>
<script src="js/jquery.form.js"></script>
<script src="js/index.js"></script>
<script src="js/jquery/jquery.mobile-1.4.5.min.js"></script>
<script src="js/jquery.dataTables.min.js"></script>
<script src="js/dataTables.bootstrap.min.js"></script>
<script src="js/dataTables.select.min.js"></script>
<script src="js/jquery/jquery.mobile.datepicker.js"></script>
<script src="js/jquery/datepicker.js"></script>
<script src="js/icryptopokermainscripts.js"></script>
<%
	Player p = (Player) request.getAttribute("player");
	if (request.getRemoteUser() != null || p == null) {
		PlayerDaoImpl pl = new PlayerDaoImpl();
		p = pl.findById(request.getRemoteUser(), null);
	}
%>
</head>
<body dir="ltr">
	<div data-role="page">
		<div data-role="header" class="ui-bar">
			<!-- 		data-position="fixed" -->
			<a href="#mainMenu" data-role="button" role="button"
				class="ui-btn ui-btn-left ui-icon-bars ui-btn-icon-notext ui-shadow ui-corner-all"></a>
			<h1 aria-level="1" role="heading" class="ui-title">i Crypto
				Poker</h1>
			<a href="#playerPopupMenu" data-rel="popup" data-transition="slideup"
				class="ui-btn-right ui-btn ui-icon-user ui-btn-icon-notext ui-btn-right ui-shadow ui-corner-all"
				data-role="button" role="button"></a>
		</div>
		<div data-role="content" id="mainBodyContents">
			<tiles:insert attribute="body" />
		</div>
		<div data-role="panel" id="mainMenu" data-display="overlay">
			<tiles:insert attribute="menu" />
		</div>
		<div data-role="popup" id="playerPopupMenu" data-theme="a">
			<ul data-role="listview" data-inset="true" style="min-width: 210px;">
				<li data-role="list-divider"><%=p.getId()%></li>
				<li>$<%=p.getTotalChips()%></li>
				<li><a href="#" onclick="openCreateNewGame();" data-rel="popup"
					data-position-to="window" data-transition="pop" data-role="button"
					role="button" class="ui-btn ui-mini ui-shadow-icon ui-corner-all">New
						Game</a></li>
			</ul>
		</div>
	</div>
</body>
</html>
