<%@ page contentType="text/html; charset=Big5" %>
<%@ page import= "com.hitrust.b2ctoolkit.b2cpay.*;" %>
<%
	B2CPost auth = new B2CPost();
	auth.setStoreId("41147");
	auth.setOrderNo((String)request.getParameter("ordernumber"));
	auth.setOrderDesc((String)request.getParameter("orderdesc"));
	auth.setAmount((String)request.getParameter("amount"));
	auth.setE17((String)request.getParameter("payexpireday"));
	auth.setType("11");

	auth.transaction();
	if (auth.getRetCode().equals("00"))
	  response.sendRedirect(auth.getToken());
	else
	  out.print(auth.getRetCode());

%>