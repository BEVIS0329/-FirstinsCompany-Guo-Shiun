<%@ page contentType="text/html; charset=Big5" %>
<%@ page import= "com.hitrust.b2ctoolkit.b2cpay.*,java.net.*;" %>
<%
	
	String storeId = "XXXXX";
	String email = request.getParameter("email");
	String orderName = request.getParameter("orderName");
	String amount = request.getParameter("amount");

	String startDate = request.getParameter("startDate");
	String endDate = request.getParameter("endDate");;
	
	String  orderdesc = request.getParameter("orderdesc");;
	
	String updateURL = request.getParameter("updateURL");
	String installmentPlans = request.getParameter("intallmentpay2");
	String noticeMail = request.getParameter("noticeMail");
	String depositflag = request.getParameter("depositflag");
	// =========================================================

	java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy/MM/dd");

	B2CEasyPayCreator creator = new B2CEasyPayCreator(storeId);
	
	creator.setFromEncoding(null);
	creator.setEmailAddress(email);
	creator.setOrderName(orderName);
	creator.setAmount(amount);
	creator.setInstallmentPlans(installmentPlans);
	creator.setUpdateURL(updateURL);
	creator.setNoticeMail(noticeMail);
	// just a simple check for set or not set available date
	// you shall check endDate also
	if(startDate!=null &&
			startDate.length()>8){
		creator.setAvailableDate(
				sdf.parse(startDate),
				sdf.parse(endDate));
	}
	creator.setDepositFlag(depositflag);
	creator.setOrderDesc(orderdesc);
	
	creator.transaction();

	out.println("rtn code: " + creator.getRetCode()+"<br/>");
	out.println("url: " + creator.getEasyPayLink()+"<br/>");
	java.util.ArrayList list = creator.getEasyPayDetailList();
	for(int i=0;i<list.size();i++){
		B2CEasyPayDetail ep = (B2CEasyPayDetail) list.get(i);
		out.println("<hr>");
		out.println("storeId: "+ep.getStoreId()+"<br/>");
		out.println("orderName: "+ep.getOrderName()+"<br/>");
		out.println("startDate: "+ep.getStartDate()+"<br/>");
		out.println("endDate: "+ep.getEndDate()+"<br/>");
		out.println("createDate: "+ep.getCreateDate()+"<br/>");
		out.println("amount: "+ep.getAmount()+"<br/>");
		out.println("mail: "+ep.getMail()+"<br/>");
		out.println("orderId: "+ep.getOrderId()+"<br/>");
		out.println("status: "+ep.getStatus()+"<br/>");
		out.println("token: "+ep.getToken()+"<br/>");
		out.println("payDate: "+ep.getPayDate()+"<br/>");
		out.println("URL to customer: "+creator.getEasyPayLink()+ep.getToken() + "<br/>");
		out.println("Installm_plan: "+ep.getInstallm_plan()+"<br/>");
		out.println("Notice_mail: "+ep.getNotice_mail()+"<br/>");
		out.println("Paytype: "+ep.getPaytype()+"<br/>");
		
	}
	out.println("====TestB2CEasyPay finished===="+"<br/>");

%>