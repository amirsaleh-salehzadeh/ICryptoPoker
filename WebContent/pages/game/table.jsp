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
<body ng-controller="PlayerListCtrl">
	<div>
		<div>
			<div class="pokerRoom"
				ng-class="{preFlop:table.gameStatus==0, final:table.gameStatus==4}">
				<div class="alert alert-warning"
					ng-class="{active:table.seats[myPosition].turn==true || alert != ''}">
					<div ng-class="{active:alert != ''}">{{alert}}</div>
					<div ng-class="{active:table.seats[myPosition].turn==true}">
						It's your turn <span ng-class="{active:table.currentBet != 0}">
							- The current bet is {{table.currentBet}}</span> <span
							class="timer pull-right">{{table.countdown}} seconds</span>
					</div>
				</div>

				<div class="pokerTable center-block">
					<!-- TO TEST HAND STRENGTH, CARDS DEALT ADD CLASS 'final' TO 'pokerCards' -->
					<!-- =================================================================== -->
					<ol class="pokerCards center-block list-unstyled"
						ng-class="{myTurn:table.seats[myPosition].turn==true,
							cardPreFlop:table.gameStatus==0,
							cardFlop:table.gameStatus==1,
							cardTurn:table.gameStatus==2,
							cardRiver:table.gameStatus==3,
							final:table.gameStatus==4}">
						<li ng-repeat="card in table.cards">
							{{table.cards[$index].cardNum}}<i
							class="symbol {{table.cards[$index].cardSuit}}"></i>
						</li>
					</ol>
					<div class="pokerPot" ng-class="{display:table.pot!=0}">
						{{table.pot}}<i class="icon coins"></i>
					</div>
				</div>
				<!-- End Poker Table -->
				<ol class="pokerPlayers list-unstyled list-inline center-block">
					<li ng-repeat="seat in table.seats"
						ng-class="{me:seat.playerId==table.seats[myPosition].playerId,
							winner:seat.winner==true,
							folded:seat.fold==true,
							currentPlayer:seat.turn==true,
							dealerButton:seat.button==true,
							smallBlind:seat.blind=='small',
							bigBlind:seat.blind=='big',
							empty:seat.dead==true}">
						<i class="icon more_items"></i> <img src="images/game/user.png"
						alt="{{seat.name}}" class="img-circle img-player">
						<ol class="pokerCards showHand list-unstyled">
							<li>{{seat.hand[0].cardNum}}<i
								class="symbol {{seat.hand[0].cardSuit}}"></i></li>
							<li>{{seat.hand[1].cardNum}}<i
								class="symbol {{seat.hand[1].cardSuit}}"></i></li>
						</ol> <span class="bet bottom-seat"
						ng-class="{display:(seat.id==myPosition && seat.currentBet != 0 && seat.callBetRaise == true) || seat.currentBet != 0}">
							{{seat.currentBet}} </span> <span class="name">{{seat.name}} -
							{{seat.playerId}}</span> <span class="chips">{{seat.chips}}<i
							class="icon coins"></i></span> <span class="bet"
						ng-class="{display:(seat.id==myPosition && seat.currentBet != 0 && seat.callBetRaise == true) || seat.currentBet != 0}">
							{{seat.currentBet}} </span> <img src="images/game/user.png"
						alt="{{seat.name}}" class="img-circle img-player bottom-seat">
					</li>
				</ol>
				<div class="row">
					<div class="pokerChat col-md-5">
						<div class="chatRoom well"></div>
						<form role="form">
							<div class="form-group">
								<div class="col-sm-9">
									<input type="text" class="form-control" id="chatBox"
										placeholder="">
								</div>
								<button type="submit" class="btn btn-default col-sm-3">Send</button>
							</div>
						</form>
					</div>
					<div class="col-md-4">
						<ol class="pokerCards myHand center-block list-unstyled"
							ng-class="{folded:table.seats[myPosition].fold==true}">
							<li>{{table.seats[myPosition].hand[0].cardNum}}<i
								class="symbol {{table.seats[myPosition].hand[0].cardSuit}}"></i></li>
							<li>{{table.seats[myPosition].hand[1].cardNum}}<i
								class="symbol {{table.seats[myPosition].hand[1].cardSuit}}"></i></li>
						</ol>
					</div>
					<div class="pokerController col-md-3">
						<p class="blinds pull-right">Blinds:
							{{table.smallBlind}}/{{table.smallBlind * 2}}</p>
						<form class="pull-right" role="form">
							<div class="form-group">
								<button id="checkBtn" class="btn btn-success"
									ng-click="table.seats[myPosition].actionTaken=true;"
									ng-disabled="table.seats[myPosition].fold==true || table.seats[myPosition].turn==false">Check</button>
								<button id="betBtn" class="btn btn-warning"
									ng-disabled="table.seats[myPosition].fold==true || table.seats[myPosition].turn==false"
									ng-click="table.seats[myPosition].actionTaken=true;
										table.seats[myPosition].currentBet=table.seats[myPosition].bet;
										table.seats[myPosition].chips=table.seats[myPosition].chips-table.seats[myPosition].currentBet;
										table.seats[myPosition].bet='';">Bet</button>
								<button id="callBtn" class="btn btn-success"
									ng-disabled="table.seats[myPosition].fold==true || table.seats[myPosition].turn==false"
									ng-click="table.seats[myPosition].actionTaken=true;
										table.seats[myPosition].currentBet=table.currentBet;
										table.seats[myPosition].chips=table.seats[myPosition].chips-table.seats[myPosition].currentBet;
										table.seats[myPosition].bet='';">Call</button>
								<button id="raiseBtn" class="btn btn-warning"
									ng-disabled="table.seats[myPosition].fold==true || table.seats[myPosition].turn==false"
									ng-click="table.seats[myPosition].actionTaken=true;
									table.seats[myPosition].currentBet=table.currentBet * 2;
									table.currentBet=table.seats[myPosition].currentBet;
									table.seats[myPosition].chips=table.seats[myPosition].chips-table.seats[myPosition].currentBet;
									">Raise</button>
								<button id="foldBtn" class="btn btn-danger"
									ng-disabled="table.seats[myPosition].fold==true || table.seats[myPosition].turn==false"
									ng-click="table.seats[myPosition].fold=true">Fold</button>
							</div>
							<div class="form-group">
								<label for="amount" class="control-label">Bet/Raise
									Amount</label> <input type="text" class="form-control" id="amount"
									placeholder="Enter Bet" ng-model="table.seats[myPosition].bet">
							</div>
						</form>
					</div>
				</div>
			</div>
			<!-- End Poker Room -->

		</div>

	</div>
	<!-- /.container -->

	<!-- Bootstrap core JavaScript
    ================================================== -->
	<!-- Placed at the end of the document so the pages load faster -->
	<script src="js/bootstrap/dist/js/bootstrap.min.js"></script>
</body>
</html>
