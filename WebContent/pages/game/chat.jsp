<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
<script type="text/javascript">
  var websocket = new WebSocket("ws://localhost:8080/ICryptoPoker/TableWebservice") ;
  websocket.onmessage = function processMessage(message){
	  var json = JSON.parse(message.data) ;
	  if(json.message!=null){
		  messagesTextArea.value+=json.message+ "/n"
	  }
	  
	  function sendMessage(){
		  websocket.send(messageText.value) ;
	  }
  }

</script>

</head>
<body>
  <textarea  id= "messagesTextArea" readonly ="readonly"  rows="10" cols="45"></textarea>
  <input type ="text" id = "messageText" size ="50"/> <input type= "button" value ="send" onclick= "sendMessage()"/>
</body>
</html>