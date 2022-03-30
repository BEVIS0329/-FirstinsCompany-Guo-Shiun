<%-- 網投繳費服務第一頁 --%>
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
	String source = "WB10M010p1";
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
	
	<input type="hidden" id="instype" name="instype" value="">
	<input type="hidden" id="carnumber" name="carnumber" value="">
	<%//<asi:smartfield type="hidden" property="pjcode"/>%>
	
	<%//加name屬性，表示值是由其它的物件或DBO物件取得%>

	<!-- Page Title -->

    <div class="container">
      <!-- Example row of columns -->
		<div class="row">
		<div class="col-sm-12" >
			<div class="panel panel-primary">
			  <div class="panel-heading">
			  	<h3 class="panel-title" style="text-align: center;">未繳費保單</h3>
			  </div>
				<div id="ins_content">
				
				<table class="table break-table" border="1" width="100%">
				
				<thead>
					<tr class="bg-info h4">
						<th class="text-center">勾選</th>
						<th class="text-center">交易序號</th>
						<th class="text-center">保單/繳費號碼</th>
						<th class="text-center">險種</th>
						<th class="text-center">車牌</th>
						<th class="text-center">被保人</th>
						<th class="text-center">起保日</th>
						<th class="text-center">保費</th>
						<th class="text-center">繳費期限</th>
					</tr>
				</thead>
					
					<c:if test="${fn:length(datalist) != 0}">
					<c:forEach items="${datalist}" var="data" varStatus="varsts">
						<c:if test="${data.BC306*1 ge data.sysdate*1}">	   <!-- 繳費期限大於等於今天日期才顯示  -->
						<tr class="bg-info">
						    <!-- 勾選的值需要包含交易序號、險種、車牌  -->
							<td data-title="勾選" style="height: 40px">
								<c:choose>
									<c:when test="${varsts.index eq 0}">
										<input type="checkbox" id="ckb<c:out value="${varsts.index}"/>" name="ckb" value="<c:out value='${data.BC301}'/>&<c:out value='${data.BC302}'/>&<c:out value='${data.carnumber}'/>" onclick="calculate(this.id)" >
									</c:when>
									<c:otherwise>
										<c:if test="${data.BC301 ne datalist[varsts.index-1].BC301}">
											<input type="checkbox" id="ckb<c:out value="${varsts.index}"/>" name="ckb" value="<c:out value='${data.BC301}'/>&<c:out value='${data.BC302}'/>&<c:out value='${data.carnumber}'/>" onclick="calculate(this.id)" >
										</c:if>
									</c:otherwise>
								</c:choose>																												
							</td>
							<!-- 交易序號 -->
							<td data-title="交易序號">
								<c:out value='${data.BC301}'/>															
							</td>
							<!-- 保單/繳費號碼 -->
							<td data-title="保單/繳費號碼" style="height: 50px">
								<c:out value='${data.BC303}'/>
							</td>
							<!-- 險種 -->
							<td data-title="險種">
								<c:choose>
									<c:when test="${data.BC302 eq 'C'}">
										車險
									</c:when>
									<c:when test="${data.BC302 eq 'F'}">
										住火險
									</c:when>
									<c:when test="${data.BC302 eq 'E'}">
										工程險
									</c:when>
									<c:when test="${data.BC302 eq 'H'}">
										船體險
									</c:when>
									<c:when test="${data.BC302 eq 'M'}">
										水險
									</c:when>
									<c:when test="${data.BC302 eq 'O'}">
										新種險
									</c:when>
									<c:otherwise>
										<c:out value='${data.BC302}'/>
									</c:otherwise>
								</c:choose>								
							</td>
							<!-- 車牌  -->
							<td data-title="車牌" style="height: 40px">
								<c:out value='${data.carnumber}'/>							
							</td>
							<!-- 被保人 -->
							<td data-title="被保人">
								<c:out value='${data.insured}'/>						
							</td>
							<!-- 起保日 -->
							<td data-title="起保日">
								<c:out value='${data.startdate}'/>								
							</td>
							<!-- 保費 -->
							<td data-title="保費" name="<c:out value='${data.BC301}'/>">
								<c:out value='${data.BC305}'/>
							</td>
							<!-- 繳費期限  -->
							<td data-title="繳費期限">					
								<c:out value='${data.BC306}'/>	
							</td>
						</tr>
						</c:if>
					</c:forEach>
					</c:if>
					
				</table>	
                                   
				</div>
				
				<div class="row custom_divider_noline">

                    <div class="col-md-6"></div>

                    <div class="col-md-3">
                       	交易序號：<input style="border:none;" type="text" id="tradecode" name="tradecode" value="" readonly="readonly"><br>
                    </div>
                    <div class="col-md-3">
                                                                 應收總保費：<input style="border:none;" type="text" id="instotal" name="instotal" value="" readonly="readonly"><br>
                    </div>
    
                </div>
				
				<br/>
				
				<div class="panel-footer text-center">
					<button class="btn btn-lg" type="button" ><a href="http://www.firstins.com.tw" target="_blank" style="color:white;text-decoration: none">回官網首頁</a></button> 
					<button class="btn btn-lg" type="button" onclick="process(5);" id="next" disabled>信用卡繳費 </button>
			    </div>
			    
			</div>			
		</div>
		</div>
	</div>
	
	<div class="container">
		<div class="row">
			<div class="col-sm-12" style="background-color: #D0D0D0">
				<ul style="padding:10px ;">
						<ol>
							<li>本公司網站符合最新SSL安全交易機制，並通過網際威信(Hi-trust)之安全認證，您可以放心在本網站進行交易與使用所有線上功能。</li>
							<li>因繳費系統須核對資料並請款，未能及時更新繳費狀態，若您已繳費，請勿重複繳費。</li>
							<li>若對繳費有任何疑問，請洽您的保險業務員，或撥打客服，我們將協助您！客服專線：0800-288-068</li>
						</ol>
				</ul>
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
    
    //id是這個勾選的checkbox的id
    function calculate(id){

    	if($('#'+id).prop("checked")){
    		//name叫做agreeChk1的全部都disabled
    		$("input[name='ckb']").each(function(){
    			$(this).prop("disabled",true);
    		})
    		//選取到的這個checkbox打開
    		$('#'+id).prop("disabled",false);
    		//讓信用卡繳費disable取消
    		$('#next').prop("disabled",false);
    		document.getElementById('next').style.backgroundColor="red";
    		//勾選的VALUE包含交易序號、險種、以及車牌，因此需先拆解
    		var detail=$('#'+id).val().split('&'); 		
    		//交易序號，同時也是各項保費的NAME
    		var tradecode=detail[0];
    		//險種
    		var type=detail[1];
    		$('#instype').val(type);
    		//車牌
    		var number=detail[2];
    		$('#carnumber').val(number);
    		//計算總保費
    		var total=0;			
    		//取得保費欄位是該交易序號的總數
    		var len=document.getElementsByName(tradecode).length;

    		for(var i=0;i<len;i++){
    			total+=parseInt(document.getElementsByName(tradecode)[i].innerHTML);
    		}

    		//把總保費total指定給總保費欄位
    		$('#instotal').val(total);
    		//交易序號指定給交易序號欄位
    		$('#tradecode').val(tradecode);
    		
    	}
    	else{
    	    //name叫做agreeChk1的全部都取消disabled
    		$("input[name='ckb']").each(function(){
    			$(this).prop("disabled",false);
    		})
    		//讓信用卡繳費disable
    		$('#next').prop("disabled",true);
    		document.getElementById('next').style.backgroundColor="";
    		//把總保費total清空
    		$('#instotal').val("");
    		//交易序號清空
    		$('#tradecode').val("");
    		//險種和車牌清空
    		$('#instype').val("");
    		$('#carnumber').val("");
    	}
    	
    }
	
</script>