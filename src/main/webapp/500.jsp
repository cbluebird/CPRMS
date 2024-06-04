<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>500 Internal Server Error</title>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/error-page.css">
</head>
<body>
<div class="container">
    <div class="error-code">500</div>
    <div class="error-message">Internal Server Error</div>
    <p>服务器遇到了一个错误，暂时无法处理您的请求，请稍后再试或点击下方按钮返回主页。</p>
    <a href="${pageContext.request.contextPath}/admin/home.jsp" class="btn">返回主页</a>
</div>
</body>
</html>
