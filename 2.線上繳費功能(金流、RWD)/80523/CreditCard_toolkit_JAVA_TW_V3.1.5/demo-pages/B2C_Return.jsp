<%@ page contentType="text/html; charset=Big5" %>
<%@ page import= "com.hitrust.b2ctoolkit.b2cpay.*;" %>
<%
	if (!((String)request.getParameter("retcode")).equals("00")){
	  out.print("�������!!");
	  out.print("<br>");
	  out.print("�q��s�� : ["+request.getParameter("ordernumber")+"]");
	  out.print("<br>");
	  out.print("�^�ǥN�X : ["+request.getParameter("retcode")+"]");
	}else{
	  B2CPayOther trx = new B2CPayOther();
	  trx.setStoreId("61510");
	  trx.setType(trx.QUERY);
	  trx.setOrderNo((String)request.getParameter("ordernumber"));
	  trx.transaction();
%>
<html>
<body>
<h1>
<CENTER>                                                                
                                                                      
<TABLE BORDER=1 CELLPADDING=3>                                        
<TR><TD COLSPAN=2 ALIGN=CENTER>�^�ǵ��G</TD></TR>
<TR>                                                                  
	<TD>�q��s��</TD>
	<TD><FONT COLOR=RED><%=trx.getOrderNo()%></FONT></TD>           
</TR>                                                                 
<TR>                                                                  
	<TD>����^�ǽX</TD>
	<TD><FONT COLOR=RED><%=trx.getRetCode()%></FONT></TD>           
</TR>
<TR>                                                                  
	<TD>���O</TD>                                           
	<TD><FONT COLOR=RED><%=trx.getCurrency()%></FONT></TD>            
</TR>
<TR>                                                                  
	<TD>�q����</TD>                                           
	<TD><FONT COLOR=RED><%=trx.getOrderDate()%></FONT></TD>            
</TR>
<TR>                                                                  
	<TD>�q�檬�A�X</TD>                                           
	<TD><FONT COLOR=RED><%=trx.getOrderStatus()%></FONT></TD>            
</TR>                                                                
<TR>                                                                  
	<TD>�֭���B</TD>                                           
	<TD><FONT COLOR=RED><%=trx.getApproveAmount()%></FONT></TD>            
</TR>
<TR>                                                                  
	<TD>�Ȧ���v�X</TD>                                           
	<TD><FONT COLOR=RED><%=trx.getAuthCode()%></FONT></TD>            
</TR>
<TR>                                                                  
	<TD>�Ȧ�ճ�s��</TD>                                           
	<TD><FONT COLOR=RED><%=trx.getAuthRRN()%></FONT></TD>            
</TR>
<TR>                                                                  
	<TD>�дڪ��B</TD>                                           
	<TD><FONT COLOR=RED><%=trx.getCaptureAmount()%></FONT></TD>            
</TR>  
<TR>                                                                  
	<TD>�дڧ妸��</TD>                                           
	<TD><FONT COLOR=RED><%=trx.getPayBatchNum()%></FONT></TD>            
</TR>
<TR>                                                                  
	<TD>�дڤ��</TD>                                           
	<TD><FONT COLOR=RED><%=trx.getCaptureDate()%></FONT></TD>            
</TR> 
<TR>                                                                  
	<TD>�h�ڪ��B</TD>                                           
	<TD><FONT COLOR=RED><%=trx.getRefundAmount()%></FONT></TD>            
</TR>
<TR>                                                                  
	<TD>�h�ڧ妸��</TD>                                           
	<TD><FONT COLOR=RED><%=trx.getRefundBatch()%></FONT></TD>            
</TR> 
<TR>                                                                  
	<TD>�h�ڽճ�s��</TD>                                           
	<TD><FONT COLOR=RED><%=trx.getRefundRRN()%></FONT></TD>            
</TR>
<TR>                                                              
	<TD>�h�ڽX</TD>                                           
	<TD><FONT COLOR=RED><%=trx.getRefundCode()%></FONT></TD>            
</TR>
<TR>                                                                  
	<TD>�h�ڤ��</TD>                                           
	<TD><FONT COLOR=RED><%=trx.getRefundDate()%></FONT></TD>            
</TR>
<TR>                                                                  
	<TD>����Ȧ�</TD>                                           
	<TD><FONT COLOR=RED><%=trx.getAcquirer()%></FONT></TD>            
</TR>                                                       
<TR>                                                                  
	<TD>�d�O</TD>                                           
	<TD><FONT COLOR=RED><%=trx.getCardType()%></FONT></TD>            
</TR>                                                       
<TR>                                                                  
	<TD>���v�覡</TD>                                           
	<TD><FONT COLOR=RED><%=trx.getEci()%></FONT></TD>            
</TR>                                                       
</TABLE>                                                              
</CENTER>
</h1>
</body>
</html>
<%
	}
%>