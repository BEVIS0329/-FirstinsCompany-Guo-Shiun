<%@ page contentType="text/html; charset=Big5" %>
<%@ page import= "com.hitrust.b2ctoolkit.b2cpay.*,java.net.*;" %>
<%
	
	String email = request.getParameter("email");
	String toStatus = request.getParameter("toStatus");

	String orderId = request.getParameter("orderId");
	String token = request.getParameter("token");
	
	// =========================================================

	//java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy/MM/dd");

	B2CEasyPayUpdate update = new B2CEasyPayUpdate("XXXXX");
	
	update.setOrderId(orderId);
	update.setToken(token);
	update.setEmail(email);
	update.changeStatusTo(Integer.parseInt(toStatus));
	
	update.transaction();

	
	out.println("rtn code: "+update.getRetCode()+"<br/>");
	out.println("====TestB2CEasyPayUpdate finished====<br/>");

%>