<%@ page contentType="text/html; charset=Big5" %>
<%@ page import= "com.hitrust.b2ctoolkit.util.*,java.util.*,com.hitrust.b2ctoolkit.b2cpay.*,java.io.*;" %>
<%

	B2CPayUpdate payUpdate = new B2CPayUpdate();
	payUpdate.setStoreId("61677");
	payUpdate.setKey((String)request.getParameter("KEY"));
	payUpdate.setMac((String)request.getParameter("MAC"));
	payUpdate.setCipher((String)request.getParameter("CIPHER"));
	payUpdate.transaction();
	
	//��@UPDATE��Ʈw
	
	//�d�Ҭ��N��Ƽg�J�ɮ�
	
	BufferedWriter bf = new BufferedWriter(new FileWriter("d:/Update.txt",true));
	String strLogData = "";
	strLogData += "������O=["+payUpdate.getType()+"],";
	strLogData += "�q��s��=["+payUpdate.getOrderNo()+"],";
	strLogData += "����^�ǽX=["+payUpdate.getRetCode()+"],";
	strLogData += "���O=["+payUpdate.getCurrency()+"],";
	strLogData += "�q����=["+payUpdate.getOrderDate()+"],";
	strLogData += "�q�檬�A�X=["+payUpdate.getOrderStatus()+"],";
	strLogData += "�֭���B=["+payUpdate.getApproveAmount()+"],";
	strLogData += "�Ȧ���v�X=["+payUpdate.getAuthCode()+"],";
	strLogData += "�Ȧ�ճ�s��=["+payUpdate.getAuthRRN()+"],";
	strLogData += "�дڪ��B=["+payUpdate.getCaptureAmount()+"],";
	strLogData += "�дڤ��=["+payUpdate.getCaptureDate()+"],";
	strLogData += "����Ȧ�=["+payUpdate.getAcquirer()+"],";
	strLogData += "�d�O=["+payUpdate.getCardType()+"],";
	strLogData += "���v�覡=["+payUpdate.getEci()+"]";
	
	bf.write(strLogData, 0, strLogData.length());
	bf.newLine(); //Move to next line.
	bf.close(); //Close the BufferedWrite.
	
	//������@
	
	
	//�ЫO�d����,�^����TrustPay��
	out.println("R01=00");
%>
