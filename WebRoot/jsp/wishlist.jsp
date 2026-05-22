<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8">
  <title>心愿单</title>
  <link rel="stylesheet" href="${pageContext.request.contextPath}/bootstrap/css/bootstrap.min.css">
  <script type="text/javascript" src="${pageContext.request.contextPath}/jquery/jquery.min.js"></script>
<script>var CTX="${pageContext.request.contextPath}";</script>
  <script type="text/javascript" src="${pageContext.request.contextPath}/bootstrap/js/bootstrap.min.js"></script>
  <script type="text/javascript" src="${pageContext.request.contextPath}/jedate/jquery.jedate.js"></script>
  <link type="text/css" rel="stylesheet" href="${pageContext.request.contextPath}/jedate/skin/jedate.css">
</head>
<body>
<div class="container">
  <h2>心愿单列表</h2>
  <p>
    <a class="btn btn-primary" href="${pageContext.request.contextPath}/jsp/addWish.jsp">添加心愿</a>
    <a class="btn btn-default" href="${pageContext.request.contextPath}/shouzhiRecord/findShouzhiRecord.action">返回收支记录</a>
    <a class="btn btn-default" href="${pageContext.request.contextPath}/memorandum/listMemorandum.action">备忘录</a>
  </p>

  <c:if test="${empty pageBean.pageList}">
    <div class="alert alert-info">暂无愿望单数据</div>
  </c:if>

  <c:if test="${not empty pageBean.pageList}">
    <table class="table table-bordered table-hover">
      <thead>
      <tr>
        <th>编号</th>
        <th>心愿</th>
        <th>预计金额</th>
        <th>日期</th>
        <th>状态</th>
        <th>操作</th>
      </tr>
      </thead>
      <tbody>
      <c:forEach items="${pageBean.pageList}" var="oneWish">
        <tr>
          <td>${oneWish.wid}</td>
          <td>${oneWish.wish}</td>
          <td>${oneWish.wnum}</td>
          <td>${oneWish.wdate}</td>
          <td>${oneWish.state}</td>
          <td>
            <a class="btn btn-info btn-xs" href="javascript:void(0)" onclick="openEditModal(${oneWish.id})">编辑</a>
            <a class="btn btn-danger btn-xs" href="javascript:void(0)" onclick="deleteWish(${oneWish.id})">删除</a>
          </td>
        </tr>
      </c:forEach>
      </tbody>
    </table>
  </c:if>

  <c:if test="${not empty pageBean}">
    <ul class="pagination">
      <li><a href="${pageContext.request.contextPath}/wishlist/findAllWishList.action?currentPage=0">首页</a></li>
      <li>
        <a href="${pageContext.request.contextPath}/wishlist/findAllWishList.action?currentPage=${pageBean.currentPage > 0 ? pageBean.currentPage - 1 : 0}">上一页</a>
      </li>
      <li>
        <a href="${pageContext.request.contextPath}/wishlist/findAllWishList.action?currentPage=${pageBean.currentPage < pageBean.allPage - 1 ? pageBean.currentPage + 1 : pageBean.allPage - 1}">下一页</a>
      </li>
      <li><a href="${pageContext.request.contextPath}/wishlist/findAllWishList.action?currentPage=${pageBean.allPage - 1}">尾页</a></li>
    </ul>
  </c:if>
</div>

<!-- 编辑模态框 -->
<div class="modal fade" id="editModal" tabindex="-1" role="dialog">
  <div class="modal-dialog" role="document">
    <div class="modal-content">
      <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal">&times;</button>
        <h4 class="modal-title">编辑心愿</h4>
      </div>
      <div class="modal-body">
        <form id="editForm">
          <input type="hidden" name="id" id="edit_id">
          <input type="hidden" name="user_id" id="edit_user_id">
          <div class="form-group">
            <label>心愿内容</label>
            <input type="text" name="wish" id="edit_wish" class="form-control" required>
          </div>
          <div class="form-group">
            <label>预计金额</label>
            <input type="number" name="wnum" id="edit_wnum" class="form-control" required>
          </div>
          <div class="form-group">
            <label>心愿日期</label>
            <div class="input-group">
              <input type="datetime-local" name="wdate" id="edit_wdate" class="form-control" required>
              <span class="input-group-btn">
                <button type="button" class="btn btn-default" onclick="setNow()">当前时间</button>
              </span>
            </div>
          </div>
          <div class="form-group">
            <label>状态</label>
            <select name="state" id="edit_state" class="form-control">
              <option value="未完成">未完成</option>
              <option value="已完成">已完成</option>
            </select>
          </div>
        </form>
      </div>
      <div class="modal-footer">
        <button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
        <button type="button" class="btn btn-primary" onclick="submitEdit()">保存修改</button>
      </div>
    </div>
  </div>
</div>

<script>
function openEditModal(id) {
  $.ajax({
    url: "${pageContext.request.contextPath}/wishlist/toEdit.action",
    type: "POST",
    data: { id: id },
    dataType: "json",
    success: function(data) {
      $("#edit_id").val(data.id);
      $("#edit_user_id").val(data.user_id);
      $("#edit_wish").val(data.wish);
      $("#edit_wnum").val(data.wnum);
      // 转换为 datetime-local 格式 (yyyy-MM-ddTHH:mm)
      var wdate = data.wdate;
      if (wdate && wdate.length >= 16) {
        wdate = wdate.replace(" ", "T");
        if (wdate.length === 16) wdate += ":00";
      }
      $("#edit_wdate").val(wdate);
      $("#edit_state").val(data.state);
      $("#editModal").modal("show");
    },
    error: function() {
      alert("获取心愿信息失败");
    }
  });
}

function submitEdit() {
  $.ajax({
    url: "${pageContext.request.contextPath}/wishlist/editWish.action",
    type: "POST",
    data: $("#editForm").serialize(),
    success: function() {
      $("#editModal").modal("hide");
      window.location.reload();
    },
    error: function() {
      alert("修改失败");
    }
  });
}

function setNow() {
  var now = new Date();
  var yyyy = now.getFullYear();
  var mm = String(now.getMonth() + 1).padStart(2, '0');
  var dd = String(now.getDate()).padStart(2, '0');
  var hh = String(now.getHours()).padStart(2, '0');
  var min = String(now.getMinutes()).padStart(2, '0');
  $("#edit_wdate").val(yyyy + '-' + mm + '-' + dd + 'T' + hh + ':' + min);
}

function deleteWish(id) {
  if (confirm("确认要删除该心愿吗？")) {
    $.ajax({
      url: "${pageContext.request.contextPath}/wishlist/deleteWish.action",
      type: "POST",
      data: { id: id },
      success: function() {
        window.location.reload();
      },
      error: function() {
        alert("删除失败");
      }
    });
  }
}
</script>
</body>
</html>
