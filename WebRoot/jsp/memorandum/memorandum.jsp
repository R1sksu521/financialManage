<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8">
  <title>备忘录</title>
  <link rel="stylesheet" href="${pageContext.request.contextPath}/bootstrap/css/bootstrap.min.css">
  <script type="text/javascript" src="${pageContext.request.contextPath}/jquery/jquery.min.js"></script>
<script>var CTX="${pageContext.request.contextPath}";</script>
  <script type="text/javascript" src="${pageContext.request.contextPath}/bootstrap/js/bootstrap.min.js"></script>
</head>
<body>
<div class="container">
  <h2>备忘录列表</h2>
  <p>
    <a class="btn btn-default" href="${pageContext.request.contextPath}/shouzhiRecord/findShouzhiRecord.action">返回收支记录</a>
    <a class="btn btn-default" href="${pageContext.request.contextPath}/wishlist/findAllWishList.action">心愿单</a>
    <a class="btn btn-primary" href="${pageContext.request.contextPath}/jsp/memorandum/addmemorandum.jsp">添加备忘录</a>
  </p>

  <c:if test="${empty pageBean.pageList}">
    <div class="alert alert-info">暂无备忘录数据</div>
  </c:if>

  <c:if test="${not empty pageBean.pageList}">
    <table class="table table-bordered table-hover">
      <thead>
      <tr>
        <th>标题</th>
        <th>记录时间</th>
        <th>操作</th>
      </tr>
      </thead>
      <tbody>
      <c:forEach items="${pageBean.pageList}" var="memorandum">
        <tr>
          <td>${memorandum.topFont}</td>
          <td>${memorandum.recordTime}</td>
          <td>
            <a class="btn btn-xs btn-info" href="${pageContext.request.contextPath}/memorandum/oneMemorandum.action?mid=${memorandum.mid}&currentPage=${pageBean.currentPage}">查看</a>
            <a class="btn btn-xs btn-danger" href="${pageContext.request.contextPath}/memorandum/deleteMemorandum.action?mid=${memorandum.mid}&currentPage=${pageBean.currentPage}">删除</a>
          </td>
        </tr>
      </c:forEach>
      </tbody>
    </table>
  </c:if>

  <c:if test="${not empty pageBean}">
    <ul class="pagination">
      <li><a href="${pageContext.request.contextPath}/memorandum/listMemorandum.action?currentPage=0">首页</a></li>
      <li><a href="${pageContext.request.contextPath}/memorandum/listMemorandum.action?currentPage=${pageBean.currentPage-1}">上一页</a></li>
      <li><a href="${pageContext.request.contextPath}/memorandum/listMemorandum.action?currentPage=${pageBean.currentPage+1}">下一页</a></li>
      <li><a href="${pageContext.request.contextPath}/memorandum/listMemorandum.action?currentPage=${pageBean.allPage-1}">尾页</a></li>
    </ul>
  </c:if>
</div>
</body>
</html>