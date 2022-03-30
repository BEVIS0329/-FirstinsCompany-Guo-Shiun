<%-- 網投繳費服務第二頁 --%>
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
	String source = "WB10M010p2";
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
	
	String checkid=request.getAttribute("checkid") != null ? request.getAttribute("checkid").toString() : "";
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
	%>
	<%//欲隱藏回傳的欄位%>
	<input type="hidden" id="tradecode" name="tradecode" value="<%=tradecode%>">
	<input type="hidden" id="instotal" name="instotal" value="<%=instotal%>">
	
	<%//<asi:smartfield type="hidden" property="pjcode"/>%>
	
	<%//加name屬性，表示值是由其它的物件或DBO物件取得%>

	<!-- Page Title -->

    <div class="container">
      <!-- Example row of columns -->
		<div class="row">
		<div class="col-sm-12">
			<div class="panel panel-primary">
			  	<div class="panel-heading"><h3 class="panel-title" style="text-align: center;">請輸入持卡人身分</h3></div>
                <div class="panel-body padding_divider">

					<div class="row custom_divider">

                        <div class="col-md-12 text-primary">
                        	持卡人姓名<input placeholder="持卡人姓名" class="form-control" id="cardname" name="cardname" type="text" maxlength="15"/>
                        </div>
                        
                        <div class="col-md-12 text-primary"><br></div>

                        <div class="col-md-12 text-primary">
			        		持卡人身分證字號<input placeholder="持卡人身分證字號" class="form-control" id="cardid" name="cardid" type="text" style="TEXT-TRANSFORM:uppercase" maxlength="10"/>             
                        </div>
                        
                        <div class="col-md-12 text-primary"><br></div>
                        
                        <div class="col-md-12 text-primary">
                                                                             與被保人關係
			        		<select class="form-control" name="cardrelselect" id="cardrelselect">
			        			<option value="AA">被保人本人</option>
			        			<option value="AB">要保人</option>
			        		</select>                  
                        </div>
					</div>
                    
                    <div class="row custom_divider_noline">

						<div role="tabpanel">
							<!-- Nav tabs -->
							<ul class="nav nav-pills" role="tablist">								
							    <li role="presentation" class="active"><a href="#row0" aria-controls="home" role="tab" data-toggle="tab">第一產物保險股份有限公司線上信用卡刷卡聲明事項</a></li>
							    <li role="presentation"><a href="#row1" aria-controls="profile" role="tab" data-toggle="tab">第一產物保險股份有限公司履行個人資料保護法告知義務</a></li>							    
							</ul>					
						    <!-- Tab panes -->
							<div class="tab-content">
							    <div role="tabpanel" class="tab-pane active" id="row0">
									<iframe id="rtnews1" name=rtnews src="doc/wb/creditcard-declaire.jsp" width=100% height=150 frameborder=1 ></iframe>								
								</div>
							    <div role="tabpanel" class="tab-pane" id="row1">
									<iframe id="rtnews2" name=rtnews src="doc/wb/reg-declaire-v2.jsp" width=100% height=150 frameborder=1 ></iframe>														
								</div>								
								
							</div>
						</div>
							
					</div>
						
					<div class="row custom_divider_noline">
												
		                <p class="text-center"> 
		                	<input type="checkbox" id="ckb1" onclick="ischeck()">
		                	本人已同意並詳細閱讀及了解第一產物保險股份有限公司線上信用卡刷卡聲明事項、個人資料保護法告知義務及聲明事項<br/>	                    
		                </p>
					</div>

			</div>
			
			<div class="panel-footer text-center">
				<button class="btn btn-lg" type="button"><a style="color:white;text-decoration: none" href="<%=request.getContextPath()%>/WB10M0101.do?actionCode=0">回上頁 </a></button>
				<button class="btn btn-lg" type="button" onclick="process(6);" id="next" disabled>下一步 </button>
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
		
		if('N'=='<%=checkid%>'){
			alert('被保/要保人與持卡人ID不一致！請重新確認');
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

			//姓名
			if(cardname.value==''){
				alert('請輸入[持卡人姓名]欄位');
				cardname.focus();
				return false;
			}
			
			//身分證
			if(cardid.value==''){
				alert('請輸入[持卡人身分證字號]欄位');
				cardid.focus();
				return false;
			}
			else{
				//身分證格式
				if (!isAllIdNumber('cardid','<asi:message key="errors.UFC0018"/>'))
	            {
					cardid.focus();               
	            	return false;
	            }
			}		
		}
		
		return true;
	}
	
    function ischeck(){
    	
    	if($('#ckb1').prop("checked")){
    		$('#next').prop("disabled",false);
    		document.getElementById('next').style.backgroundColor="red";
    	}
    	else{
    		$('#next').prop("disabled",true);
    		document.getElementById('next').style.backgroundColor="";
    	}
    }
    
</script>