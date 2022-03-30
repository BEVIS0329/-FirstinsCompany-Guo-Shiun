package com.asi.kyc.wb10.actions;

import java.sql.Connection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.MapListHandler;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

import com.asi.common.GlobalKey;
import com.asi.common.exception.AsiException;
import com.asi.common.security.UserInfo;
import com.asi.common.struts.AsiActionForm;
import com.asi.common.util.DateUtil;
import com.asi.kyc.common.utils.AS400Connection;
import com.asi.kyc.common.utils.HitrustUtil;
import com.asi.kyc.common.utils.KycMailUtil;
import com.asi.kyc.wb1.actions.AS400Procedure;
import com.asi.kyc.wb10.forms.WB10M010f;
import com.asi.kyc.wb10.models.WB10M010m;
import com.asi.kyc.wb8.actions.WB8I085;
import com.kyc.schedule.CreditCardAPI;
import com.kyc.sec.actions.kycAction;

/**
 * 網投線上繳費服務
 * @author Vincent
 * @Create Date 2021年07月15日
 */
public class WB10M0101 extends kycAction
{
	
	public void redefineActionCode(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	{
		WB10M010f form1 = (WB10M010f) form;
		if (form1.getActionCode() == 0)
		{
			form1.setActionCode(99);
			
		}
	}

	public void doProcess(ActionMapping mapping, AsiActionForm form, HttpServletRequest request, HttpServletResponse response) throws AsiException
	{
			
		WB10M010f form1 = (WB10M010f) form;
		tx_controller.begin(1);
		WB10M010m model = new WB10M010m(tx_controller, request, form1);		
		model.init();

		int action = form.getActionCode();

		HttpSession session = request.getSession(false);
		UserInfo ui = (UserInfo) session.getAttribute(GlobalKey.USER_INFO);

		//網投頁面或官網進入繳費服務
		if(action == 99)
        {
			//已登入->進入繳費服務，未登入->進入登入頁面
			if(ui != null){
				String id=ui.getUserId();
				//String id="T12XXXX577";//A123456789,T122360208
				
				List<Map> datalist=model.queryInsurancePolicy(id);
				
				request.setAttribute("form", form1);
				request.setAttribute("datalist", datalist);
	            form.setNextPage(1);
			}
			else{
				form.setNextPage(5);
			}
			
        }
		//繳費服務第一頁選擇信用卡繳費(將交易序號及總保費紀錄在form)
		else if(action == 5){	
			
			//先記住險種和車牌，最後取授權成功寄mail會用到
			String instype=request.getParameter("instype") != null ? request.getParameter("instype").toString() : "";
			String carnumber=request.getParameter("carnumber") != null ? request.getParameter("carnumber").toString() : "";
			session.setAttribute("instype", instype);
			session.setAttribute("carnumber", carnumber);
			
            form.setNextPage(2);
        }
		//繳費服務第二頁選擇下一步(將信用卡持卡人ID、姓名、關係記錄在form)
		else if(action == 6){		
			form.setNextPage(3);
		}
		//繳費服務第三頁選擇確認付款(驗證信用卡持卡人ID、姓名、關係、卡號、有效年月日，並且CALL Hitrut API取得授權，若失敗要在下一頁顯示錯誤訊息)
		else if(action == 7){
				
			//取得信用卡相關資料
			String tradecode=form1.getTradecode() != null ? form1.getTradecode() : "";//交易序號
			double instotal=form1.getInstotal() != null ? Double.parseDouble(form1.getInstotal()) : 0;//總保費
			String cardname=form1.getCardname() != null ? form1.getCardname() : "";//持卡人姓名
			String cardid=form1.getCardid() != null ? form1.getCardid() : "";//持卡人ID
			String cardrelselect=form1.getCardrelselect() != null ? form1.getCardrelselect() : "";//與被保人關係
			String cardNo = form1.getCard1() + form1.getCard2() + form1.getCard3() + form1.getCard4();//信用卡卡號
			String expireDate = form1.getCardYY().substring(2) + form1.getCardMM();//到期日，必須是4碼，EX：202206 -> 2206
			
			//檢核信用卡號
			AS400Procedure pro = new AS400Procedure();
			String mds030 = pro.callMDS030PRC(tx_controller.getConnection(1), cardNo);
			if("N".equals(mds030)){
				throw new AsiException("WB8.ERROR02");//信用卡號錯誤!
			}				
			//檢核信用卡到期年月
			WB8I085 wb8 = new WB8I085();
			if(!wb8.checkCreditDateYM(form1.getCardYY()+form1.getCardMM())){
				throw new AsiException("WB8.ERROR08");//信用卡有效年月超過規定期限!
			}				
			//檢核信用卡持卡人ID及姓名
			//檢查ZS03PF信用卡號對應持卡人檢查表
			String[] cardInfo = wb8.getCardOwnerZS03PF(tx_controller.getConnection(1), cardNo);
			if(cardInfo[0].trim().length()>0 && !form1.getCardid().equals(cardInfo[0]))
			{
				throw new AsiException("WB8.ERROR10","ID");//信用卡持卡人ID有誤!
			}
			if(cardInfo[1].trim().length()>0 && !cardname.equals(cardInfo[1]))
			{
				throw new AsiException("WB8.ERROR10","姓名");//信用卡持卡人姓名有誤!
			}
			//檢查線上繳費檔案
			String[] cardInfo2 = wb8.getCardOwner(tx_controller.getConnection(1), cardNo);
			if(cardInfo2[0]!=null && cardInfo2[0].trim().length()>0 && !form1.getCardid().equals(cardInfo2[0]))
			{
				throw new AsiException("WB8.ERROR10","ID.");//信用卡持卡人ID有誤!
			}
			if(cardInfo2[1]!=null && cardInfo2[1].trim().length()>0 && !cardname.equals(cardInfo2[1]))
			{
				throw new AsiException("WB8.ERROR10","姓名.");//信用卡持卡人姓名有誤!
			}
			
			CreditCardAPI creditApi = new CreditCardAPI();
		
			//紀錄成功或失敗的狀態
			Map status=new HashMap();
			//取授權	ret[0]:交易結果('00'為成功) ret[1]:授權碼
			String[] ret = HitrustUtil.callHitrustSSL_OL(tradecode, instotal, "WB10M010-"+cardid, cardNo, expireDate, servlet);
			//取授權成功
			if("00".equals(ret[0]))
			{					
				//取得要寫檔的資料
				List<Map> result=model.queryData(tradecode);
							
				//在HEADER的X-FORWARDED-FOR的標籤中找到真實的IP
				String remoteAddr ="";
		        if (request.getHeader("x-forwarded-for") == null) { 
		        	remoteAddr = request.getRemoteAddr(); 
		        }else{
		        	remoteAddr = request.getHeader("x-forwarded-for");
		        }
        
				//卡號隱碼  
				String hiddenCardNumber=hiddenCard(form1.getCard1(),form1.getCard2(),form1.getCard4());
		        				
				for(int i=0;i<result.size();i++){
					Map m=result.get(i);
					
					//只有BC382為Y的才需要call FAS22PRC 轉出單	
					if(m.get("BC382").toString().equals("Y")){
										
						//需傳入BC302險別、BC303繳費號碼、BC372招攬人
						String[] fas22ret1 = pro.callFAS22PRC(tx_controller.getConnection(1), servlet , request , "K", m.get("BC302").toString(), m.get("BC303").toString(), "2", "0",m.get("BC372").toString());
						//第一次call fas22prc成功
						if("0".equals(fas22ret1[0]))
						{
							//log
							Map map = model.getPt85pfInfo(m.get("BC302").toString(), m.get("BC303").toString());
							System.out.println("callFAS22PRC "+m.get("BC303").toString()+" 異動後：T8501="+String.valueOf(map.get("T8501"))+",T8502="+String.valueOf(map.get("T8502"))+",T8508="+String.valueOf(map.get("T8508"))
									+",T8522="+String.valueOf(map.get("T8522"))+",T8524="+String.valueOf(map.get("T8524"))+",T8529="+String.valueOf(map.get("T8529")));
							
							//解繳成功並回傳暫收保費日
							String sysdate=DateUtil.getSysDate(DateUtil.ChType, DateUtil.Format_YYYYMMDD, false);
							String[] fas22ret2 = pro.callFAS22PRC(tx_controller.getConnection(1) , getServlet(), request , "K", String.valueOf(map.get("T8501")), String.valueOf(map.get("T8502")), "", sysdate,m.get("BC372").toString());
							//第二次call fas22prc成功，寫檔
							if("0".equals(fas22ret2[0])){	
								//UPDATE MBC3PF
								model.updateMBC3PF(m);
								//INSERT PT15PF  信用卡紀錄：hiddenCardNumber(卡號),授權碼(ret[1])  IP(remoteAddr)  序號
								model.insertPT15PF(m, hiddenCardNumber, ret[1],remoteAddr,i+1);
								
							}
							//第二次call fas22prc失敗
							else{
								//直接update PT85PF的繳費狀態T8522及暫收資料來源T8524
								model.updatePt85pf(String.valueOf(map.get("T8501")), String.valueOf(map.get("T8502")));
								throw new AsiException("WB8.ERROR04",new String[]{m.get("BC303").toString(),fas22ret2[2]});//繳費號碼：[{0}]，有錯誤訊息「[{1}]」
							}
						}
						else{
							throw new AsiException("WB8.ERROR04",new String[]{m.get("BC303").toString(),fas22ret1[2]});//繳費號碼：[{0}]，有錯誤訊息「[{1}]」
						}					
					}
					//正常寫檔
					else{
						//UPDATE MBC3PF
						model.updateMBC3PF(m);
						//INSERT PT15PF  信用卡紀錄：hiddenCardNumber(卡號),授權碼(ret[1])  IP(remoteAddr)  序號
						model.insertPT15PF(m, hiddenCardNumber, ret[1],remoteAddr,i+1);
					}
				}
				
				//寄mail通知客戶
				String bcc="'"+result.get(0).get("BC372").toString()+"'";
				if(ui != null && ui.getEmail() != null){
					sendMail(request,ui.getEmail(),tradecode,bcc);
				}
				
				status.put("issuccess", "Y");
				request.setAttribute("status", status);
				
				form1.setNextPage(4);	
					
			}
			//取授權失敗
			else{					
				//throw new AsiException("WB8.ERROR03",new String[]{ret[0],creditApi.getErrorDesc(ret[0])});//信用卡取授權失敗，錯誤代碼:[{0}]，[{1}]請確認填入欄位資料，再重新操作!		
				status.put("issuccess", "N");
				status.put("errorcode", ret[0]);
				status.put("errorcontent", creditApi.getErrorDesc(ret[0]));
				request.setAttribute("status", status);
				
				form1.setNextPage(4);		
			}	
			
		}
	}	
	
	//車牌隱碼
	public String hiddenCarNumber(String carNumber){
		StringBuffer hidden=new StringBuffer();
		//拆為前後段來隱碼
		String[] number=carNumber.split("-");
		//前段
		StringBuffer number1=new StringBuffer();
		for(int i=0;i<number[0].length();i++){
			if(i==number[0].length()-1){
				number1.append("*");
			}
			else{
				number1.append(number[0].substring(i,i+1));
			}
			
		}
		//後段
		StringBuffer number2=new StringBuffer();
		for(int i=0;i<number[1].length();i++){
			if(i==0 || i==1){
				number2.append("*");
			}
			else{
				number2.append(number[1].substring(i,i+1));
			}
			
		}
		//前段+後段
		hidden.append(number1.toString()).append("-").append(number2.toString());
		
		return hidden.toString();
	}
	
	//卡號隱碼，card1不隱碼、card2隱後兩碼、card3全部隱碼、card4不隱碼
	public String hiddenCard(String card1,String card2,String card4){
		
		StringBuffer hidden=new StringBuffer();
		
		StringBuffer hiddenCard2=new StringBuffer();
		for(int i=0;i<card2.length();i++){
			if(i==0 || i==1){
				hiddenCard2.append(card2.substring(i, i+1));
			}
			else{
				hiddenCard2.append("*");
			}
		}
		
		hidden.append(card1).append(hiddenCard2.toString()).append("****").append(card4);
		
		return hidden.toString();
				
	}
	
	//寄送mail給客戶
	public void sendMail(HttpServletRequest request,String sendto,String tradecode,String bcc){
		HttpSession session = request.getSession(false);
		
		String sysdate = DateUtil.getSysDate(DateUtil.ChType, DateUtil.Format_YYYYMMDD, false);	
		
		String year=String.valueOf((Integer.parseInt(sysdate.substring(0, 3))+1911));
		String month=sysdate.substring(3, 5);
		String date=sysdate.substring(5);
		
		String insTypeCode= session.getAttribute("instype") != null ? session.getAttribute("instype").toString() : "";
		String insType = insTypeCode.equals("C") ? "車險" : insTypeCode.equals("F") ? "住火險" : insTypeCode.equals("E") ? "工程險" : insTypeCode.equals("H") ? "船體險" : insTypeCode.equals("M") ? "水險" : insTypeCode.equals("O") ? "新種險" : insTypeCode;

		String carNumber= session.getAttribute("carnumber") != null ? session.getAttribute("carnumber").toString() : "";
		session.removeAttribute("instype");
		session.removeAttribute("carnumber");
		
		KycMailUtil kmu = new KycMailUtil();
		kmu.setFrom("admin@firstins.com.tw");
		
		StringBuffer context=new StringBuffer();
		context.append("親愛的保戶您好：<br>");
		context.append("您於").append(year).append("/").append(month).append("/").append(date).append("完成線上信用卡繳費。<br>");
		context.append("交易序號：").append(tradecode).append("<br>");
		context.append("投保險種：").append(insType).append("<br>");
		if(!carNumber.equals("")){
			String hidden=hiddenCarNumber(carNumber);
			context.append("車牌：").append(hidden).append("<br>");
		}							
		context.append("若有其他問題，請洽您的業務人員，或撥0800-288-068，由專人為您服務。<br>");
		context.append("本項交易若未獲發卡銀行核准，則本保險費簽帳單自動失效，本公司得重行收費<br>");

		kmu.setMessage(context.toString());
		kmu.setSubject("【刷卡完成通知】");		
			
		if(!sendto.equals("")){
			//取得招攬人員email
			String[] getbccemail = getAuthManList(bcc);
			//kmu.addTo("w92532@gmail.com");//測試用
			kmu.addTo(sendto);
			kmu.addBcc(getbccemail);
			kmu.sendMail();
		}		
		
	}
	
	/**
	 * 取出人員帳號檔email
	 * @param list
	 * @return
	 */
	private String[] getAuthManList(String list)
	{
		StringBuffer buf = new StringBuffer();
		buf.append("SELECT * FROM SECAJ WHERE USERID IN (" + list + ")");

		List<?> rs = null;
		String[] email = null;
		Connection con = null;
		
		try
		{
			con = AS400Connection.getOracleConnection();
			QueryRunner runner = new QueryRunner();
			rs = (List<?>) runner.query(con, buf.toString(), new MapListHandler());

			email = new String[rs.size()];
			for (int i = 0; i < rs.size(); i++)
			{
				Map m = (Map) rs.get(i);
				email[i] = m.get("EMAIL").toString();
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}finally{
			AS400Connection.closeConnection(con);
		}

		return email;
	}
}