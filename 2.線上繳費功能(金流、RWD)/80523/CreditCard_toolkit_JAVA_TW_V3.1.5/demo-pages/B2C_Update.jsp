<%@ page contentType="text/html; charset=Big5" %>
<%@ page import= "com.hitrust.b2ctoolkit.util.*,java.util.*,com.hitrust.b2ctoolkit.b2cpay.*,java.io.*;" %>
<%

	B2CPayUpdate payUpdate = new B2CPayUpdate();
	payUpdate.setStoreId("61677");
	payUpdate.setKey((String)request.getParameter("KEY"));
	payUpdate.setMac((String)request.getParameter("MAC"));
	payUpdate.setCipher((String)request.getParameter("CIPHER"));
	payUpdate.transaction();
	
	//實作UPDATE資料庫
	
	//範例為將資料寫入檔案
	
	BufferedWriter bf = new BufferedWriter(new FileWriter("d:/Update.txt",true));
	String strLogData = "";
	strLogData += "交易類別=["+payUpdate.getType()+"],";
	strLogData += "訂單編號=["+payUpdate.getOrderNo()+"],";
	strLogData += "交易回傳碼=["+payUpdate.getRetCode()+"],";
	strLogData += "幣別=["+payUpdate.getCurrency()+"],";
	strLogData += "訂單日期=["+payUpdate.getOrderDate()+"],";
	strLogData += "訂單狀態碼=["+payUpdate.getOrderStatus()+"],";
	strLogData += "核准金額=["+payUpdate.getApproveAmount()+"],";
	strLogData += "銀行授權碼=["+payUpdate.getAuthCode()+"],";
	strLogData += "銀行調單編號=["+payUpdate.getAuthRRN()+"],";
	strLogData += "請款金額=["+payUpdate.getCaptureAmount()+"],";
	strLogData += "請款日期=["+payUpdate.getCaptureDate()+"],";
	strLogData += "收單銀行=["+payUpdate.getAcquirer()+"],";
	strLogData += "卡別=["+payUpdate.getCardType()+"],";
	strLogData += "授權方式=["+payUpdate.getEci()+"]";
	
	bf.write(strLogData, 0, strLogData.length());
	bf.newLine(); //Move to next line.
	bf.close(); //Close the BufferedWrite.
	
	//結束實作
	
	
	//請保留此行,回應至TrustPay用
	out.println("R01=00");
%>
