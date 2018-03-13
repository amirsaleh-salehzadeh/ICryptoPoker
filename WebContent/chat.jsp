<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
<script src="js/jquery.min.js"></script>

<script type="text/javascript">

var websocket;
function start() {
    var uri = "ws://localhost:8080/ICryptoPoker/chat/"+$("#sID").val(); // ws -> not secure, wss (ssl)
    websocket = new WebSocket(uri);
    // override Methode: when recive msg from server.
    websocket.onmessage = function (msg) {
      console.log("msg recived from server: " + msg.data);
      
    }
    // override Methode: when server throw an error.
    websocket.onerror = function (evt) {
      console.log("error from server: Can't establish a connection to the server!");
      console.info(evt);
    }
    // override Methode: when server close the connection.
    websocket.onclose = function () {
      console.log("Server close connection.");
	  
    }
    // override Methode: when websocket connection was established
    websocket.onopen = function () {
      // send a msg to server
      websocket.send("getID");
      $("#sID").val(websocket.toString());
    }
    
  }
  function sendMessage(){
	  websocket.send($("#msg").val()) ;//this wasnt a genreal variable firstly
  }
</script>

</head>
<body>
  <textarea  id= "msg" rows="10" cols="45"></textarea>
 
  <br/>
 TO <input type ="text" id = "rID" size ="50"/> <br/>
 FROM <input type ="text" id = "sID" size ="50"/> <br/>
  
  <input type= "button" value ="send" onclick="sendMessage()"/>
  <input type= "button" value ="start" onclick="start()"/>
</body>
</html>