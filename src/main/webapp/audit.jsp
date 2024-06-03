<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<jsp:useBean id="pagination" scope="request"
             type="team.sugarsmile.cprms.dto.PaginationDto<team.sugarsmile.cprms.model.Audit>"/>
<jsp:useBean id="departmentMap" scope="request" type="java.util.HashMap"/>
<fmt:formatNumber var="totalPage" scope="request" type="number"
                  value="${pagination.total == 0 ? 1 : (pagination.total - 1) / pagination.pageSize + 1}"
                  maxFractionDigits="0"/>
<html>
<head>
    <title>审计管理</title>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/appointment.css">
    <%
        String error = (String) request.getAttribute("error");
        if (error != null) {
    %>
    <script>
        window.onload = function () {
            alert("<%= error %>");
        };
    </script>
    <%
        }
    %>
</head>
<body>
<jsp:useBean id="admin" scope="session" type="team.sugarsmile.cprms.model.Admin"/>
<jsp:include page="sidebar.jsp"/>
<div class="content">
    <div class="container">
        <div class="header">
            <h2>审计管理</h2>
        </div>
        <div class="search-section">
            <form action="${pageContext.request.contextPath}/admin/audit/query?pageNum=1&pageSize=10" method="get">
                <div class="row">
                    <label for="applyDate">创建日期：</label>
                    <input type="date" id="applyDate" name="applyDate">

                    <label for="type">操作类型：</label>
                    <select id="type" name="type">
                        <option value="">全部</option>
                        <option value="1">登录</option>
                        <option value="2">添加</option>
                        <option value="3">删除</option>
                        <option value="4">更新</option>
                        <option value="5">查询</option>
                    </select>

                    <label for="operate">操作：</label>
                    <input type="text" id="operate" name="operate">

                    <label for="admin_id">管理员ID：</label>
                    <input type="text" id="admin_id" name="admin_id">
                </div>
                <div class="row">
                    <button type="submit" class="btn btn-primary">查询</button>
                    <label for="count">符合条件的记录总数：</label>
                    <span type="text" id="count" name="count">${pagination.total}</span>
                </div>
            </form>
        </div>
        <div class="result-section">
            <table>
                <thead>
                <tr>
                    <th>创建日期</th>
                    <th>操作类型</th>
                    <th>操作</th>
                    <th>管理员ID</th>
                </tr>
                </thead>
                <tbody>
                <c:forEach var="audit" items="${pagination.list}">
                    <tr>
                        <td><fmt:formatDate value="${audit.createTime}" pattern="yyyy-MM-dd"/></td>
                        <td>
                            <c:choose>
                                <c:when test="${audit.type.value == 1}">登录</c:when>
                                <c:when test="${audit.type.value == 2}">添加</c:when>
                                <c:when test="${audit.type.value == 3}">删除</c:when>
                                <c:when test="${audit.type.value == 4}">更新</c:when>
                                <c:when test="${audit.type.value == 5}">查询</c:when>
                            </c:choose>
                        </td>
                        <td>${audit.operate}</td>
                        <td>${audit.adminId}</td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
            <div class="foot">
                <div class="foot_page">
                    共&nbsp;${pagination.total}&nbsp;条&nbsp;${pagination.pageSize}条/页&nbsp;
                    <c:if test="${pagination.pageNum gt 1}">
                        <a onclick="loadPreviousPage()">上一页</a>
                    </c:if>
                    ${pagination.pageNum} / ${requestScope.totalPage}
                    <c:if test="${pagination.pageNum lt requestScope.totalPage}">
                        <a onclick="loadNextPage()">下一页</a>
                    </c:if>
                </div>
            </div>
        </div>

        <div id="overlay" class="overlay"></div>

    </div>
</div>

<script>
    let currPage = ${pagination.pageNum};
    let totalPage = ${requestScope.totalPage};

    function loadNextPage() {
        if (currPage === totalPage) {
            alert("已经是最后一页了");
            return;
        }
        currPage++;
        window.location.href = "${pageContext.request.contextPath}/admin/appointment/public/query?pageNum=" + currPage + "&pageSize=10";
    }

    function loadPreviousPage() {
        if (currPage === 1) {
            alert("已经是第一页了");
            return;
        }
        currPage--;
        window.location.href = "${pageContext.request.contextPath}/admin/appointment/public/query?pageNum=" + currPage + "&pageSize=10";
    }
</script>

</body>
</html>
