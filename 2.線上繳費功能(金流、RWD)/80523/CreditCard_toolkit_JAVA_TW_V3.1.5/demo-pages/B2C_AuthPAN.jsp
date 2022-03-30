<%@ page contentType="text/html; charset=Big5" %>
<%@ page import="com.hitrust.b2ctoolkit.b2cpay.B2CPayAuthSSL" %>
<%
	B2CPayAuthSSL auth = new B2CPayAuthSSL();
	auth.setStoreId((String)request.getParameter("storeid"));
	auth.setOrderNo((String) request.getParameter("ordernumber"));
	auth.setOrderDesc((String)request.getParameter("orderdesc"));
	auth.setAmount((String)request.getParameter("amount"));
	auth.setTicketNo((String)request.getParameter("ticketno"));
	auth.setPan((String)request.getParameter("cardnum"));
	auth.setExpiry((String)request.getParameter("expiry"));
	auth.transaction();
%>
<html>
<body>
<h1>
<CENTER>                                                                
                                                                      
<TABLE BORDER=1 CELLPADDING=3>                                        
<TR><TD COLSPAN=2 ALIGN=CENTER>回傳結果</TD></TR>
<TR>                                                                  
	<TD>訂單編號</TD>
	<TD><FONT COLOR=RED><%=auth.getOrderNo()%></FONT></TD>           
</TR>                                                                 
<TR>                                                                  
	<TD>交易回傳碼</TD>
	<TD><FONT COLOR=RED><%=auth.getRetCode()%></FONT></TD>           
</TR>
<TR>                                                                  
	<TD>幣別</TD>                                           
	<TD><FONT COLOR=RED><%=auth.getCurrency()%></FONT></TD>            
</TR>
<TR>                                                                  
	<TD>訂單日期</TD>                                           
	<TD><FONT COLOR=RED><%=auth.getOrderDate()%></FONT></TD>            
</TR>
<TR>                                                                  
	<TD>訂單狀態碼</TD>                                           
	<TD><FONT COLOR=RED><%=auth.getOrderStatus()%></FONT></TD>            
</TR>                                                                
<TR>                                                                  
	<TD>核准金額</TD>                                           
	<TD><FONT COLOR=RED><%=auth.getApproveAmount()%></FONT></TD>            
</TR>
<TR>                                                                  
	<TD>銀行授權碼</TD>                                           
	<TD><FONT COLOR=RED><%=auth.getAuthCode()%></FONT></TD>            
</TR>
<TR>                                                                  
	<TD>銀行調單編號</TD>                                           
	<TD><FONT COLOR=RED><%=auth.getAuthRRN()%></FONT></TD>            
</TR>
<TR>                                                                  
	<TD>請款金額</TD>                                           
	<TD><FONT COLOR=RED><%=auth.getCaptureAmount()%></FONT></TD>            
</TR>  
<TR>                                                                  
	<TD>請款批次號</TD>                                           
	<TD><FONT COLOR=RED><%=auth.getPayBatchNum()%></FONT></TD>            
</TR>
<TR>                                                                  
	<TD>請款日期</TD>                                           
	<TD><FONT COLOR=RED><%=auth.getCaptureDate()%></FONT></TD>            
</TR> 
<TR>                                                                  
	<TD>退款金額</TD>                                           
	<TD><FONT COLOR=RED><%=auth.getRefundAmount()%></FONT></TD>            
</TR>
<TR>                                                                  
	<TD>退款批次號</TD>                                           
	<TD><FONT COLOR=RED><%=auth.getRefundBatch()%></FONT></TD>            
</TR> 
<TR>                                                                  
	<TD>退款調單編號</TD>                                           
	<TD><FONT COLOR=RED><%=auth.getRefundRRN()%></FONT></TD>            
</TR>
<TR>                                                              
	<TD>退款碼</TD>                                           
	<TD><FONT COLOR=RED><%=auth.getRefundCode()%></FONT></TD>            
</TR>
<TR>                                                                  
	<TD>退款日期</TD>                                           
	<TD><FONT COLOR=RED><%=auth.getRefundDate()%></FONT></TD>            
</TR>
<TR>                                                                  
	<TD>收單銀行</TD>                                           
	<TD><FONT COLOR=RED><%=auth.getAcquirer()%></FONT></TD>            
</TR>                                                       
<TR>                                                                  
	<TD>卡別</TD>                                           
	<TD><FONT COLOR=RED><%=auth.getCardType()%></FONT></TD>            
</TR>                                                       
<TR>                                                                  
	<TD>授權方式</TD>                                           
	<TD><FONT COLOR=RED><%=auth.getEci()%></FONT></TD>            
</TR>

</TABLE>                                                              
</CENTER>
</h1>
</body>
</html>