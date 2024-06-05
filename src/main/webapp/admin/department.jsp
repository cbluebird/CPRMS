<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<jsp:include page="sidebar.jsp"/>
<jsp:useBean id="pagination" scope="request"
             type="team.sugarsmile.cprms.dto.PaginationDto<team.sugarsmile.cprms.model.Department>"/>
<fmt:formatNumber var="totalPage" scope="request" type="number"
                  value="${pagination.total == 0 ? 1 : (pagination.total - 1) / pagination.pageSize + 0.51}"
                  maxFractionDigits="0"/>
<html>
<head>
    <title>部门管理页面</title>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/admin/table.css">
    <%
        String error = (String) session.getAttribute("error");
        if (error != null) {
    %>
    <script>
        window.onload = function () {
            alert("<%= error %>");
        };
    </script>
    <%
            session.removeAttribute("error");
        }
    %>
</head>
<body>
<div class="container">
    <div class="table">
        <div class="header-container">
            <h2>部门管理</h2>
        </div>
        <div class="table-container">
            <table>
                <thead>
                <tr>
                    <th>部门编号</th>
                    <th>部门类型</th>
                    <th>部门名称</th>
                    <th>社会公众预约管理权限</th>
                    <th>公务预约管理权限</th>
                    <th>操作</th>
                </tr>
                </thead>
                <tbody>
                <c:forEach var="department" items="${pagination.list}">
                    <tr>
                        <td>${department.id}</td>
                        <td>
                            <c:choose>
                                <c:when test="${department.type.value eq 1}">
                                    行政部门
                                </c:when>
                                <c:when test="${department.type.value eq 2}">
                                    直属部门
                                </c:when>
                                <c:when test="${department.type.value eq 3}">
                                    学院
                                </c:when>
                            </c:choose>
                        </td>
                        <td>${department.name}</td>
                        <td>
                            <c:choose>
                                <c:when test="${department.social}">
                                    有
                                </c:when>
                                <c:otherwise>
                                    无
                                </c:otherwise>
                            </c:choose>
                        </td>
                        <td>
                            <c:choose>
                                <c:when test="${department.business}">
                                    所有部门
                                </c:when>
                                <c:otherwise>
                                    本部门
                                </c:otherwise>
                            </c:choose>
                        </td>
                        <td>
                            <button class="modify"
                                    onclick="updateDepartment('${department.id}','${department.type.value}','${department.name}','${department.social}','${department.business}')">
                                修改
                            </button>
                            <button class="delete" onclick="deleteDepartment('${department.id}')">删除</button>
                        </td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
        </div>
        <div class="foot-container">
            <div class="foot-page">
                共&nbsp;${pagination.total}&nbsp;条&nbsp;${pagination.pageSize}条/页&nbsp;
                <c:if test="${pagination.pageNum gt 1}">
                    <a onclick="loadPreviousPage()">上一页</a>
                </c:if>
                ${pagination.pageNum} / ${requestScope.totalPage}
                <c:if test="${pagination.pageNum lt requestScope.totalPage}">
                    <a onclick="loadNextPage()">下一页</a>
                </c:if>
            </div>
            <div class="foot-add">
                <button onclick="showAddPopup()">添加</button>
            </div>
        </div>
    </div>

    <div id="overlay" class="overlay"></div>

    <div id="popup-add" class="popup">
        <h2>添加部门</h2>
        <form action="${pageContext.request.contextPath}/admin/department/add" method="post">
            <div>
                <label for="addType">部门类型:</label>
                <select id="addType" name="type">
                    <option value="1">行政部门</option>
                    <option value="2">直属部门</option>
                    <option value="3">学院</option>
                </select>
            </div>
            <div>
                <label for="addName">部门名称:</label>
                <input type="text" id="addName" name="name" required/>
            </div>
            <div>
                <label for="addSocial">社会公众预约管理权限:</label>
                <input type="checkbox" id="addSocial" name="social" value="true"/>
            </div>
            <div>
                <label for="addBusiness">所有部门公务预约管理权限:</label>
                <input type="checkbox" id="addBusiness" name="business" value="true"/>
            </div>
            <div class="button-group">
                <button type="submit">提交</button>
                <button class="cancel" type="button" onclick="closeAddPopup()">取消</button>
            </div>
        </form>
    </div>

    <div id="popup-update" class="popup">
        <h2>修改部门</h2>
        <form action="${pageContext.request.contextPath}/admin/department/update" method="post">
            <input type="hidden" id="updateId" name="id"/>
            <div>
                <label for="updateType">部门类型:</label>
                <select id="updateType" name="type">
                    <option value="1">行政部门</option>
                    <option value="2">直属部门</option>
                    <option value="3">学院</option>
                </select>
            </div>
            <div>
                <label for="updateName">部门名称:</label>
                <input type="text" id="updateName" name="name" required/>
            </div>
            <div>
                <label for="updateSocial">社会公众预约管理权限:</label>
                <input type="checkbox" id="updateSocial" name="social" value="true"/>
            </div>
            <div>
                <label for="updateBusiness">所有部门公务预约管理权限:</label>
                <input type="checkbox" id="updateBusiness" name="business" value="true"/>
            </div>
            <div class="button-group">
                <button type="submit">保存</button>
                <button class="cancel" type="button" onclick="closeUpdatePopup()">取消</button>
            </div>
        </form>
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
        window.location.href = "${pageContext.request.contextPath}/admin/department/list?pageNum=" + currPage + "&pageSize=10";
    }

    function loadPreviousPage() {
        if (currPage === 1) {
            alert("已经是第一页了");
            return;
        }
        currPage--;
        window.location.href = "${pageContext.request.contextPath}/admin/department/list?pageNum=" + currPage + "&pageSize=10";
    }

    function updateDepartment(id, type, name, social, business) {
        document.getElementById("updateId").value = id;
        document.getElementById("updateType").value = type;
        document.getElementById("updateName").value = name;
        document.getElementById("updateSocial").value = social;
        document.getElementById("updateBusiness").value = business;

        showUpdatePopup();
    }

    function deleteDepartment(id) {
        window.location.href = "${pageContext.request.contextPath}/admin/department/delete?id=" + id;
    }

    function showUpdatePopup() {
        const overlay = document.getElementById("overlay");
        const popup = document.getElementById("popup-update");
        overlay.style.display = "block";
        popup.style.display = "block";
    }

    function closeUpdatePopup() {
        const overlay = document.getElementById("overlay");
        const popup = document.getElementById("popup-update");
        overlay.style.display = "none";
        popup.style.display = "none";
    }

    function showAddPopup() {
        const overlay = document.getElementById('overlay');
        const popup = document.getElementById('popup-add');
        overlay.style.display = 'block';
        popup.style.display = 'block';
    }

    function closeAddPopup() {
        const overlay = document.getElementById('overlay');
        const popup = document.getElementById('popup-add');
        overlay.style.display = 'none';
        popup.style.display = 'none';
    }
</script>
</body>
</html>
