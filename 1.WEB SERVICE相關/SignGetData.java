package com.kyc.ws;

import java.io.ByteArrayInputStream;
import java.io.UnsupportedEncodingException;
import java.sql.Connection;
import java.sql.SQLException;

import org.apache.commons.dbutils.QueryRunner;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.asi.common.util.DateUtil;
import com.asi.kyc.common.utils.AS400Connection;
import com.kyc.inc.dao.TrimedMapHandler;

/**
 * 	@author vsg
 * 	簽署平台WEB-Service
 *	@update 2020/10/13 增加板信用取號及辨視條碼上傳功能
 */
public class SignGetData
{
	/**
	 * 簽署平台取號
	 * 
	 * @param Sales 通路別
	 * @param userid 員工編號
	 * @param number 取號數量
	 * @return
	 */
	public String getNumber(String Sales, String number, String userid , String branch , String casetype , String instype)
	{
		System.out.println("呼叫SignGetData：getNumber 開始執行" + DateUtil.getSysDate(DateUtil.ChType, DateUtil.Format_YYYYMMDD, false) + " " + DateUtil.getSysTime(false));
		GetNumberData ser = new GetNumberData();
		Map map = null;
		
		//處理取號，若有傳入D050台企代號，則另外取號
		if(Sales.equals("D050") || Sales.equals("B075"))
			map = ser.getDataD050("D050", number, userid, branch, casetype, instype);
		else if(Sales.equals("B052") || Sales.equals("B053"))//2020/10/13 新增板信用取號規則
			map = ser.getDataB052B053(Sales, number, userid, "110", casetype, instype);	
		else
			map = ser.getData(Sales, number, userid);
		
		if (map != null)
		{
			Document doc = DocumentHelper.createDocument();
			Element root = doc.addElement("ROOT");
			Element ret1 = root.addElement("result");
			ret1.setText(String.valueOf(map.get("result")));

			System.out.println(doc.asXML());
			System.out.println("呼叫SignGetData：getNumber 完成" + DateUtil.getSysDate(DateUtil.ChType, DateUtil.Format_YYYYMMDD, false) + " " + DateUtil.getSysTime(false));

			return doc.asXML();
		}
		return null;
	}

	/**
	 * 員工個人資料查詢
	 * 
	 * @param userid 員工代號
	 * @return
	 */
	public String getUserId(String userid)
	{
		GetUserIdData ser = new GetUserIdData();
		Map map = ser.getUsetData(userid);
		if (map != null)
		{
			Document doc = DocumentHelper.createDocument();
			Element root = doc.addElement("ROOT");
			Element ret1 = root.addElement("name");
			ret1.setText(String.valueOf(map.get("M302")));
			Element ret2 = root.addElement("mail");
			ret2.setText(String.valueOf(map.get("EMAIL")));
			Element ret3 = root.addElement("unit");
			ret3.setText(String.valueOf(map.get("M303")));
			Element ret4 = root.addElement("endday");
			ret4.setText(String.valueOf(map.get("M315")));
			System.out.println(doc.asXML());
			return doc.asXML();
		}
		return null;
	}

	/**
	 * 查保單號碼
	 * 
	 * @param Acc  受理編號
	 * @return
	 */
	public String getInsuranceId(String Acc)
	{
		System.out.println("呼叫SignGetData：getInsuranceId 開始執行" + DateUtil.getSysDate(DateUtil.ChType, DateUtil.Format_YYYYMMDD, false) + " " + DateUtil.getSysTime(false));
		List data = getXmlData(Acc);

		Document doc = DocumentHelper.createDocument();
		if (data != null)
		{
			Element root = doc.addElement("ROOT");

			for (int i = 0; i < data.size(); i++)
			{
				Element ret = root.addElement("Acc");
				Map map = (Map) data.get(i);
				
				Element r1 = ret.addElement("AccID");
				r1.setText(String.valueOf(map.get("AccID")));
				
				Element r2 = ret.addElement("InsID");
				if (map.get("InsID").equals(""))
				{
					r2.setText(String.valueOf("0"));
				} else
				{
					r2.setText(String.valueOf(map.get("InsID")));
				}

				Element r3 = ret.addElement("InsIDDay");
				r3.setText(String.valueOf(map.get("InsIDDay")));

			}
			System.out.println(doc.asXML());
			System.out.println("呼叫SignGetData：getInsuranceId 完成" + DateUtil.getSysDate(DateUtil.ChType, DateUtil.Format_YYYYMMDD, false) + " " + DateUtil.getSysTime(false));
			return doc.asXML();
		} else
		{
			System.out.println("呼叫SignGetData：getInsuranceId 無資料" + DateUtil.getSysDate(DateUtil.ChType, DateUtil.Format_YYYYMMDD, false) + " " + DateUtil.getSysTime(false));
			return null;
			
		}

	}
	
	/**
	 * 查保單號碼 火險 傷健險 用
	 * 
	 * @param Acc 受理編號
	 * @return
	 */
	public String getInsuranceIdV2(String Acc)
	{
		System.out.println("呼叫SignGetData：getInsuranceIdV2 開始執行" + DateUtil.getSysDate(DateUtil.ChType, DateUtil.Format_YYYYMMDD, false) + " " + DateUtil.getSysTime(false));
		List data = getXmlDataV2(Acc);

		Document doc = DocumentHelper.createDocument();
		if (data != null)
		{
			Element root = doc.addElement("ROOT");

			for (int i = 0; i < data.size(); i++)
			{
				Element ret = root.addElement("Acc");
				Map map = (Map) data.get(i);
				Element r1 = ret.addElement("AccID");// 條碼號碼
				Element r2 = ret.addElement("InsID");// 保單號碼
				Element r3 = ret.addElement("InsDayA");// 生效日
				Element r4 = ret.addElement("InsDayB");// 作帳日 出單日
				Element r5 = ret.addElement("InsDayC");// 打單日
				Element r6 = ret.addElement("InsDayD");// 保險起日
				Element r7 = ret.addElement("ChannelID");// 保經代號
				Element r8 = ret.addElement("Insurance");// 單位代號
				Element r9 = ret.addElement("Department");// 險種
				Element r10 = ret.addElement("RenewalNO");// 續保單號碼
				if (map == null)
				{
					r1.setText("");
					r2.setText("");
					r3.setText("");
					r4.setText("");
					r5.setText("");
					r6.setText("");
					r7.setText("");
					r8.setText("");
					r9.setText("");
					r10.setText("");
				} else
				{
					if (map.get("AccID") != null)
					{
						r1.setText(String.valueOf(map.get("AccID")));
					} else
					{
						r1.setText("");
					}
					if (map.get("InsID") != null)
					{
						r2.setText(String.valueOf(map.get("InsID")));
					} else
					{
						r2.setText("");
					}

					if (map.get("InsDayA") != null)
					{
						r3.setText(String.valueOf(map.get("InsDayA")));
					} else
					{
						r3.setText("");
					}

					if (map.get("InsDayB") != null)
					{
						r4.setText(String.valueOf(map.get("InsDayB")));
					} else
					{
						r4.setText("");
					}

					if (map.get("InsDayC") != null)
					{
						r5.setText(String.valueOf(map.get("InsDayC")));
					} else
					{
						r5.setText("");
					}

					if (map.get("InsDayD") != null)
					{
						r6.setText(String.valueOf(map.get("InsDayD")));
					} else
					{
						r6.setText("");
					}

					if (map.get("ChannelID") != null)
					{
						r7.setText(String.valueOf(map.get("ChannelID")));
					} else
					{
						r7.setText("");
					}

					if (map.get("Insurance") != null)
					{
						r8.setText(String.valueOf(map.get("Insurance")));
					} else
					{
						r8.setText("");
					}

					if (map.get("RenewalNO") != null)
					{
						r10.setText(String.valueOf(map.get("RenewalNO")));
					} else
					{
						r10.setText("");
					}
					
					String query = "";
					if (map.get("t0d01").equals("F"))
					{
						query += "SELECT ";
						query += "CASE WHEN XZ04 IS NOT NULL THEN XZ04                                                                         ";
						query += "ELSE YT04 END AS AJENTCODE,                                                                                  ";
						query += "FPYTPF.*,FPXZPF.*                                                                                            ";
						query += "FROM FPYTPF                                                                                                  ";
						query += "LEFT JOIN FPXZPF ON XZ07||XZ08=YT01||YT02 AND XZ93='100'                                                     ";
						query += "AND XZ00||XZ01=(                                                                                             ";
						query += "    SELECT MODINS FROM                                                                                       ";
						query += "    (                                                                                                        ";
						query += "    SELECT X000||X001 MODINS,X003||X004 INSNO , X091 FROM FGFLIB/FPX0PF01 WHERE X003||X004 = '" + String.valueOf(map.get("InsID")) + "'";
						query += "    UNION                                                                                                    ";
						query += "    SELECT X000||X001 MODINS,X003||X004 INSNO , X091 FROM FGFLIB/FPX0PF02 WHERE X003||X004 = '" + String.valueOf(map.get("InsID")) + "'";
						query += "    UNION                                                                                                    ";
						query += "    SELECT X000||X001 MODINS,X003||X004 INSNO , X091 FROM FGFLIB/FPX0PF03 WHERE X003||X004 = '" + String.valueOf(map.get("InsID")) + "'";
						query += "    UNION                                                                                                    ";
						query += "    SELECT X000||X001 MODINS,X003||X004 INSNO , X091 FROM FGFLIB/FPX0PF04 WHERE X003||X004 = '" + String.valueOf(map.get("InsID")) + "'";
						query += "    UNION                                                                                                    ";
						query += "    SELECT X000||X001 MODINS,X003||X004 INSNO , X091 FROM FGFLIB/FPX0PF05 WHERE X003||X004 = '" + String.valueOf(map.get("InsID")) + "'";
						query += "    UNION                                                                                                    ";
						query += "    SELECT X000||X001 MODINS,X003||X004 INSNO , X091 FROM FGFLIB/FPX0PF06 WHERE X003||X004 = '" + String.valueOf(map.get("InsID")) + "'";
						query += "    UNION                                                                                                    ";
						query += "    SELECT X000||X001 MODINS,X003||X004 INSNO , X091 FROM FGFLIB/FPX0PF07 WHERE X003||X004 = '" + String.valueOf(map.get("InsID")) + "'";
						query += "    UNION                                                                                                    ";
						query += "    SELECT X000||X001 MODINS,X003||X004 INSNO , X091 FROM FGFLIB/FPX0PF08 WHERE X003||X004 = '" + String.valueOf(map.get("InsID")) + "'";
						query += "    UNION                                                                                                    ";
						query += "    SELECT X000||X001 MODINS,X003||X004 INSNO , X091 FROM FGFLIB/FPX0PF09 WHERE X003||X004 = '" + String.valueOf(map.get("InsID")) + "'";
						query += "    UNION                                                                                                    ";
						query += "    SELECT X000||X001 MODINS,X003||X004 INSNO , X091 FROM FGFLIB/FPX0PF10 WHERE X003||X004 = '" + String.valueOf(map.get("InsID")) + "'";
						query += "    UNION                                                                                                    ";
						query += "    SELECT X000||X001 MODINS,X003||X004 INSNO , X091 FROM FGFLIB/FPX0PF11 WHERE X003||X004 = '" + String.valueOf(map.get("InsID")) + "'";
						query += "    UNION                                                                                                    ";
						query += "    SELECT X000||X001 MODINS,X003||X004 INSNO , X091 FROM FGFLIB/FPX0PF12 WHERE X003||X004 = '" + String.valueOf(map.get("InsID")) + "'";
						query += "    UNION                                                                                                    ";
						query += "    SELECT X000||X001 MODINS,X003||X004 INSNO , X091 FROM FGFLIB/FPX0PF98 WHERE X003||X004 = '" + String.valueOf(map.get("InsID")) + "'";
						query += "    ) A ORDER BY A.X091 DESC                                                                                 ";
						query += "    FETCH FIRST 1 ROW ONLY)                                                                                  ";
						query += "WHERE YT01||YT02 = '" + String.valueOf(map.get("InsID")) + "'                                                                          ";

						Connection conn = null;
						QueryRunner runner = new QueryRunner();
						try
						{
							conn = AS400Connection.getConnection();

							Map map2 = (Map) runner.query(conn, query,	new TrimedMapHandler());
							if (map2 != null)
							{
								if (map2.get("XZ05") != null)
								{
									r9.setText(String.valueOf(map2.get("XZ05")));
								} else if (map2.get("YT05") != null)
								{
									r9.setText(String.valueOf(map2.get("YT05")));
								} else
								{
									r9.setText("");
								}
							} else
							{
								r9.setText("");
							}

						} catch (SQLException e)
						{
							e.printStackTrace();
							return null;
						} finally
						{
							AS400Connection.closeConnection(conn);
						}
					} else
					{
						query += "SELECT T117,T118 FROM FGFLIB/EPS1PF where T102||T103 = '"	+ String.valueOf(map.get("InsID")) + "'";
						
						Connection conn = null;
						QueryRunner runner = new QueryRunner();
						try
						{
							conn = AS400Connection.getConnection();

							Map map2 = (Map) runner.query(conn, query,	new TrimedMapHandler());
							if (map2 != null)
							{
								if (map2.get("T117") != null && map2.get("T118") != null)
								{
									r9.setText(String.valueOf(map2.get("T117"))	+ String.valueOf(map2.get("T118")));
								} else
								{
									r9.setText("");
								}
							} else
							{
								r9.setText("");
							}
						} catch (SQLException e)
						{
							e.printStackTrace();
							return null;
						} finally
						{
							AS400Connection.closeConnection(conn);
						}
					}
				}
			}
			System.out.println(doc.asXML());
			System.out.println("呼叫SignGetData：getInsuranceIdV2  完成" + DateUtil.getSysDate(DateUtil.ChType, DateUtil.Format_YYYYMMDD, false) + " " + DateUtil.getSysTime(false));

			return doc.asXML();
		} else
		{
			return null;
		}

	}

	/**
	 * 更新公司單位資料
	 * 
	 * @return
	 */
	public String getDepartment(String type)
	{
		GetDepartment ser = new GetDepartment();

		List data = ser.getDepartmentData(type);

		// 調整抓取類型如果是要抓公司資料把
		String DepIdDataName = "T3901";
		if (type.equals("first"))
		{
			DepIdDataName = "M202";
		}
		String DepNameDataName = "T3903";
		if (type.equals("first"))
		{
			DepNameDataName = "M203";
		}

		Document doc = DocumentHelper.createDocument();
		if (data != null)
		{
			Element root = doc.addElement("ROOT");

			for (int i = 0; i < data.size(); i++)
			{
				Element ret = root.addElement("Department");
				Map map = (Map) data.get(i);
				Element r1 = ret.addElement("DepId");
				r1.setText(String.valueOf(map.get(DepIdDataName)));
				
				Element r2 = ret.addElement("DepName");
				r2.setText(String.valueOf(map.get(DepNameDataName)));
				if (type.equals("first"))
				{
					Element r3 = ret.addElement("DepArea");
					r3.setText(String.valueOf(map.get("M206")));
				}
			}
			System.out.println(doc.asXML());
			return doc.asXML();
		} else
		{
			return null;
		}

	}

	/**
	 * 解析接收的xml字串
	 *
	 * @param xmlString 解析文件字串(XML)
	 * @return true-解析正確，false-解析有誤
	 */
	private List getXmlData(String xmlString)
	{
		if (xmlString != null && xmlString.trim().length() > 0)
		{
			try
			{
				Map mainData = new HashMap();
				SAXReader reader = new SAXReader(false);
				Document doc = reader.read(new ByteArrayInputStream(xmlString.getBytes("utf-8")));

				List list = doc.selectNodes("//ROOT/Acc");
				List insData = new ArrayList();
				
				GetInsuranceIdData ser = null;
				Map NewData = null;
				
				if (list != null)
				{
					String b1401 = "";
					boolean b1401_110 = false;//判斷受理編號是否110開頭
					ser = new GetInsuranceIdData();
					
					for (int i = 0; i < list.size(); i++)
					{
						Element element = (Element) list.get(i);
						for (Iterator j = element.elementIterator(); j
								.hasNext();)
						{
							Map e = new HashMap();
							Element element2 = (Element) j.next();
							mainData.put(element2.getName(), element2.getTextTrim());
							e.put(element2.getName(), element2.getTextTrim());
							
							//判斷是否為110開頭
							b1401_110 = element2.getTextTrim().substring(0, 3).equals("110") ? true : false;
							
							if(b1401_110){
								//若前3碼為台企特殊受編原則110，則去掉前3碼進入查詢
								b1401 = element2.getTextTrim().substring(3, element2.getTextTrim().length());
								NewData = ser.getAccData(b1401);
								
								//若查無資料，則以全碼查詢，因為板信也是用110開頭編碼，可能為板信B053,B052的案件
								if(NewData == null || NewData.isEmpty()){
									b1401 = element2.getTextTrim();
									NewData = ser.getAccData(b1401);									
								}						
								
							}else{
								b1401 = element2.getTextTrim();
								NewData = ser.getAccData(b1401);
							}
							
							e.put("InsID", NewData != null ? NewData.get("B1403") : "");
							e.put("InsIDDay", NewData != null ? NewData.get("B1404") : "");
							insData.add(e);
						}
					}
				}
				return insData;
			} catch (DocumentException e)
			{
				e.printStackTrace();

				return null;
			} catch (UnsupportedEncodingException e)
			{
				e.printStackTrace();
				return null;
			}
		} else
		{
			return null;
		}
	}

	/**
	 * 解析接收的xml字串
	 *
	 * @param xmlString 解析文件字串(XML)
	 * @return true-解析正確，false-解析有誤
	 */
	private List getXmlDataV2(String xmlString)
	{
		if (xmlString != null && xmlString.trim().length() > 0)
		{
			try
			{
				Map mainData = new HashMap();
				SAXReader reader = new SAXReader(false);
				Document doc = reader.read(new ByteArrayInputStream(xmlString.getBytes("utf-8")));

				List list = doc.selectNodes("//ROOT/Acc");
				List insData = new ArrayList();
				if (list != null)
				{

					for (int i = 0; i < list.size(); i++)
					{
						Element element = (Element) list.get(i);
						for (Iterator j = element.elementIterator(); j.hasNext();)
						{
							Map e = new HashMap();
							Element element2 = (Element) j.next();
							mainData.put(element2.getName(),element2.getTextTrim());
							e.put(element2.getName(), element2.getTextTrim());

							GetInsuranceIdV2Data ser = new GetInsuranceIdV2Data();
							Map NewData = ser.getAccData(element2.getTextTrim());

							insData.add(NewData);
						}
					}
				}
				return insData;
			} catch (DocumentException e)
			{
				e.printStackTrace();

				return null;
			} catch (UnsupportedEncodingException e)
			{
				e.printStackTrace();
				return null;
			}
		} else
		{
			return null;
		}
	}


	/**
	 * 提供寫入辨視出的條碼號
	 * @param channel
	 * @param userid
	 * @param Acc
	 * @return
	 */
	public String sendAccNumbers(String channel , String userid , String Acc)
	{
		System.out.println("呼叫SignGetData：sendAccNumbers 寫入辨視出的條碼號。開始執行" + DateUtil.getSysDate(DateUtil.ChType, DateUtil.Format_YYYYMMDD, false) + " " + DateUtil.getSysTime(false));
		List data = getXmlDataForInsert(channel , userid , Acc);

		Document doc = DocumentHelper.createDocument();
		if (data != null)
		{
			Element root = doc.addElement("ROOT");

			for (int i = 0; i < data.size(); i++)
			{
				Element ret = root.addElement("Acc");
				Map map = (Map) data.get(i);
				Element r1 = ret.addElement("AccID");// 條碼號碼
				Element r2 = ret.addElement("State");// 狀態
				Element r3 = ret.addElement("Msg");// 說明
				if (map == null)
				{
					r1.setText("");
					r2.setText("");
					r3.setText("");
				} else
				{
					if (map.get("AccID") != null)
					{
						r1.setText(String.valueOf(map.get("AccID")));
					} else
					{
						r1.setText("");
					}
					if (map.get("State") != null)
					{
						r2.setText(String.valueOf(map.get("State")));
					} else
					{
						r2.setText("");
					}

					if (map.get("Msg") != null)
					{
						r3.setText(String.valueOf(map.get("Msg")));
					} else
					{
						r3.setText("");
					}
				}
			}
			
			System.out.println(doc.asXML());
			System.out.println("呼叫SignGetData：sendAccNumbers 完成" + DateUtil.getSysDate(DateUtil.ChType, DateUtil.Format_YYYYMMDD, false) + " " + DateUtil.getSysTime(false));

			return doc.asXML();
		
		}else
		{
			return null;
		}

	}

	/**
	 * 解析接收的xml字串，並寫入受理編號檔
	 * @param channel
	 * @param userid
	 * @param xmlString
	 * @return
	 */
	private List getXmlDataForInsert(String channel , String userid , String xmlString)
	{
		if (xmlString != null && xmlString.trim().length() > 0)
		{
			try
			{
				Map mainData = new HashMap();
				SAXReader reader = new SAXReader(false);
				Document doc = reader.read(new ByteArrayInputStream(xmlString.getBytes("utf-8")));

				List list = doc.selectNodes("//ROOT/Acc");
				List insData = new ArrayList();
				boolean isDataExit = false;
				int count = 0;
				String accno = "";
				
				if (list != null)
				{

					for (int i = 0; i < list.size(); i++)
					{
						Element element = (Element) list.get(i);
						for (Iterator j = element.elementIterator(); j.hasNext();)
						{
							Map e = new HashMap();
							Element element2 = (Element) j.next();
							accno = element2.getTextTrim();
							e.put(element2.getName(), accno);

							//查詢條碼是否存在
							isDataExit = isExistWB14PF(accno);
							//寫入條碼資料
							if(isDataExit == false){
								count = insertWB14PF(accno, userid, channel);
								
								if(count > 0 ){//成功
									e.put("State", "Y");
									e.put("Msg", "");
								}else{//失敗
									e.put("State", "N");
									e.put("Msg", "條碼資料寫入錯誤");
								}
							}else{
								e.put("State", "N");
								e.put("Msg", "條碼資料已存在");
							}
								
							insData.add(e);
						}
					}
				}
				return insData;
			} catch (DocumentException e)
			{
				e.printStackTrace();

				return null;
			} catch (UnsupportedEncodingException e)
			{
				e.printStackTrace();
				return null;
			}
		} else
		{
			return null;
		}
	}

	/**
	 * 查詢條碼號是否存在
	 * @param b1401
	 * @return
	 */
	private boolean isExistWB14PF(String b1401){
		
		boolean ret = false;
		Map mp = null;
		String sql = "SELECT * FROM WB14PF WHERE B1401 = ?";
		
    	Connection conn = null;
    	QueryRunner runner = new QueryRunner();
    	
		try
		{	
			conn = AS400Connection.getConnection();
			mp = (Map) runner.query(conn, sql, b1401, new TrimedMapHandler());
			
			if(mp != null && !mp.isEmpty())
				ret = true;
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}  
		finally 
		{
			AS400Connection.closeConnection(conn);
		}

		return ret ; 
	}
	
	/**
	 * 寫入受理編號檔
	 * @param b1401
	 * @param user
	 * @param channel
	 * @return
	 */
	private int insertWB14PF(String b1401 , String user , String channel){
		
		String sql = "INSERT INTO WB14PF (B1401,B1402,B1405,B1494,B1495,B1496) VALUES (?,?,?,?,?,?) ";
		
		int ret = 0;
		
		String sysdate = DateUtil.getSysDate(DateUtil.ChType, DateUtil.Format_YYYYMMDD, false);
		String systime = DateUtil.getSysTime();
		
		String[] param = new String[6];
		param[0] = b1401;
		param[1] = sysdate;
		param[2] = channel;
		param[3] = sysdate;
		param[4] = systime;
		param[5] = user;

    	Connection conn = null;
    	QueryRunner runner = new QueryRunner();
    	
		try
		{	
			conn = AS400Connection.getConnection();
			ret = runner.update(conn, sql, param);
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

}
