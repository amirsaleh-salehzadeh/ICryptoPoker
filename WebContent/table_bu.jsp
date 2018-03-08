<%@page import="game.poker.holdem.domain.BlindLevel"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<link rel="stylesheet" href="css/game/lobby.holdem.css" />
<script src="js/lobby/lobby.js"></script>
<html lang="en" ng-app="pokerApp">
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1">
<link href="js/bootstrap/dist/css/bootstrap.min.css" rel="stylesheet">
<!-- <link href="poker-template.css" rel="stylesheet"> -->

<script src="js/lobby/firebase.js"></script>
<script src="js/lobby/angular.min.js"></script>
<script src="js/lobby/app.js"></script>
<script src="js/lobby/controllers.js"></script>

</head>
<body>


	<div class="ui-block-solo ui-grid-c">
		<div class="ui-block-a">chips</div>
		<div class="ui-block-b">money</div>
		<div class="ui-block-c">name</div>
		<div class="ui-block-d">surname</div>

	</div>
	<!-- middle -->
	<div class="ui-block-solo ui-grid-b">
		<div class="ui-block-a">
			<div class="ui-grib-b">
				<div class="ui-block-a">25</div>
				<div class="ui-block-a">25</div>
				<div class="ui-block-a">25</div>
			</div>
		</div>
		<div class="ui-block-b">25</div>
		<div class="ui-block-c">
			<div class="ui-grib-b">
				<div class="ui-block-c">25</div>
				<div class="ui-block-c">25</div>
				<div class="ui-block-c">25</div>
			</div>

		</div>
	</div>
	<!-- bottom -->
	<div class=" ui-block-solo ui-grid-c">
		<div class="ui-block-a">footer</div>
		<div class="ui-block-b">footer</div>
		<div class="ui-block-c">footer</div>
		<div class="ui-block-d">footer</div>
	</div>
	================================================== -->
	<!-- Placed at the end of the document so the pages load faster -->
	<script src="js/bootstrap/dist/js/bootstrap.min.js"></script>
</body>
</html>
