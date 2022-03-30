/*
 * $Header: D:/Repositories/KYC2/JavaSource/com/com/asi/kyc/common/utils/HitrustUtil.java,v 1.3 2006/11/02 08:25:10 cvsuser Exp $
 *
 * Copyright (c) 2000-2004 Asgard System, Inc. Taipei, Taiwan. All rights
 * reserved.
 * 
 * This software is the confidential and proprietary information of Asgard
 * System, Inc. ("Confidential Information"). You shall not disclose such
 * Confidential Information and shall use it only in accordance with the terms
 * of the license agreement you entered into with Asgard.
 * 
 */
package com.asi.kyc.common.utils;

import java.io.IOException;
import java.net.MalformedURLException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.util.RequestUtils;

import com.asi.common.exception.AsiException;
import com.asi.common.exception.UserException;
import com.asi.common.manager.IConfigManager;
import com.asi.common.util.DateUtil;
import com.asi.common.util.ManagerUtil;
import com.asi.kyc.common.SystemParam;
import com.hitrust.b2ctoolkit.b2cpay.B2CPayAuth;
import com.hitrust.b2ctoolkit.b2cpay.B2CPayAuthSSL;
import com.hitrust.b2ctoolkit.b2cpay.B2CPayOther;

/**
 * 和Hitrust相關的功能元件
 * 
 * @author ：eric_w
 * @version ：$Revision: 18 $ $Date: 2015-08-20 15:13:06 +0800 (Thu, 20 Aug 2015) $
 */
public class HitrustUtil {
	/**
	 * 產生呼叫Hitrust的網頁
	 * 
	 * @param orderNumber
	 *            訂單編號
	 * @param orderAccount
	 *            訂單金額(不用先乘100再傳入)，統一在這裡乘上100
	 * @param orderDesc
	 *            訂單說明 <BR>
	 *            新保流程:<BR>
	 *            訂單說明 => "新保" + 商品內容(16位) + 被保險人身份證字號/統編(10位) + 被保險人姓名(10位)<BR>
	 *            旅平險為 => "新保" + 商品內容(16位) + 要保人身份證字號/統編(10位) + 要保人姓名(10位)<BR>
	 *            <BR>
	 *            續保/斷保流程:<BR>
	 *            訂單說明 => "續保" + 原保單號碼(16位) + 被保險人身份證字號/統編(10位) + 被保險人姓名(10位)<BR>
	 *            旅平險為 => "續保" + 原保單號碼(16位) + 要保人身份證字號/統編(10位) + 要保人姓名(10位)<BR>
	 * 
	 * @param returnPath
	 *            指定接續網址
	 * @param servlet
	 *            ActionServlet
	 * @param request
	 *            HttpServletRequest
	 * @param response
	 *            HttpServletResponse
	 * @throws AsiException
	 */
	public static void callHitrust(String orderNumber, double orderAccount,
			String orderDesc, String returnPath, HttpServlet servlet,
			HttpServletRequest request, HttpServletResponse response)
			throws AsiException {
		// PrintWriter out = null;
		String storeid = null; // 商店代號
		// String hitrutsServerPath = null; //Hitrust的授權Server
		String root = null;
		String depositFlag = null; // 自動請款標記
		String queryFlag = null; // 訂單資料

		// 取得參數
		IConfigManager conf = (IConfigManager) ManagerUtil
				.getConfigManager(servlet);
		
		//20210219因應變更商店代號，避免無法授權/請款/取消等作業
		String storedid_changeDate = SystemParam.getParam("HISTOREID_CHD");
		String sysdate = DateUtil.getSysDate(DateUtil.ChType,DateUtil.Format_YYYYMMDD, false);
		if(Integer.parseInt(storedid_changeDate) > Integer.parseInt(sysdate))
			storeid = "80136";
		else
			storeid = SystemParam.getParam("STOREID");
		
		// hitrutsServerPath = conf.getConfig("hitrust.server.path");
		depositFlag = conf.getConfig("hitrust.deposit");
		queryFlag = conf.getConfig("hitrust.queryflag");

		// 取得目前使用者進入用的完整Http路徑
		try {
			root = RequestUtils.serverURL(request).toString();
			root += request.getContextPath();
			root = response.encodeURL(root);
		} catch (MalformedURLException e) {
			throw new UserException("errors.1000", e);
		}

		String updatePath = conf.getConfig("hitrust.server.updatepath")
				+ request.getContextPath() + "/TransactionUpdate.do"; // 交易結果網址

		/** ************* 使用 HiTrust toolkit 進行線上金流 *************** */
		B2CPayAuth auth = new B2CPayAuth();
		auth.setStoreId(storeid);
		auth.setOrderNo(orderNumber);
		auth.setOrderDesc(orderDesc);
		auth.setAmount(String.valueOf((int) (orderAccount * 100)));
		auth.setDepositFlag(depositFlag); // ‘1’: Sale交易: 自動請款 ‘0’: 一般交易
											// *商家欲使用自動請款交易時,需先經過銀行同意。
		auth.setQueryFlag(queryFlag); // ‘1’: 詳細資料:當您啟動本參數時，TrustPay
										// Server會將本筆交易的詳細資料以POST的方式送至UpdateURL
										// ‘0’: 一般資料
		auth.setReturnURL(root + returnPath);
		auth.setUpdateURL(updatePath);
		auth.transaction();

		try {
			if (auth.getRetCode().equals("00")) {
				response.sendRedirect(auth.getToken());
			} else {
				throw new UserException("errors.1000", "錯誤的回傳值："
						+ auth.getRetCode());

				/*
				 * UpdateState.updateKL80_HitrustStop(orderNumber,
				 * tx_controller);//更新交易狀態碼 99-未開放線上刷卡 response.sendRedirect(
				 * RequestUtils.serverURL(request) +
				 * request.getContextPath()+"/web/wb1/HITRUSTSTOPTIME_PAGE.html");
				 */
			}
		} catch (IOException e) {
			throw new UserException("errors.1000", e);
		}
	}

	/**
	 * 請款（Capture）
	 * 
	 * @param orderNumber
	 *            訂單編號
	 * @param orderAccount
	 *            訂單金額(不用先乘100再傳入)，統一在這裡乘上100
	 * @throws AsiException
	 */
	public static String capture(String orderNumber, double orderAccount,
			HttpServlet servlet, HttpServletRequest request)
			throws AsiException {
		String storeid = null; // 商店代號
		String queryFlag = null; // 訂單資料

		// 取得參數
		IConfigManager conf = (IConfigManager) ManagerUtil
				.getConfigManager(servlet);
		storeid = conf.getConfig("hitrust.storeid");
		queryFlag = conf.getConfig("hitrust.queryflag");

		/** ************* 使用 HiTrust toolkit 進行請款 *************** */
		B2CPayOther trx = new B2CPayOther();
		trx.setStoreId(storeid);
		trx.setType(trx.CAPTURE);
		trx.setOrderNo(orderNumber);
		trx.setAmount(String.valueOf((int) (orderAccount * 100)));
		trx.setQueryFlag(queryFlag);
		trx.transaction();

		try {
			if (trx.getRetCode().equals("00")) {
				// response.sendRedirect(auth.getToken());
			} else {
				throw new UserException("errors.1000", "錯誤的回傳值："
						+ trx.getRetCode());
			}
		} catch (Exception e) {
			throw new UserException("errors.1000", e);
		}
		return trx.getRetCode();
	}
	
	/**
	 * 取消授權(Authorization Reverse)
	 * @param number 訂單編號
	 * @return 交易結果('00'為成功)
	 */
	public static String cancelAuth(String orderNumber , String storeid)
	{
		B2CPayOther b2cPayOther = new B2CPayOther();
		b2cPayOther.setStoreId(storeid);
		b2cPayOther.setType(b2cPayOther.RE_AUTH);// 取消授權
		b2cPayOther.setOrderNo(orderNumber);
		b2cPayOther.setQueryFlag("1");
		b2cPayOther.transaction();

		String retCode = b2cPayOther.getRetCode();
		return retCode;
	}
	
	/**
	 * 直接取授權(AuthorizationSSL)
	 * @param orderNumber 訂單號碼
	 * @param orderAmount 交易金額
	 * @param orderDesc 訂單說明
	 * @param cardNo 信用卡號
	 * @param expireDate 到期日
	 * @param servlet
	 * @return [0]:交易結果('00'為成功) [1]:授權碼
	 * @throws AsiException
	 */
	public static String[] callHitrustSSL(String orderNumber, double orderAmount,
			String orderDesc,String cardNo,String expireDate, HttpServlet servlet)
			throws AsiException {
		String storeid =SystemParam.getParam("STOREID");// 商店代號
		IConfigManager conf = (IConfigManager) ManagerUtil.getConfigManager(servlet);
		String depositFlag = conf.getConfig("hitrust.deposit"); // 自動請款標記
		String queryFlag = conf.getConfig("hitrust.queryflag");//查詢標記
		
		B2CPayAuthSSL authSSL = new B2CPayAuthSSL();
		authSSL.setStoreId(storeid);
		authSSL.setOrderNo(orderNumber);
		authSSL.setOrderDesc(orderDesc);
		authSSL.setDepositFlag(depositFlag);
		authSSL.setQueryFlag(queryFlag);
		authSSL.setAmount(String.valueOf((int) (orderAmount * 100)));//須包含兩位小數 Ex.若輸入值為100,則代表1 元
		authSSL.setPan(cardNo);//卡號
		authSSL.setExpiry(expireDate);//到期日
		
		authSSL.transaction();
		
		String[] ret = new String[2];
		ret[0] = authSSL.getRetCode();
		ret[1] = authSSL.getAuthCode();
		
		return ret;
	}
	
	/**
	 * 信用卡持卡人驗證
	 * @param orderNumber 訂單號碼
	 * @param orderDesc 訂單說明
	 * @param cardNo 信用卡號
	 * @param expireDate 到期日
	 * @param ownerId 持卡人ID
	 * @param servlet
	 * @return
	 * @throws AsiException
	 */
	public static String[] callHitrustOwnerAuth(String orderNumber,
			String orderDesc,String cardNo,String expireDate,String ownerId, HttpServlet servlet)
			throws AsiException {
		String storeid =SystemParam.getParam("STOREID");// 商店代號
		IConfigManager conf = (IConfigManager) ManagerUtil.getConfigManager(servlet);
		String queryFlag = conf.getConfig("hitrust.queryflag");//查詢標記
		
		B2CPayAuthSSL authSSL = new B2CPayAuthSSL();
		authSSL.setStoreId(storeid);
		authSSL.setOrderNo(orderNumber);
		authSSL.setOrderDesc(orderDesc);
		authSSL.setDepositFlag("0");//不可為自動請款交易
		authSSL.setQueryFlag(queryFlag);
		authSSL.setAmount("000");//須包含兩位小數 Ex.若輸入值為100,則代表1 元,固定放0元
		authSSL.setPan(cardNo);//卡號
		authSSL.setExpiry(expireDate);//到期日
		
		//驗證持卡人ID
		authSSL.setE36(ownerId);
		authSSL.setE42("Y");
		
		authSSL.transaction();
		
		String[] ret = new String[2];
		ret[0] = authSSL.getRetCode();
		ret[1] = authSSL.getAuthCode();
		
		return ret;
	}
	
	/**
	 * 官網線上繳費功能使用-直接取授權(AuthorizationSSL)
	 * @param orderNumber 訂單號碼
	 * @param orderAmount 交易金額
	 * @param orderDesc 訂單說明
	 * @param cardNo 信用卡號
	 * @param expireDate 到期日
	 * @param servlet
	 * @return [0]:交易結果('00'為成功) [1]:授權碼
	 * @throws AsiException
	 */
	public static String[] callHitrustSSL_OL(String orderNumber, double orderAmount,
			String orderDesc,String cardNo,String expireDate, HttpServlet servlet)
			throws AsiException {
		String storeid =SystemParam.getParam("STOREID_OL");
		IConfigManager conf = (IConfigManager) ManagerUtil.getConfigManager(servlet);
		//String depositFlag = conf.getConfig("hitrust.deposit"); // 自動請款標記
		String depositFlag = "1"; // 自動請款標記：自動請款 -> "1"，一般交易 -> "0"
		String queryFlag = conf.getConfig("hitrust.queryflag");//查詢標記
		
		B2CPayAuthSSL authSSL = new B2CPayAuthSSL();
		authSSL.setStoreId(storeid);
		authSSL.setOrderNo(orderNumber);
		authSSL.setOrderDesc(orderDesc);
		authSSL.setDepositFlag(depositFlag);
		authSSL.setQueryFlag(queryFlag);
		authSSL.setAmount(String.valueOf((int) (orderAmount * 100)));//須包含兩位小數 Ex.若輸入值為100,則代表1 元
		authSSL.setPan(cardNo);//卡號
		authSSL.setExpiry(expireDate);//到期日
		
		authSSL.transaction();
		
		String[] ret = new String[2];
		ret[0] = authSSL.getRetCode();
		ret[1] = authSSL.getAuthCode();
		
		return ret;
	}
	
	/**
	 * 官網線上繳費功能使用-信用卡持卡人驗證
	 * @param orderNumber 訂單號碼
	 * @param orderDesc 訂單說明
	 * @param cardNo 信用卡號
	 * @param expireDate 到期日
	 * @param ownerId 持卡人ID
	 * @param servlet
	 * @return
	 * @throws AsiException
	 */
	public static String[] callHitrustOwnerAuth_OL(String orderNumber,
			String orderDesc,String cardNo,String expireDate,String ownerId, HttpServlet servlet)
			throws AsiException {
		String storeid =SystemParam.getParam("STOREID_OL");// 商店代號
		IConfigManager conf = (IConfigManager) ManagerUtil.getConfigManager(servlet);
		String queryFlag = conf.getConfig("hitrust.queryflag");//查詢標記
		
		B2CPayAuthSSL authSSL = new B2CPayAuthSSL();
		authSSL.setStoreId(storeid);
		authSSL.setOrderNo(orderNumber);
		authSSL.setOrderDesc(orderDesc);
		authSSL.setDepositFlag("0");//不可為自動請款交易
		authSSL.setQueryFlag(queryFlag);
		authSSL.setAmount("000");//須包含兩位小數 Ex.若輸入值為100,則代表1 元,固定放0元
		authSSL.setPan(cardNo);//卡號
		authSSL.setExpiry(expireDate);//到期日
		
		//驗證持卡人ID
		authSSL.setE36(ownerId);
		authSSL.setE42("Y");
		
		authSSL.transaction();
		
		String[] ret = new String[2];
		ret[0] = authSSL.getRetCode();
		ret[1] = authSSL.getAuthCode();
		
		return ret;
	}
}
