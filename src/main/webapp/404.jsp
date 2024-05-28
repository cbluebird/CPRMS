<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>404 Not Found</title>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/error-page.css">
</head>
<body>
<div class="container">
    <div class="error-code">404</div>
    <div class="error-message">Not Found</div>
    <p>抱歉，找不到您要查找的页面，请检查网址是否有误或点击下方按钮返回主页。</p>
    <a href="${pageContext.request.contextPath}/homepage.jsp" class="btn">返回主页</a>
</div>
</body>
</html>
