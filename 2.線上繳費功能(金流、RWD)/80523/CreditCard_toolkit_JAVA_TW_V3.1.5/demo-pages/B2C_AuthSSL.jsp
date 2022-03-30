<%@ page contentType="text/html; charset=Big5" %>
<%@ page import= "com.hitrust.b2ctoolkit.b2cpay.*,java.net.*;" %>
<%
	B2CPayAuthSSL auth = new B2CPayAuthSSL();
	auth.setStoreId("60000");
	auth.setOrderNo("20101108001");
	auth.setOrderDesc("test");
	auth.setAmount("12000");
	auth.setQueryFlag("1");
	auth.setPan("8801200700001001");
	auth.setExpiry("1703");
	auth.setE01("678");
	auth.setE04("1");

	auth.transaction();
	if (auth.getRetCode().equals("00")){
			out.print("getRedemordernum="+auth.getRedemordernum());
			out.print("getRedem_discount_point="+auth.getRedem_discount_point());
			out.print("getRedem_discount_amount="+auth.getRedem_discount_amount());
			out.print("getRedem_purchase_amount="+auth.getRedem_purchase_amount());
			out.print("getRedem_balance_point="+auth.getRedem_balance_point());
	}else{
	  out.print(auth.getRetCode());
	}

%>
