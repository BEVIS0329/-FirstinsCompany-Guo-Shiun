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
<TR><TD COLSPAN=2 ALIGN=CENTER>�^�ǵ��G</TD></TR>
<TR>                                                                  
	<TD>�q��s��</TD>
	<TD><FONT COLOR=RED><%=auth.getOrderNo()%></FONT></TD>           
</TR>                                                                 
<TR>                                                                  
	<TD>����^�ǽX</TD>
	<TD><FONT COLOR=RED><%=auth.getRetCode()%></FONT></TD>           
</TR>
<TR>                                                                  
	<TD>���O</TD>                                           
	<TD><FONT COLOR=RED><%=auth.getCurrency()%></FONT></TD>            
</TR>
<TR>                                                                  
	<TD>�q����</TD>                                           
	<TD><FONT COLOR=RED><%=auth.getOrderDate()%></FONT></TD>            
</TR>
<TR>                                                                  
	<TD>�q�檬�A�X</TD>                                           
	<TD><FONT COLOR=RED><%=auth.getOrderStatus()%></FONT></TD>            
</TR>                                                                
<TR>                                                                  
	<TD>�֭���B</TD>                                           
	<TD><FONT COLOR=RED><%=auth.getApproveAmount()%></FONT></TD>            
</TR>
<TR>                                                                  
	<TD>�Ȧ���v�X</TD>                                           
	<TD><FONT COLOR=RED><%=auth.getAuthCode()%></FONT></TD>            
</TR>
<TR>                                                                  
	<TD>�Ȧ�ճ�s��</TD>                                           
	<TD><FONT COLOR=RED><%=auth.getAuthRRN()%></FONT></TD>            
</TR>
<TR>                                                                  
	<TD>�дڪ��B</TD>                                           
	<TD><FONT COLOR=RED><%=auth.getCaptureAmount()%></FONT></TD>            
</TR>  
<TR>                                                                  
	<TD>�дڧ妸��</TD>                                           
	<TD><FONT COLOR=RED><%=auth.getPayBatchNum()%></FONT></TD>            
</TR>
<TR>                                                                  
	<TD>�дڤ��</TD>                                           
	<TD><FONT COLOR=RED><%=auth.getCaptureDate()%></FONT></TD>            
</TR> 
<TR>                                                                  
	<TD>�h�ڪ��B</TD>                                           
	<TD><FONT COLOR=RED><%=auth.getRefundAmount()%></FONT></TD>            
</TR>
<TR>                                                                  
	<TD>�h�ڧ妸��</TD>                                           
	<TD><FONT COLOR=RED><%=auth.getRefundBatch()%></FONT></TD>            
</TR> 
<TR>                                                                  
	<TD>�h�ڽճ�s��</TD>                                           
	<TD><FONT COLOR=RED><%=auth.getRefundRRN()%></FONT></TD>            
</TR>
<TR>                                                              
	<TD>�h�ڽX</TD>                                           
	<TD><FONT COLOR=RED><%=auth.getRefundCode()%></FONT></TD>            
</TR>
<TR>                                                                  
	<TD>�h�ڤ��</TD>                                           
	<TD><FONT COLOR=RED><%=auth.getRefundDate()%></FONT></TD>            
</TR>
<TR>                                                                  
	<TD>����Ȧ�</TD>                                           
	<TD><FONT COLOR=RED><%=auth.getAcquirer()%></FONT></TD>            
</TR>                                                       
<TR>                                                                  
	<TD>�d�O</TD>                                           
	<TD><FONT COLOR=RED><%=auth.getCardType()%></FONT></TD>            
</TR>                                                       
<TR>                                                                  
	<TD>���v�覡</TD>                                           
	<TD><FONT COLOR=RED><%=auth.getEci()%></FONT></TD>            
</TR>

</TABLE>                                                              
</CENTER>
</h1>
</body>
</html>