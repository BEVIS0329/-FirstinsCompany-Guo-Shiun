<%@ page contentType="text/html; charset=Big5"%>
<%@ page import="com.hitrust.b2ctoolkit.b2cpay.*"%>
<%
	B2CPayAuthTrxToken auth = new B2CPayAuthTrxToken();
	auth.setStoreId("88168");
	String ordernumber = "" + System.currentTimeMillis();
	ordernumber = (String) request.getParameter("ordernumber");
	auth.setOrderNo(ordernumber);
	//auth.setOrderDesc(new String(((String)request.getParameter("orderdesc")).getBytes("8859_1"), "Big5"));
	String orderDesc = request.getParameter("orderdesc");
	auth.setOrderDesc(orderDesc);
	String amount = "1";
	amount = (String) request.getParameter("amount");
	auth.setAmount(String.valueOf((Integer.parseInt(amount) * 100)));
	auth.setTicketNo((String) request.getParameter("ticketno"));
	//auth.setDepositFlag("1");
	String trxToken = "607013ac98d4f5dbb19e233dc0a559d2c7a5547c";
	trxToken = (String) request.getParameter("trxToken");
	auth.setTrxToken(trxToken);
	String expiry = "1701";
	expiry = (String) request.getParameter("expiry");
	auth.setExpiry(expiry);
	String cvv2 = "123";
	cvv2 = (String) request.getParameter("cvv2");
	auth.setE01(cvv2);
	auth.transaction();
	if (auth.getRetCode().equals("00")) {
		System.out.println("auth.getToken()" + auth.getToken());
		response.sendRedirect(auth.getToken());
	} else
		out.print(auth.getRetCode());
%>