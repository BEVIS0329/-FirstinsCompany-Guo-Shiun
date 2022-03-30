<%@ page contentType="text/html; charset=Big5" %>
<%@ page import= "com.hitrust.b2ctoolkit.b2cpay.*,java.net.*;" %>
<%
	B2CPayAuth auth = new B2CPayAuth();
	auth.setStoreId("88025");
	auth.setOrderNo((String)request.getParameter("ordernumber"));
	auth.setOrderDesc(new String(((String)request.getParameter("orderdesc")).getBytes("8859_1"), "Big5"));
	//auth.setOrderDesc((String)request.getParameter("orderdesc"));
	auth.setAmount((String)request.getParameter("amount"));
	//auth.setQueryFlag("1");
	//auth.setDepositFlag("1");
	//auth.setUpdateURL("http://192.168.120.34:8080/toolkit/B2C_Return.jsp");
	//auth.setReturnURL("http://192.168.120.34:8080/toolkit/B2C_Return.jsp");
  //travel card
	//auth.setE11("");
	//auth.setE12("");
	//auth.setE13("");
	
	auth.transaction();
	if (auth.getRetCode().equals("00"))
			response.sendRedirect(auth.getToken());
	else
	  out.print(auth.getRetCode());


%>