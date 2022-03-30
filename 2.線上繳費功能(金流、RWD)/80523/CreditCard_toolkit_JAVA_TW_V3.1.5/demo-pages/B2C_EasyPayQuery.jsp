<%@ page contentType="text/html; charset=Big5" %>
<%@ page import= "com.hitrust.b2ctoolkit.b2cpay.*,java.net.*;" %>
<%
	
	String email = request.getParameter("email");

	String startDate = request.getParameter("startDate");
	String endDate = request.getParameter("endDate");

	String createStartDate = request.getParameter("createStartDate");
	String createEndDate = request.getParameter("createEndDate");

	// =========================================================
			
	if(startDate!=null&&startDate.trim().length()==0)
		startDate = null;
	if(endDate!=null&&endDate.trim().length()==0)
		endDate = null;
	if(createStartDate!=null&&createStartDate.trim().length()==0)
		createStartDate = null;
	if(createEndDate!=null&&createEndDate.trim().length()==0)
		createEndDate = null;
	
	// =========================================================

	java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

	B2CEasyPayQuery qry = new B2CEasyPayQuery("XXXXX");
	
	qry.setCreateDate(
			(createStartDate==null?null:sdf.parse(createStartDate+" 00:00:00")), 
			(createEndDate==null?null:sdf.parse(createEndDate+" 23:59:59")));
	qry.setAvailableDate(
			(startDate==null?null:sdf.parse(startDate+" 00:00:00")), 
			(endDate==null?null:sdf.parse(endDate+" 23:59:59")));
	qry.setEmail(email);
	
	
	qry.transaction();
	
	out.println("rtn code: "+qry.getRetCode()+"<br/><br/>");
	java.util.ArrayList list = qry.getOrders();
	for(int i=0;i<list.size();i++){
		B2CEasyPayDetail ep = (B2CEasyPayDetail) list.get(i);
		out.println("----------------------------<br/>");
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
		out.println("Installm_plan: "+ep.getInstallm_plan()+"<br/>");
		out.println("Notice_mail: "+ep.getNotice_mail()+"<br/>");
		out.println("Paytype: "+ep.getPaytype()+"<br/>");
	}
	out.println("====TestB2CEasyPay Query finished====");

%>