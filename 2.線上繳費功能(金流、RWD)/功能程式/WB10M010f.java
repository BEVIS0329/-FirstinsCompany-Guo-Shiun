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
package com.asi.kyc.wb10.forms;

import com.asi.common.struts.AsiActionForm;

/**
 * 網投繳費服務
 * 
 * @author Vincent
 * @Create Date：2021/7/15
 */
public class WB10M010f extends AsiActionForm
{
	/** 交易序號*/
	private String tradecode = "";


	/** 總保費 */
	private String instotal = "";

	/** 持卡人ID */
	private String cardid = "";
	
	/** 持卡人姓名 */
	private String cardname = "";
	
	/** 與被保人關係 */
	private String cardrelselect = "";
	
	/** 有效年月-年 */
	private String cardYY = " ";
	
	/** 有效年月-月 */
	private String cardMM =" ";
	
	/** 信用卡號1 */
	private String card1 = "";
	
	/** 信用卡號2 */
	private String card2 = "";
	
	/** 信用卡號3 */
	private String card3 = "";
	
	/** 信用卡號4 */
	private String card4 = "";

	/**
	 * @return the tradecode
	 */
	public String getTradecode() {
		return tradecode;
	}

	/**
	 * @param tradecode the tradecode to set
	 */
	public void setTradecode(String tradecode) {
		this.tradecode = tradecode;
	}

	/**
	 * @return the instotal
	 */
	public String getInstotal() {
		return instotal;
	}

	/**
	 * @param instotal the instotal to set
	 */
	public void setInstotal(String instotal) {
		this.instotal = instotal;
	}

	/**
	 * @return the cardid
	 */
	public String getCardid() {
		return cardid;
	}

	/**
	 * @param cardid the cardid to set
	 */
	public void setCardid(String cardid) {
		this.cardid = cardid;
	}

	/**
	 * @return the cardname
	 */
	public String getCardname() {
		return cardname;
	}

	/**
	 * @param cardname the cardname to set
	 */
	public void setCardname(String cardname) {
		this.cardname = cardname;
	}

	/**
	 * @return the cardrelselect
	 */
	public String getCardrelselect() {
		return cardrelselect;
	}

	/**
	 * @param cardrelselect the cardrelselect to set
	 */
	public void setCardrelselect(String cardrelselect) {
		this.cardrelselect = cardrelselect;
	}

	/**
	 * @return the cardYY
	 */
	public String getCardYY() {
		return cardYY;
	}

	/**
	 * @param cardYY the cardYY to set
	 */
	public void setCardYY(String cardYY) {
		this.cardYY = cardYY;
	}

	/**
	 * @return the cardMM
	 */
	public String getCardMM() {
		return cardMM;
	}

	/**
	 * @param cardMM the cardMM to set
	 */
	public void setCardMM(String cardMM) {
		this.cardMM = cardMM;
	}

	/**
	 * @return the card1
	 */
	public String getCard1() {
		return card1;
	}

	/**
	 * @param card1 the card1 to set
	 */
	public void setCard1(String card1) {
		this.card1 = card1;
	}

	/**
	 * @return the card2
	 */
	public String getCard2() {
		return card2;
	}

	/**
	 * @param card2 the card2 to set
	 */
	public void setCard2(String card2) {
		this.card2 = card2;
	}

	/**
	 * @return the card3
	 */
	public String getCard3() {
		return card3;
	}

	/**
	 * @param card3 the card3 to set
	 */
	public void setCard3(String card3) {
		this.card3 = card3;
	}

	/**
	 * @return the card4
	 */
	public String getCard4() {
		return card4;
	}

	/**
	 * @param card4 the card4 to set
	 */
	public void setCard4(String card4) {
		this.card4 = card4;
	}

}
