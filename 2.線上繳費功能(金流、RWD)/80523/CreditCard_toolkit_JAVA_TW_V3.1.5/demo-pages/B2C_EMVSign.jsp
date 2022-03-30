<%@ page contentType="text/html; charset=Big5" %>
<%@ page import= "com.hitrust.b2ctoolkit.b2cpay.*;" %>
<%
	B2CPayEMVSign auth = new B2CPayEMVSign();
	auth.setStoreId("41157");
	auth.setOrderNo((String)request.getParameter("ordernumber"));
	auth.setOrderDesc(new String(((String)request.getParameter("orderdesc")).getBytes("8859_1"), "Big5"));
	auth.setAmount((String)request.getParameter("amount"));
	//auth.setDepositFlag("1");
	
	auth.setE22((String)request.getParameter("EMVSignID"));
	auth.setE23((String)request.getParameter("CAPToken"));
	auth.setE24((String)request.getParameter("ChallengeCode"));
	auth.transaction();
	
	if (auth.getRetCode().equals("00"))
	  response.sendRedirect(auth.getToken());
	else
	  out.print(auth.getRetCode());

%>