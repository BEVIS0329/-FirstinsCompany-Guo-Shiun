<%@ page contentType="text/html; charset=Big5" %>
<%@ page import= "com.hitrust.b2ctoolkit.util.*,java.util.*,com.hitrust.b2ctoolkit.b2cpay.*,java.io.*;" %>
<%

	B2CPayUpdate payUpdate = new B2CPayUpdate();
	payUpdate.setStoreId("61677");
	payUpdate.setKey("6d58b022176e99490b669f144da642ae3c4ca8e845e8ace7058f3c67a74e0576f0a1e5e52cfa84c0be4c80b641893680a1f3259c4544c97d687bd4bba7f4a9dd0ad2d6e11722de88f7119a33af0eb8aeb54dbf212466253ca26d8b01528e04278c1562d3d1a274238ead681f323f4f32702703a6d101ee9603b84fd0736b389b");
	payUpdate.setMac("80bb159bddb210cab2ab0fc307d9677929a7137f64c3129711ca8840aecdfc8520dc933496ab885313dbe6f1cf5ba5ef232cba945414e0378d2933501c94f1d99061cf2fd64904e8a687415d019e6ac38b3b1b70f6d873c02fd0665e580632446740e05675630d0e2b3498e4913f407cc51f1fc423c9fc7876756eb5e5e0a85671ea82e6fb0f78b6e65aa1e702ec61afdfb4d2b9076b119d4242d4d5c2764a3727d6e7856b2f482f9cfe7d9db665fa0a02439332906464612123facecb0170fb825958d39ddc7c23f472dead25946a4a30a358d38f955d3ec2fbeac9d257655d80bacc806e3ad8826813232ec8c6cad2e9141f86388e26b356e7de5957ef72a0");
	payUpdate.setCipher("08a524588b6bd9059274c20ed0310e9beffb3f491d7d95246f2723338cb1f50e99a67955fe81e3f9a4fd6b67f4b72baaa3c42408700fa359533c67e2dd17308a5bc38392e5b374438adfb8933f7a8477e84b1d59a770a001933ce2af9db08df43702a8a81186db3ee25640b21caf92ff271212a74adb70ff59cdc752044ea6c466c1907b51d12bac54a88a1f306a060619");
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
	strLogData += "TrxToken=["+payUpdate.getTrxToken()+"]";
	/*
	bf.write(strLogData, 0, strLogData.length());
	bf.newLine(); //Move to next line.
	bf.close(); //Close the BufferedWrite.
	*/
	//������@
	
	
	//�ЫO�d����,�^����TrustPay��
	out.println(strLogData);
%>
