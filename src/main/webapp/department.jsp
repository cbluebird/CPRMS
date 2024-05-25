<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>部门管理页面</title>
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
        }
    %>
</head>
<body>
<div id="departmentTable" class="table">
    <div class="head_add">
        <button onclick="showAddPopup()">添加</button>
    </div>
    <div>
        <table border="1">
            <thead>
            <tr>
                <th>部门编号</th>
                <th>部门类型</th>
                <th>部门名称</th>
                <th>操作</th>
            </tr>
            </thead>
            <tbody>
            <c:forEach var="department" items="${requestScope.list}">
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
                        <button onclick="updateDepartment('${department.id}','${department.type.value}','${department.name}')">
                            修改
                        </button>
                        <button onclick="deleteDepartment('${department.id}')">删除</button>
                    </td>
                </tr>
            </c:forEach>
            </tbody>
        </table>
    </div>
    <div class="foot_page">
        <c:if test="${requestScope.totalPage ne 0}">
            共&nbsp;${requestScope.total}&nbsp;条&nbsp;${requestScope.pageSize}条/页&nbsp;
            <c:if test="${requestScope.pageNum gt 1}">
                <a onclick="loadPreviousPage()">上一页</a>
            </c:if>
            ${requestScope.pageNum} / ${requestScope.totalPage}
            <c:if test="${requestScope.pageNum lt requestScope.totalPage}">
                <a onclick="loadNextPage()">下一页</a>
            </c:if>
        </c:if>
    </div>
</div>

<div id="overlay" class="overlay">
</div>

<div id="popup_add" class="popup">
    <h2>添加部门</h2>
    <form action="${pageContext.request.contextPath}/admin/department/add" method="post">
        <div>
            <label for="addType">部门类型:</label>
            <select id="addType" name="type">
                <option value="1">行政部门</option>
                <option value="2">直属部门</option>
                <option value="1">学院</option>
            </select>
        </div>
        <div>
            <label for="addName">部门名称:</label>
            <input type="text" id="addName" name="name"/>
        </div>
        <div>
            <button type="submit">提交</button>
            <button type="button" onclick="closeAddPopup()">取消</button>
        </div>
    </form>
</div>

<div id="popup_update" class="popup">
    <h2>修改部门</h2>
    <form action="${pageContext.request.contextPath}/admin/department/update" method="post">
        <input type="hidden" id="updateId" name="id"/>
        <label for="updateType">部门类型:</label>
        <select id="updateType" name="type">
            <option value="1">行政部门</option>
            <option value="2">直属部门</option>
            <option value="3">学院</option>
        </select>
        <div>
            <label for="updateName">学院名称:</label>
            <input type="text" id="updateName" name="name"/>
        </div>
        <div>
            <button type="submit">保存</button>
            <button type="button" onclick="closeUpdatePopup()">取消</button>
        </div>
    </form>
</div>
</body>
</html>
<script>
    let currentPage = ${requestScope.pageNum};

    function loadNextPage() {
        if (currentPage === ${requestScope.totalPage}) {
            alert("已经是最后一页了");
            return;
        }
        currentPage++;
        window.location.href = "${pageContext.request.contextPath}/admin/department/list?pageNum=" + currentPage + "&pageSize=10";
    }

    function loadPreviousPage() {
        if (currentPage === 1) {
            alert("已经是第一页了");
            return;
        }
        currentPage--;
        window.location.href = "${pageContext.request.contextPath}/admin/department/list?pageNum=" + currentPage + "&pageSize=10";
    }

    function updateDepartment(id, type, name) {
        document.getElementById("updateId").value = id;
        document.getElementById("updateType").value = type;
        document.getElementById("updateName").value = name;

        showUpdatePopup()
    }

    function deleteDepartment(id) {
        window.location.href = "${pageContext.request.contextPath}/admin/department/delete?id=" + id
    }

    function showUpdatePopup() {
        const overlay = document.getElementById("overlay");
        const popup = document.getElementById("popup_update");
        overlay.style.display = "block";
        popup.style.display = "block";
    }

    function closeUpdatePopup() {
        const overlay = document.getElementById("overlay");
        const popup = document.getElementById("popup_update");
        overlay.style.display = "none";
        popup.style.display = "none";
    }

    function showAddPopup() {
        const overlay = document.getElementById('overlay');
        const popup = document.getElementById('popup_add');
        overlay.style.display = 'block';
        popup.style.display = 'block';
    }

    function closeAddPopup() {
        const overlay = document.getElementById('overlay');
        const popup = document.getElementById('popup_add');
        overlay.style.display = 'none';
        popup.style.display = 'none';
    }
</script>
<style>
    table {
        text-align: center;
    }

    .table {
        position: fixed;
        top: 50%;
        left: 50%;
        transform: translate(-50%, -50%);
    }

    .head_add {
        margin-bottom: 5px;
    }

    .foot_page {
        text-align: right;
    }

    .overlay {
        position: fixed;
        top: 0;
        left: 0;
        width: 100%;
        height: 100%;
        background-color: rgba(0, 0, 0, 0.5);
        display: none;
        z-index: 9999;
    }

    .popup {
        width: 300px;
        position: fixed;
        top: 50%;
        left: 50%;
        transform: translate(-50%, -50%);
        background-color: #fff;
        padding: 20px;
        border-radius: 20px;
        z-index: 10000;
        text-align: center;
        display: none;
    }

    .popup input[type=text], .popup select {
        width: 200px;
    }

    .foot_page a {
        cursor: pointer;
    }
</style>