<%@ page contentType="text/html; charset=Big5" %>
<%@ page import= "com.hitrust.b2ctoolkit.b2cpay.*;" %>
<%
	B2CPayOther trx = new B2CPayOther();
	trx.setStoreId("41147");
	trx.setType(trx.REFUND);
	trx.setOrderNo((String)request.getParameter("ordernumber"));
	trx.setAmount((String)request.getParameter("amount"));
	trx.setQueryFlag((String)request.getParameter("queryflag"));
	trx.transaction();
%>
<html>
<body>
<h1>
<CENTER>                                                                
                                                                      
<TABLE BORDER=1 CELLPADDING=3>                                        
<TR><TD COLSPAN=2 ALIGN=CENTER>回傳結果</TD></TR>
<TR>                                                                  
	<TD>訂單編號</TD>
	<TD><FONT COLOR=RED><%=trx.getOrderNo()%></FONT></TD>           
</TR>                                                                 
<TR>                                                                  
	<TD>交易回傳碼</TD>
	<TD><FONT COLOR=RED><%=trx.getRetCode()%></FONT></TD>           
</TR>
<TR>                                                                  
	<TD>幣別</TD>                                           
	<TD><FONT COLOR=RED><%=trx.getCurrency()%></FONT></TD>            
</TR>
<TR>                                                                  
	<TD>訂單日期</TD>                                           
	<TD><FONT COLOR=RED><%=trx.getOrderDate()%></FONT></TD>            
</TR>
<TR>                                                                  
	<TD>訂單狀態碼</TD>                                           
	<TD><FONT COLOR=RED><%=trx.getOrderStatus()%></FONT></TD>            
</TR>                                                                
<TR>                                                                  
	<TD>核准金額</TD>                                           
	<TD><FONT COLOR=RED><%=trx.getApproveAmount()%></FONT></TD>            
</TR>
<TR>                                                                  
	<TD>銀行授權碼</TD>                                           
	<TD><FONT COLOR=RED><%=trx.getAuthCode()%></FONT></TD>            
</TR>
<TR>                                                                  
	<TD>銀行調單編號</TD>                                           
	<TD><FONT COLOR=RED><%=trx.getAuthRRN()%></FONT></TD>            
</TR>
<TR>                                                                  
	<TD>請款金額</TD>                                           
	<TD><FONT COLOR=RED><%=trx.getCaptureAmount()%></FONT></TD>            
</TR>  
<TR>                                                                  
	<TD>請款批次號</TD>                                           
	<TD><FONT COLOR=RED><%=trx.getPayBatchNum()%></FONT></TD>            
</TR>
<TR>                                                                  
	<TD>請款日期</TD>                                           
	<TD><FONT COLOR=RED><%=trx.getCaptureDate()%></FONT></TD>            
</TR> 
<TR>                                                                  
	<TD>退款金額</TD>                                           
	<TD><FONT COLOR=RED><%=trx.getRefundAmount()%></FONT></TD>            
</TR>
<TR>                                                                  
	<TD>退款批次號</TD>                                           
	<TD><FONT COLOR=RED><%=trx.getRefundBatch()%></FONT></TD>            
</TR> 
<TR>                                                                  
	<TD>退款調單編號</TD>                                           
	<TD><FONT COLOR=RED><%=trx.getRefundRRN()%></FONT></TD>            
</TR>
<TR>                                                              
	<TD>退款碼</TD>                                           
	<TD><FONT COLOR=RED><%=trx.getRefundCode()%></FONT></TD>            
</TR>
<TR>                                                                  
	<TD>退款日期</TD>                                           
	<TD><FONT COLOR=RED><%=trx.getRefundDate()%></FONT></TD>            
</TR>
<TR>                                                                  
	<TD>收單銀行</TD>                                           
	<TD><FONT COLOR=RED><%=trx.getAcquirer()%></FONT></TD>            
</TR>                                                       
<TR>                                                                  
	<TD>卡別</TD>                                           
	<TD><FONT COLOR=RED><%=trx.getCardType()%></FONT></TD>            
</TR>                                                       
<TR>                                                                  
	<TD>授權方式</TD>                                           
	<TD><FONT COLOR=RED><%=trx.getEci()%></FONT></TD>            
</TR>                                                       
</TABLE>                                                              
</CENTER>
</h1>
</body>
</html>