<%@page import="game.poker.holdem.domain.BlindLevel"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>


<style>
div {
	border: 1px solid white !important;
}
</style>
<!-- 	HEADER INFO -->


<div class="ui-block-solo ui-grid-c">
	<div class="ui-block-a">chips</div>
	<div class="ui-block-b">money</div>
	<div class="ui-block-c">name</div>
	<div class="ui-block-d">surname</div>

</div>


<!-- MIDDLE PANEL  -->


<div class="ui-block-solo ui-grid-b">

	<!-- LEFT GRID  -->

	<div class="ui-block-a">
		<div class="ui-block-solo">leftT</div>
		<div class="ui-block-solo">leftM</div>
		<div class="ui-block-solo">leftB</div>
	</div>

	<!-- CENTER GRID  -->

	<div class="ui-block-b">
		<div class="ui-block-solo">
			<div class="ui-grid-b">
				<div class="ui-block-a">T1</div>
				<div class="ui-block-b">T2</div>
				<div class="ui-block-c">T3</div>
			</div>
		</div>
		<div class="ui-block-solo">middleTable</div>
		<div class="ui-block-solo">
			<div class="ui-grid-b">
				<div class="ui-block-a">B1</div>
				<div class="ui-block-b">B2</div>
				<div class="ui-block-c">B3</div>
			</div>
		</div>
	</div>

	<!-- RIGHT GRID  -->

	<div class="ui-block-c">
		<div class="ui-block-solo">rightTop</div>
		<div class="ui-block-solo">rightMid</div>
		<div class="ui-block-solo">RightB</div>
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