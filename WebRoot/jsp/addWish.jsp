<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8">
  <title>添加心愿</title>
  <link rel="stylesheet" href="${pageContext.request.contextPath}/bootstrap/css/bootstrap.min.css">
  <script src="${pageContext.request.contextPath}/jquery/jquery.min.js"></script>
</head>
<body>
<div class="container">
  <div class="row">
    <div class="col-md-6 col-md-offset-3">
      <h2>添加新心愿</h2>
      <hr>
      <form id="addWishForm">
        <div class="form-group">
          <label>心愿内容</label>
          <input type="text" name="wish" class="form-control" required placeholder="例如：买相机">
        </div>
        <div class="form-group">
          <label>预计金额</label>
          <input type="number" name="wnum" class="form-control" required placeholder="请输入数字">
        </div>
        <div class="form-group">
          <label>心愿日期</label>
          <div class="input-group">
            <input type="datetime-local" name="wdate" id="wdate" class="form-control" required>
            <span class="input-group-btn">
              <button type="button" class="btn btn-default" onclick="setNow()">当前时间</button>
            </span>
          </div>
        </div>

        <div class="form-group">
          <button type="button" onclick="submitWish()" class="btn btn-success">提交保存</button>
          <a href="${pageContext.request.contextPath}/wishlist/findAllWishList.action" class="btn btn-default">返回列表</a>
        </div>
      </form>
    </div>
  </div>
</div>

<script>
  function setNow() {
    var now = new Date();
    var yyyy = now.getFullYear();
    var mm = String(now.getMonth() + 1).padStart(2, '0');
    var dd = String(now.getDate()).padStart(2, '0');
    var hh = String(now.getHours()).padStart(2, '0');
    var min = String(now.getMinutes()).padStart(2, '0');
    document.getElementById('wdate').value = yyyy + '-' + mm + '-' + dd + 'T' + hh + ':' + min;
  }

  // 默认显示当前日期时间
  setNow();

  function submitWish() {
    $.ajax({
      url: "${pageContext.request.contextPath}/wishlist/addWish.action",
      type: "POST",
      data: $('#addWishForm').serialize(),
      success: function(data) {
        // 使用 trim() 防止后端返回字符串带空格
        if(data.trim() == "OK") {
          alert("添加成功！");
          window.location.href = "${pageContext.request.contextPath}/wishlist/findAllWishList.action";
        } else {
          alert("添加失败，后端反馈：" + data);
        }
      },
      error: function() {
        alert("请求服务器失败，请检查登录状态或后台日志");
      }
    });
  }
</script>
</body>
</html>