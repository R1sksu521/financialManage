<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<%
	String path = request.CTX;
	String host = request.getHeader("X-Forwarded-Host");
			if (host == null) host = request.getHeader("Host");
			if (host == null) host = request.getServerName() + ":" + request.getServerPort();
			String basePath = request.getScheme() + "://" + host + path + "/";
%>

<!DOCTYPE HTML>
<html>
<head>
	<!-- <base href="<%=basePath%>"> -->
	<title>我的备忘</title>
	<meta name="viewport" content="width=device-width,initial-scale=1,user-scalable=no">
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">

	<link rel="stylesheet" href="${pageContext.request.contextPath}/bootstrap/css/bootstrap.min.css">
	<script type="text/javascript" src="${pageContext.request.contextPath}/jquery/jquery.min.js"></script>
<script>var CTX="${pageContext.request.contextPath}";</script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/bootstrap/js/bootstrap.min.js"></script>

	<link rel="stylesheet" href="${pageContext.request.contextPath}/css/memorandum.css">
	<script type="text/javascript" src="${pageContext.request.contextPath}/js/memorandum.js"></script>

<script>window.UEDITOR_HOME_URL="${pageContext.request.contextPath}/ueditor/"";</script>
	<script type="text/javascript" charset="utf-8" src="${pageContext.request.contextPath}/ueditor/ueditor.config.js"></script>
	<script type="text/javascript" charset="utf-8" src="${pageContext.request.contextPath}/ueditor/ueditor.all.min.js"> </script>
	<script type="text/javascript" charset="utf-8" src="${pageContext.request.contextPath}/ueditor/lang/zh-cn/zh-cn.js"></script>

	<script type="text/javascript">
		// 禁止后退键逻辑
		function forbidBackSpace(e) {
			var ev = e || window.event;
			var obj = ev.target || ev.srcElement;
			var t = obj.type || obj.getAttribute('type');
			var vReadOnly = (obj.readOnly == undefined) ? false : obj.readOnly;
			var vDisabled = (obj.disabled == undefined) ? true : obj.disabled;
			var flag1 = ev.keyCode == 8 && (t == "password" || t == "text" || t == "textarea") && (vReadOnly == true || vDisabled == true);
			var flag2 = ev.keyCode == 8 && t != "password" && t != "text" && t != "textarea";
			if (flag2 || flag1) return false;
		}
		document.onkeydown = forbidBackSpace;

		function getPlainTxt() {
			$("#editvalue").val(UE.getEditor('editor').getPlainTxt());
			var edit = $("#editvalue").val();
			var replaceEdit = edit.replace(/\s+/g,"");
			if(replaceEdit.length == 0){
				$("#myMsg").text("请输入备忘信息");
				return false;
			}
			return true;
		}
	</script>
</head>

<body>
<nav class="navbar navbar-default" role="navigation" >
	<div class="container">
		<div class="navbar-header">
			<button type="button" class="navbar-toggle" data-toggle="collapse" data-target="#home-navbar-collapse">
				<span class="icon-bar"></span> <span class="icon-bar"></span> <span class="icon-bar"></span>
			</button>
			<a class="navbar-brand"> 财务管理系统 </a>
		</div>
		<div class="collapse navbar-collapse" id="home-navbar-collapse">
			<ul class="nav navbar-nav">
				<li><a href="${pageContext.request.contextPath}/shouzhiRecord/findShouzhiRecord.action">财务管理</a></li>
				<li><a href="${pageContext.request.contextPath}/toFinancialCount.action">财务统计</a></li>
				<li><a href="${pageContext.request.contextPath}/financialAnalysis/toFinancialAnalysis.action">财务分析</a></li>
				<li><a href="${pageContext.request.contextPath}/budget/findBudget.action">财务预算</a></li>
				<li><a href="${pageContext.request.contextPath}/wishlist/findAllWishList.action">心愿单</a></li>
				<li class="active"><a href="${pageContext.request.contextPath}/memorandum/listMemorandum.action">备忘录</a></li>
			</ul>
			<ul class="nav navbar-nav navbar-right">
				<li><a href="javascript:void(0)">欢迎：${sessionScope.user.username}</a></li>
			</ul>
		</div>
	</div>
</nav>

<div class="container" id="mainContent" >
	<div class="row">
		<div class="col-md-1"></div>
		<div class="col-md-10">
			<form name="upfile" action="${pageContext.request.contextPath}/memorandum/editMemorandum.action" method="post">
				<div class="row" style="margin-bottom: 15px; display: flex; align-items: center;">
					<div class="col-md-2 col-xs-2">
						<a href="${pageContext.request.contextPath}/memorandum/listMemorandum.action" style="text-decoration: none;">
							<span class="glyphicon glyphicon-arrow-left addZiti">返回</span>
						</a>
					</div>
					<div class="col-md-8 col-xs-8" style="text-align:center;">
						<span style="color:green; font-weight: bold; font-size: 24px;" class="addZiti">编辑备忘</span>
					</div>
					<div class="col-md-2 col-xs-2"></div>
				</div>

				<script id="editor" type="text/plain" style="width:100%;height:500px;">${content}</script>

				<input type="hidden" id="editvalue" name="editvalue">
				<input type="hidden" name="content" value="${memorandum.content }">
				<input type="hidden" name="mid" value="${memorandum.mid }">
				<input type="hidden" name="currentPage" value="${currentPage}">

				<div style="margin-top: 15px;">
					<button type="submit" class="btn btn-primary" onclick="return getPlainTxt()">保存备忘</button>
					<label id="myMsg" style="color:red; margin-left: 20px;"></label>
				</div>
			</form>

			<script type="text/javascript">
				// 实例化编辑器（保持默认设置，显示字数统计）
				var ue = UE.getEditor('editor');
			</script>
		</div>
	</div>
</div>
</body>
</html>