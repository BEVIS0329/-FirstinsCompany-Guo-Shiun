<%-- 網投繳費服務第四頁 --%>
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
	String source = "WB10M010p4";
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
	%>
	<%//欲隱藏回傳的欄位%>
	
	<%//<asi:smartfield type="hidden" property="pjcode"/>%>
	
	<%//加name屬性，表示值是由其它的物件或DBO物件取得%>

	<!-- Page Title -->

    <div class="container">
      <!-- Example row of columns -->
		<div class="row">
		<div class="col-sm-12">
			<div class="panel panel-primary">
			  	<div class="panel-heading"><h3 class="panel-title" style="text-align: center;">線上信用卡繳費-結束頁</h3></div>
                <div class="panel-body padding_divider">
                    
                    <div class="row custom_divider_noline">

						<c:if test="${status.issuccess eq 'Y'}">
							<div class="col-md-12"><h3>刷卡成功</h3></div>
						</c:if>
						
                        <c:if test="${status.issuccess eq 'N'}">
							<div class="col-md-12"><h3>刷卡失敗</h3></div>
							<div class="col-md-3"><h3>錯誤代碼：<c:out value='${status.errorcode}'/></h3></div>
							<div class="col-md-9"><h3><c:out value='${status.errorcontent}'/></h3></div>
						</c:if>
    
                    </div>

			</div>
			
			<div class="panel-footer text-center">
				<button class="btn btn-lg" type="button" style="background-color:red"><a href="http://www.firstins.com.tw" target="_blank" style="color:white;text-decoration: none">回官網首頁</a></button> 
				<c:if test="${status.issuccess eq 'N'}">
				<button class="btn btn-lg" type="button"><a style="color:white;text-decoration: none" href="<%=request.getContextPath()%>/WB10M0101.do?actionCode=0">重新繳費</a> </button>	
				</c:if>			
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

  	<%//處理按鈕動作%>

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
		
		}
		return true;
	}
	
</script>