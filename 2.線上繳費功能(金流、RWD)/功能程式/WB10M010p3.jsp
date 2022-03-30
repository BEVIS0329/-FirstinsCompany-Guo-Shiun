<%-- 網投繳費服務第三頁 --%>
<%@ page contentType="text/html;charset=UTF-8"%>

<%@ taglib uri="/WEB-INF/tlds/asi/asi.tld" prefix="asi" %>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ page contentType="text/html;charset=UTF-8" isELIgnored="false"%>
<%@ page import="com.asi.common.struts.AsiActionForm" %>
<%@ page import="com.asi.common.util.JspUtil"%>
<%@ page import="com.asi.common.GlobalKey"%>
<%@ page import="com.asi.kyc.common.KycGlobal"%>
<%@ page import="com.asi.common.security.UserInfo"%>
<%@ page import="com.asi.common.util.MathUtil"%>
<%@ page import="com.asi.common.util.DateUtil"%>
<%@ page import="java.util.*"%>
<%@ page import="java.text.SimpleDateFormat"%>
<%@ page import="com.asi.kyc.common.SystemParam"%>
<%@ page import="com.asi.kyc.wb10.forms.WB10M010f"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>

<%/** 此區塊一定要寫在asi:form的外面 **/%>
<%
	//設定此畫面主要的action
	String mainAction = "/WB10M0101";
	//取得action的form name
	String formName = JspUtil.getFormName(pageContext,mainAction);
	//目前畫面名稱
	String source = "WB10M010p3";
	//找出系統設定之密碼最少長度
	//String minPwdLen = ConfigUtil.getConfig(pageContext,GlobalKey.MIN_PWD_LEN);
	//若長度有設定而且不為0時，就要檢查密碼長度
	//boolean check = minPwdLen != null && !minPwdLen.equals("0");

	//如果是用LDAP做ID控管就不顯示"密碼"之欄位
	//boolean show2 = !ManagerUtil.getManager(pageContext,GlobalKey.SECURITY_MANAGER).getClass().equals(LDAP4AD_DBSecurityManager.class);	
	
	UserInfo ui = (UserInfo) session.getAttribute(GlobalKey.USER_INFO);
	boolean isNew = (ui.getInfo("FAKE")!=null);
	
	WB10M010f fm = (WB10M010f) session.getAttribute("WB10M010f");

	String ecom_tel = SystemParam.getParam("ECOM_TEL"); // 服務電話
%>

<%//程式標頭區塊 %>

	<%/** 主要編輯區塊 begin **/%>
	<asi:form action="<%= mainAction %>">
	
	<% /** 此程式區塊一定要寫在asi:form之後 **/
		//取出form bean (固定寫法)
		AsiActionForm asiForm = JspUtil.getFormBean(pageContext);
		WB10M010f form = (WB10M010f) asiForm;
		String tradecode=form.getTradecode() != null ? form.getTradecode() : "";
		String instotal=form.getInstotal() != null ? form.getInstotal() : "";
		String cardname=form.getCardname() != null ? form.getCardname() : "";
		String cardid=form.getCardid() != null ? form.getCardid() : "";
		String cardrelselect=form.getCardrelselect() != null ? form.getCardrelselect() : "";
		
	%>
	<%//欲隱藏回傳的欄位%>
	<input type="hidden" id="tradecode" name="tradecode" value="<%=tradecode%>">
	<input type="hidden" id="instotal" name="instotal" value="<%=instotal%>">
	<input type="hidden" id="cardname" name="cardname" value="<%=cardname%>">
	<input type="hidden" id="cardid" name="cardid" value="<%=cardid%>">
	<input type="hidden" id="cardrelselect" name="cardrelselect" value="<%=cardrelselect%>">
	
	<%//<asi:smartfield type="hidden" property="pjcode"/>%>
	
	<%//加name屬性，表示值是由其它的物件或DBO物件取得%>

	<!-- Page Title -->

    <div class="container">
      <!-- Example row of columns -->
		<div class="row">
		<div class="col-sm-12">
			<div class="panel panel-primary">
			  	<div class="panel-heading"><h3 class="panel-title" style="text-align: center;">線上信用卡繳費-刷卡頁</h3></div>
                <div class="panel-body padding_divider">


					
					<div class="row custom_divider_noline">
					
						<div class="col-md-12">請輸入信用卡卡號</div>
						
						<div class="col-md-3">
							<input placeholder="XXXX" class="form-control" id="card1" name="card1" type="text" onkeyup="Setfocus(0);" runat="server" style="TEXT-TRANSFORM:uppercase" maxlength="4"/>
						</div>
							
						<div class="col-md-3">
							<input placeholder="XXXX" class="form-control" id="card2" name="card2" type="text" onkeyup="Setfocus(1);" runat="server" style="TEXT-TRANSFORM:uppercase" maxlength="4"/>
						</div>
						
						<div class="col-md-3">
							<input placeholder="XXXX" class="form-control" id="card3" name="card3" type="text" onkeyup="Setfocus(2);" runat="server" style="TEXT-TRANSFORM:uppercase" maxlength="4"/>
						</div>
						
						<div class="col-md-3">
							<input placeholder="XXXX" class="form-control" id="card4" name="card4" type="text" onkeyup="Setfocus(3);" runat="server" style="TEXT-TRANSFORM:uppercase" maxlength="4"/>
						</div>
													
					</div>
								
                    
                    <div class="row custom_divider_noline">

                        <div class="col-md-12">
								請輸入信用卡到期月年
						</div>
						<div class="col-md-6">
							<select class="form-control" id="cardMM" name="cardMM">
								<option value="N">到期月</option>
								<option value="01">01</option>
								<option value="02">02</option>
								<option value="03">03</option>
								<option value="04">04</option>
								<option value="05">05</option>
								<option value="06">06</option>
								<option value="07">07</option>
								<option value="08">08</option>
								<option value="09">09</option>
								<option value="10">10</option>
								<option value="11">11</option>
								<option value="12">12</option>
							</select>
						</div>
						
						<div class="col-md-6">
							<select class="form-control" name="cardYY" id="cardYY">
								<option value="N">到期年</option>
							</select>
						</div>
						
						<script>
							var today = new Date();
							var year = today.getFullYear();
							//先用getElementById取得select的id
							var s = document.getElementById('cardYY');
							//這兩個可以用其他方式取得，端看怎麼使用
							var t = Number(year);
									
							//最後產出年份
							for (var i = 0;i<50;i++){
								var new_option = new Option(t + i, t + i);
								s.options.add(new_option);
							}
						</script>
    
                    </div>

			</div>
			
			<div class="panel-footer text-center">
				<input class="btn btn-lg" type="reset" value="清除付款資料">
				<button class="btn btn-lg" type="button" onclick="process(7);">確定付款 </button>
			</div>

			</div>			
		</div>
		</div>

	</div>

		
	</asi:form>

<%/** 主要編輯區塊 end **/%>

<%/** Popup視窗設定區塊 begin **/%>	
<%//回呼函式設定，由自訂視窗所呼叫%>
<script language="JavaScript" type="text/javascript">
</script>
<%/** Popup視窗設定區塊 end **/%>
<%/** 自訂或代碼視窗區塊 begin **/%>
<script language="JavaScript" type="text/javascript">
</script>
<%/** 自訂或代碼視窗區塊 end **/%>

<%/** 動作及畫面檢核區塊 begin **/%>
<script language="JavaScript" type="text/javascript">  
  	
	$(document).ready(function(){
		with(document.<%=formName%>) {
			
		}		
	});

  	function process(arg1) {
    	with(document.<%=formName%>) {
    		setformname('<%=formName%>');
    		source.value = "<%=source%>";
    		actionCode.value = arg1;
    		
			if ( checkform() == false)  return false;

			submit();
    	}
    }
    <%//畫面欄位檢核%>
    function checkform(){
		with(document.<%=formName%>) {

			//到期月
			if(cardMM.value=='N'){
				alert('請選擇[到期月]欄位');
				cardMM.focus();
				return false;
			}
			//到期年
			if(cardYY.value=='N'){
				alert('請選擇[到期年]欄位');
				cardYY.focus();
				return false;
			}
			//信用卡號
			if(card1.value=='' || card2.value=='' ||card3.value=='' ||card4.value==''){
				alert('請輸入[信用卡卡號]欄位');
				return false;
			}
			
		}
		return true;
	}
    
    function Setfocus(txtid) {
	 	if (txtid != 3){
	 		var txtname = ["card1","card2","card3","card4"]
			if (document.getElementById(txtname[txtid]).value.length == 4) {
				document.getElementById(txtname[txtid + 1]).focus();
			}
	 	}
	}
	
</script>