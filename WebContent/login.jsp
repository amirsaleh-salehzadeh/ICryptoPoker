<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<title>iCryptoPoker</title>
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
<link rel="stylesheet" href="css/login.css">
<link rel="stylesheet" href="css/user/passtrength.css" media="screen"
	title="no title">
<script type="text/javascript" src="js/user/jquery.passtrength.js"></script>

<script type="text/javascript">
	function registerNew() {
		$("#messageSuccess").fadeOut(1000);
		$("#reTypePassDiv").fadeOut(1000);
		// 		if ($("#reTypePassDiv").css("display") == "none") {
		$("#reTypePassDiv").fadeIn(1000);
		// 			return;
		// 		}
		if ($("#uNameEmail").val() == "") {
			$("#message").html("Please insert the username.");
			return;
		}
		if ($("#loginRePass").val() != $("#loginPass").val()) {
			$("#message").hide();
			$("#message")
					.html("Password and Re-type of password do not match.");
			$("#message").fadeIn(2000);
			return;
		}

		var url = "/ICryptoPoker/REST/GetGeneralServiceWS/RegisterNew?userName="
				+ $("#uNameEmail").val()
				+ "&userPassword="
				+ $("#loginPass").val();
		$.ajax({
			url : url,
			cache : false,
			async : false,
			dataType : 'json',
			contentType : 'application/json; charset=utf-8',
			success : function(data) {
				$("#messageSuccess").html("Registered Successfully");
				$("#messageSuccess").fadeIn(2000);
				$("#message").hide();
			},
			error : function(xhr, resp, text) {
				$("#message").hide();
				$("#message").html(xhr.responseText);
				$("#message").fadeIn(2000);
			}
		});
	}

	$(document).ready(function($) {
		$('#loginPass').passtrength({
			minChars : 8,
			passwordToggle : true,
			tooltip : true,
			eyeImg : "css/themes/default/images/icons-png/eye-white.png"
		});
		// 		$('#uNameEmail').passtrength({
		// 			minChars : 3,
		// 			maxChars : 3,
		// 			passwordToggle : false,
		// 			tooltip : true
		// 		});
	});
</script>
</head>
<body>
	<div id="loginContents" class="ui-bar-a ui-corner-all">
		<div class="ui-block-solo" style="text-align: center; width: 100%;">
			<h4>LOGIN / REGISTER</h4>
		</div>

		<form name="loginForm" method="POST" action="j_security_check">
			<div class="ui-block-solo">
				<input type="text" name="j_username" id="uNameEmail" value=""
					placeholder="Username" class="inptxt ui-mini" />
			</div>
			<div class="ui-block-solo">
				<input type="password" name="j_password" id="loginPass" value=""
					placeholder="Password" class="inptxt ui-mini ui-corner-all" />
			</div>
			<div class="ui-block-solo" id="reTypePassDiv"
				style="display: none; text-align: center;">
				<input type="password" name="loginPass" id="loginRePass" value=""
					placeholder="Re-Type Password" />
			</div>
			<div class="ui-grid-a ui-block-solo">
				<div class="ui-block-a">
					<input type="submit" id="loginBtn" value="Login" />
				</div>
				<div class="ui-block-b">
					<input type="button" id="registerBtn" onclick="registerNew()"
						value="Register" />
				</div>
			</div>
			<div class="ui-block-solo" style="text-align: center; width: 100%;">
				<span id="message"
					style="color: #b00000; text-align: center; width: 100%;"> <%
 	if (request.getParameter("errMsg") != null)
 		out.print("Invalid Username or Password");
 %>
				</span> <span id="messageSuccess"
					style="color: #42A72A; text-align: center; width: 100%;"></span>
			</div>
		</form>
	</div>
</body>
</html>