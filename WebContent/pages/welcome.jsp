<%@page import="game.poker.holdem.domain.Player"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta name="viewport"
	content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=0" />
<!--320-->
<link rel="shortcut icon" href="favicon.ico" type="image/x-icon">
<link rel="stylesheet"
	href="css/themes/default/jquery.mobile.icons.min.css">
<link rel="stylesheet" href="css/themes/default/theme-classic.css">
<link rel="stylesheet"
	href="css/themes/default/jquery.mobile.structure-1.4.5.min.css">
<script src="js/jquery/jquery.min.js"></script>
<script src="js/jquery/jquery.mobile-1.4.5.min.js"></script>
<%
	Player player = (Player) request.getAttribute("player");
%>
</head>
<body>
	<div data-role="page">
		<ul data-role="listview" data-inset="true">
			<li><a href="t_game.do" data-ajax="false"><img
					src="images/game/stack.png">
					<h2>i Crypto Poker</h2>
					<p>Broken Bells</p></a></li>
			<li><a href="t_user.do?reqCode=userManagement" data-ajax="false"><img
					src="images/game/user.png">
					<h2>User Management</h2>
					<p>Hot Chip</p></a></li>
			<li><a href="t_payment.do?reqCode=paymentManagement"
				data-ajax="false"><img src="images/game/payment.png">
					<h2>Payment Management</h2>
					<p></p></a></li>
			<li><a href="t_sale.do?reqCode=saleManagement"
				data-ajax="false"><img src="images/game/sale.png">
					<h2>Sale Management</h2>
					<p></p></a></li>
					<li><a href="logOut.do"
				data-ajax="false"><img src="images/icons/undo.png">
					<h2>Logout</h2>
					<p></p></a></li>
		</ul>
	</div>
</body>
</html>