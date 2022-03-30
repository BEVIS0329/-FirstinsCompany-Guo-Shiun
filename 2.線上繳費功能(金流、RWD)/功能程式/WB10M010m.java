/*
 *
 * Copyrights (c) 2005 The First Insurance Co, Ltd. All Rights Reserved. 
 * 
 * This software is the confidential and proprietary information of  
 * The First Insurance Co, Ltd. ("Confidential Information").  
 * You shall not disclose such Confidential Information and shall use 
 * it only in accordance with the terms of the license agreement you 
 * entered into with The First Insurance Co, Ltd. 
 * 
 */
package com.asi.kyc.wb10.models;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.asi.common.AsiModel;
import com.asi.common.dbo.TransactionControl;
import com.asi.common.exception.AsiException;
import com.asi.common.struts.AsiActionForm;
import com.asi.kyc.wb10.dao.WB10M010Dao;
import com.asi.kyc.wb10.forms.WB10M010f;


/**
 * <!--程式說明寫在此-->
 * 
 * @author ：Vincent
 * @version ：$Revision$ $Date$<br>
 *          <p>
 * 
 *          <pre>
 * 存放路徑	：$Header$  
 * 建立日期	：2021/7/15
 * 異動註記	：
 * </pre>
 * 
 *          </p>
 */
public class WB10M010m extends AsiModel
{

	private static Log logger = LogFactory.getLog(WB10M010m.class);

	private WB10M010f mform;


	private TransactionControl tx_controller;

	public WB10M010m(TransactionControl tx_controller,
			HttpServletRequest request, AsiActionForm form)
	{
		super(tx_controller, request, form);
		this.tx_controller = tx_controller;
	}

	//初始化
	public void init() throws AsiException
	{

		mform = new WB10M010f();
		// 把form做拷貝
		try
		{
			BeanUtils.copyProperties(mform, getForm());
		} catch (InvocationTargetException e)
		{
			if (logger.isErrorEnabled())
			{
				logger.error(e);
			}
		} catch (IllegalAccessException e)
		{
			if (logger.isErrorEnabled())
			{
				logger.error(e);
			}
		}
		setMainForm(mform);

	}

	
	/**
	 * 查詢未繳費保單明細
	 * @param id
	 */
	public List<Map> queryInsurancePolicy(String id)throws AsiException
	{
		
		WB10M010Dao dao = new WB10M010Dao();
		
		List<Map> ret = null;
		
		ret = dao.queryInsurancePolicy(id);
				
		return ret ;
		
	}
	
	/**
	 * 判斷被保人ID或要保人ID與持卡人ID是否符合(以交易序號找)
	 * @param tradecode
	 */
	public List<Map> getInsuranceID(String tradecode) throws AsiException
	{
		WB10M010Dao dao = new WB10M010Dao();
		
		List<Map> ret = null;
		
		ret = dao.getInsuranceID(tradecode);
				
		return ret ;
	}
	
	/**
	 * 查詢寫檔資料(以交易序號找)
	 * @param tradecode
	 */
	public List<Map> queryData(String tradecode)throws AsiException
	{
		WB10M010Dao dao = new WB10M010Dao();
		
		List<Map> ret = null;
		
		ret = dao.queryData(tradecode);
				
		return ret ;
	}
	
	/**
	 * 取得授權後更新MBC3PF
	 * @param data
	 */
	public void updateMBC3PF(Map data) throws AsiException
	{
		WB10M010Dao dao = new WB10M010Dao();
		dao.updateMBC3PF(data);
	}
	
	/**
	 * 取得授權後新增PT15PF
	 * @param data
	 * @param cardNumber
	 * @param auth
	 * @param ip
	 * @param num
	 */
	public void insertPT15PF(Map data,String cardNumber,String auth,String ip,int num) throws AsiException
	{
		WB10M010Dao dao = new WB10M010Dao();
		dao.insertPT15PF(data, cardNumber, auth,ip,num);
	}
	
	/**
	 * CALL FAS22PRC 後 取得繳費號碼檔資料
	 * @param t8501 大險別
	 * @param t8502 繳費號碼
	 * @return
	 */
	public Map getPt85pfInfo(String t8501,String t8502){
		
		Map ret = null;				
		
		WB10M010Dao dao = new WB10M010Dao();
		ret = dao.getPt85pfInfo(t8501,t8502);
		
		return ret;
	}
	
	/**
	 * CALL FAS22PRC 第二次失敗後 更新PT85PF的繳費狀態T8522及暫收資料來源T8524
	 * @param t8501 大險別
	 * @param t8502 繳費號碼
	 * @return
	 */
	public void updatePt85pf(String t8501,String t8502){
		
		WB10M010Dao dao = new WB10M010Dao();
		dao.updatePt85pf(t8501,t8502);

	}
	
	@Override
	public void destroy()
	{
		// TODO Auto-generated method stub

	}
}
