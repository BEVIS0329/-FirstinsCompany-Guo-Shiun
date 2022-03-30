/*
 *
 * Copyrights (c) 2005 The First Insurance Co, Ltd. All Rights Reserved.
 *
 * This software is the confidential and proprietary information of The First
 * Insurance Co, Ltd. ("Confidential Information"). You shall not disclose such
 * Confidential Information and shall use it only in accordance with the terms
 * of the license agreement you entered into with The First Insurance Co, Ltd.
 *
 */
package com.asi.kyc.wb10.dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import org.apache.commons.dbutils.QueryRunner;
import com.asi.common.exception.AsiException;
import com.asi.common.util.DateUtil;
import com.asi.kyc.common.utils.AS400Connection;
import com.kyc.inc.dao.TrimedMapHandler;
import com.kyc.inc.dao.TrimedMapListHandler;

/**
 * @author ：Vincent
 * @version ：$Revision$ $Date$<br>
 *          <p>
 * 
 *          <pre>
 * 存放路徑	：$Header$  
 * 建立日期	：2021/7/15
 * 異動註記	：
 */
public class WB10M010Dao
{
	
	/**
	 * 查詢未繳費保單明細實作(以ID找)
	 * @param id
	 */
	public List<Map> queryInsurancePolicy(String id) throws AsiException
	{	
		String sysdate = DateUtil.getSysDate(DateUtil.ChType, DateUtil.Format_YYYYMMDD, false);	
		
		//先用ID去MBC3PF找資料，再用FOR迴圈跑，判斷每筆的BC382是否為Y， Y -> PT85PF找車牌、被保人、起保日加入MAP裡 ，不是Y - > IC02PF找車牌、被保人、起保日加入MAP裡
		String sql="select BC301,BC302,BC305,BC306,BC382,BC303,BC384,BC371 from MBC3PF "
				  +"where BC371=? AND BC384=0 AND BC308=98 AND BC307 = 0 "
				  +"order by BC301,BC306";
		
		List<Map> ret = null;

		Connection conn = null;

		try
		{
			conn = AS400Connection.getConnection();

			QueryRunner runner = new QueryRunner();
			ret = (List<Map>) runner.query(conn, sql, id , new TrimedMapListHandler());
			
			if(ret.size() > 0){
				
				for(int i=0;i<ret.size();i++){
					
					Map m=ret.get(i);
					//PT85PF
					if(m.get("BC382").toString().equals("Y")){						
						String sql2="select T8513,T8509,T8506 from PT85PF where T8502=?";
						Map data=(Map)runner.query(conn, sql2, m.get("BC303").toString() , new TrimedMapHandler());
						m.put("carnumber", hiddenCarNumber(data.get("T8513").toString()));
						m.put("insured", hiddenName(data.get("T8509").toString()));
						m.put("startdate", data.get("T8506").toString());
					}
					//IC02PF
					else{					
						String sql3="select C245,C230,C205,G111Y,G111M,G111D from MEG1PF left join IC02PF on G103=C202 AND G101=C236 where G102=?";
						Map data=(Map)runner.query(conn, sql3, m.get("BC303").toString() , new TrimedMapHandler());
						m.put("carnumber", hiddenCarNumber(data.get("C245").toString()));
						m.put("insured", hiddenName(data.get("C230").toString()));
						
						String startdateY=data.get("G111Y").toString();
						String startdateM=Integer.parseInt(data.get("G111M").toString())<10 ? "0"+data.get("G111M").toString() : data.get("G111M").toString();
						String startdateD=Integer.parseInt(data.get("G111D").toString())<10 ? "0"+data.get("G111D").toString() : data.get("G111D").toString();
						m.put("startdate", startdateY+startdateM+startdateD);
					}
					
					//調整繳費期限， 若前一筆交易序號和目前這筆交易序號一樣，則把繳費期限設為和前一筆一樣
					if(i>=1){
						String currentBC301=m.get("BC301").toString();
						String previousBC301=ret.get(i-1).get("BC301").toString();
						
						if(currentBC301.equals(previousBC301)){
							m.put("BC306", ret.get(i-1).get("BC306").toString());
						}
					}					
								
					//加入今天日期
					m.put("sysdate", sysdate);
					
				}			
			}
			
		} catch (SQLException e)
		{
			e.printStackTrace();
			throw new AsiException(e.getLocalizedMessage());
		}
		finally
		{
			AS400Connection.closeConnection(conn);
		}
		return ret;
	}
	
	/**
	 * 判斷被保人ID或要保人ID與持卡人ID是否符合實作(以交易序號找)
	 * @param tradecode
	 */
	public List<Map> getInsuranceID(String tradecode) throws AsiException
	{	
		//BC301(交易序號),BC303(保單或繳費號碼),BC302(大險別),BC305(保費),BC371(客戶ID),BC372(招攬人代號),BC315(通路代碼),
		String sql = "select BC301,BC303,BC302,BC305,BC371,BC372,BC315,BC382 from MBC3PF where BC301=?";  
				
		List<Map> ret = null;

		Connection conn = null;

		try
		{
			conn = AS400Connection.getConnection();

			QueryRunner runner = new QueryRunner();
			ret = (List<Map>) runner.query(conn, sql, tradecode , new TrimedMapListHandler());
			
			for(int i=0;i<ret.size();i++){
				
				Map m=ret.get(i);
				//PT85PF
				if(m.get("BC382").toString().equals("Y")){						
					String sql2="select T8513,T8509,T8506,T8527 from PT85PF where T8502=?";
					Map data=(Map)runner.query(conn, sql2, m.get("BC303").toString() , new TrimedMapHandler());
					m.put("guarantor", data.get("T8527").toString());
				}
				//IC02PF
				else{					
					String sql3="select C245,C230,C205,C248 from MEG1PF left join IC02PF on G103=C202 AND G101=C236 where G102=?";
					Map data=(Map)runner.query(conn, sql3, m.get("BC303").toString() , new TrimedMapHandler());
					m.put("guarantor", data.get("C248").toString());
				}
				
			}
					
		} catch (SQLException e)
		{
			e.printStackTrace();
			throw new AsiException(e.getLocalizedMessage());
		}
		finally
		{
			AS400Connection.closeConnection(conn);
		}
		return ret;
	}
	
	/**
	 * 查詢寫檔資料實作(以交易序號找)
	 * @param tradecode
	 */
	public List<Map> queryData(String tradecode) throws AsiException
	{
		//BC301(交易序號),BC303(保單或繳費號碼),BC302(大險別),BC305(保費),BC371(客戶ID),BC372(招攬人代號),BC315(通路代碼),
		String sql = "select BC301,BC303,BC302,BC305,BC371,BC372,BC315,BC382 from MBC3PF where BC301=?";  
		
		List<Map> ret = null;

		Connection conn = null;

		try
		{
			conn = AS400Connection.getConnection();

			QueryRunner runner = new QueryRunner();
			ret = (List<Map>) runner.query(conn, sql, tradecode , new TrimedMapListHandler());	
			
		} catch (SQLException e)
		{
			e.printStackTrace();
			throw new AsiException(e.getLocalizedMessage());
		}
		finally
		{
			AS400Connection.closeConnection(conn);
		}
		return ret;
	}
	
	/**
	 * 取得授權後更新MBC3PF實作
	 * @param data
	 */
	public void updateMBC3PF(Map m) throws AsiException
	{
		String sysdate = DateUtil.getSysDate(DateUtil.ChType, DateUtil.Format_YYYYMMDD, false);	
		String systime = DateUtil.getSysTime();
		
		//BC309營業日期,BC310交易金額=BC305,BC384收繳日期,BC395(更新時間),BC398(更新日期),BC399(更新人員)
		String sql = "update MBC3PF set BC309=?,BC384=?,BC310=?,BC395=?,BC398=?,BC399=? where BC301=? AND BC303=?";  

		Connection conn = null;

		try
		{
			conn = AS400Connection.getConnection();
				
			String[] param={sysdate,sysdate,m.get("BC305").toString(),systime,sysdate,m.get("BC371").toString(),m.get("BC301").toString(),m.get("BC303").toString()};
				
			QueryRunner runner = new QueryRunner();
			runner.update(conn, sql, param);				
			
		} catch (SQLException e)
		{
			e.printStackTrace();
			throw new AsiException(e.getLocalizedMessage());
		}
		finally
		{
			AS400Connection.closeConnection(conn);
		}
	}
	
	/**
	 * 取得授權後新增PT15PF實作
	 * @param data
	 * @param cardNumber
	 * @param auth
	 * @param ip
	 * @param num
	 */
	public void insertPT15PF(Map m,String cardNumber,String auth,String ip,int num) throws AsiException
	{	
		String sysdate = DateUtil.getSysDate(DateUtil.ChType, DateUtil.Format_YYYYMMDD, false);	
		String systime = DateUtil.getSysTime();
		
		//T1501交易序號-同BC301,T1502(固定寫FIRSTOL當作KEY值),T1503險別-多筆時序編1,2……,T1504保單號碼-繳費號碼或保單號碼-同BC303,          
		//T1507(客戶ID-同BC371),T1520(招攬人-同BC372),T1523(交易日期),T1532(卡號),T1538(資料來源-固定OL),T1539&T1541(保費-同BC305),T1574(確認碼-固定X),
		//T1578大險別-同BC302,T1579(授權碼)T15D6(取授權日期),T15D8(通路代碼-同BC315),T15E0(來源IP),T1594(建檔時間),T1595(異動時間),T1596(建檔日期),T1597(建檔者ID-BC371),
		//T1598(修改日期),T1599(修改者ID-BC371)
		String sql = "insert into PT15PF(T1501,T1502,T1503,T1504,T1507,T1520,T1523,T1532,T1538,T1539,T1541,T1574,T1578,T1579,T15D6,T15D8,T15E0,"
				   + "T1594,T1595,T1596,T1597,T1598,T1599) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";  

		Connection conn = null;

		try
		{
			conn = AS400Connection.getConnection();
				
			String[] param={m.get("BC301").toString(),"FIRSTOL",String.valueOf(num),m.get("BC303").toString(),m.get("BC371").toString(),
						m.get("BC372").toString(),sysdate,cardNumber,"OL",m.get("BC305").toString(),m.get("BC305").toString(),"X",m.get("BC302").toString(),
						auth,sysdate,m.get("BC315").toString(),ip,systime,systime,sysdate,m.get("BC371").toString(),sysdate,m.get("BC371").toString()};
				
			QueryRunner runner = new QueryRunner();
			runner.update(conn, sql, param);	
					
		} catch (SQLException e)
		{
			e.printStackTrace();
			throw new AsiException(e.getLocalizedMessage());
		}
		finally
		{
			AS400Connection.closeConnection(conn);
		}
	}
	
	/**
	 * CALL FAS22PRC 後 取得繳費號碼檔資料實作
	 * @param t8501 大險別
	 * @param t8502 繳費號碼
	 * @return
	 */
	public Map getPt85pfInfo(String t8501,String t8502){
		String sql = "SELECT T8501,T8502,T8508,T8522,T8524,T8529,T8523 FROM PT85PF WHERE T8501=? AND T8502=?";

		Map ret = null;		
		Connection conn = null;
		
		try
		{
			conn = AS400Connection.getConnection();
			
			QueryRunner runner = new QueryRunner();
			ret = (Map)runner.query(conn, sql,new String[]{t8501,t8502},new TrimedMapHandler());
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		finally
		{
			AS400Connection.closeConnection(conn);
		}
		
		return ret;
	}
	
	/**
	 * CALL FAS22PRC 第二次失敗後 更新PT85PF的繳費狀態T8522及暫收資料來源T8524改回T8522=’1’,T8524=空白實作
	 * @param t8501 大險別
	 * @param t8502 繳費號碼
	 * @return
	 */
	public void updatePt85pf(String t8501,String t8502){
		String sql = "update PT85PF set T8522='1',T8524='' where T8501=? AND T8502=?";
	
		Connection conn = null;
		
		try
		{
			conn = AS400Connection.getConnection();
			
			QueryRunner runner = new QueryRunner();
			runner.update(conn, sql,new String[]{t8501,t8502});
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		finally
		{
			AS400Connection.closeConnection(conn);
		}

	}
	
	//車牌隱碼
	public String hiddenCarNumber(String carNumber){
		if("".equals(carNumber)){
			return "";
		}
		
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
	
	//姓名隱碼
	public String hiddenName(String name){
	    StringBuffer hiddenName  = new StringBuffer();
	    if(name.length() < 4){
	       for(int i=0;i<name.length();i++){
	    	   if(i==1){
	    		   hiddenName.append("O");
	    	   }
	    	   else{
	    		   hiddenName.append(name.substring(i,i+1));
	       	   }
	       }
	    }
	    else{
	        for(int i=0;i<name.length();i++){
	    	    if(i==1 || i==2){
	    		    hiddenName.append("O");
	    	    }
	    	    else{
	    		    hiddenName.append(name.substring(i,i+1));
	       	    }
	        }
	    }
	    return hiddenName.toString();
	}

}
