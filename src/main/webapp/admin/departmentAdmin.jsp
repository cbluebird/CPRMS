<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<jsp:include page="sidebar.jsp"/>
<jsp:useBean id="pagination" scope="request"
             type="team.sugarsmile.cprms.dto.PaginationDto<team.sugarsmile.cprms.model.Admin>"/>
<jsp:useBean id="departmentMap" scope="request" type="java.util.HashMap"/>
<fmt:formatNumber var="totalPage" scope="request" type="number"
                  value="${pagination.total == 0 ? 1 : (pagination.total - 1) / pagination.pageSize + 0.51}"
                  maxFractionDigits="0"/>
<jsp:useBean id="admin" scope="session" type="team.sugarsmile.cprms.model.Admin"/>
<html>
<head>
    <title>部门管理员管理</title>
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
            <h2>部门管理员管理</h2>
        </div>
        <div class="table-container">
            <table>
                <thead>
                <tr>
                    <th>编号</th>
                    <th>姓名</th>
                    <th>登录名</th>
                    <th>电话</th>
                    <th>部门</th>
                    <th>操作</th>
                </tr>
                </thead>
                <tbody>
                <c:forEach var="admin" items="${pagination.list}">
                    <tr>
                        <td>${admin.id}</td>
                        <td>${admin.name}</td>
                        <td>${admin.userName}</td>
                        <td>${admin.phone}</td>
                        <td>${departmentMap[admin.departmentID].name}</td>
                        <td>
                            <button class="modify"
                                    onclick="updateDepartmentAdmin('${admin.id}','${admin.name}','${admin.userName}','${admin.phone}','${admin.departmentID}')">
                                修改
                            </button>
                            <button class="delete" onclick="deleteDepartmentAdmin('${admin.id}')">删除</button>
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
        <h2>添加部门管理员</h2>
        <form action="${pageContext.request.contextPath}/admin/departmentAdmin/add" method="post"
              onsubmit="return validateAddForm()">
            <div>
                <label for="addName">姓名:</label>
                <input type="text" id="addName" name="name" required/>
            </div>
            <div>
                <label for="addUserName">登录名:</label>
                <input type="text" id="addUserName" name="userName" required/>
            </div>
            <div>
                <label for="addPhone">电话:</label>
                <input type="text" id="addPhone" name="phone" required/>
            </div>
            <div>
                <label for="addDepartmentID">部门:</label>
                <select id="addDepartmentID" name="departmentID">
                    <c:forEach var="entry" items="${departmentMap}">
                        <option value="${entry.key}">${entry.value.name}</option>
                    </c:forEach>
                </select>
            </div>
            <div class="button-group">
                <button type="submit">提交</button>
                <button class="cancel" type="button" onclick="closeAddPopup()">取消</button>
            </div>
        </form>
    </div>

    <div id="popup-update" class="popup">
        <h2>修改部门管理员信息</h2>
        <form action="${pageContext.request.contextPath}/admin/departmentAdmin/update" method="post"
              onsubmit="return validateUpdateForm()">
            <input type="hidden" id="updateId" name="id"/>
            <div>
                <label for="updateName">姓名:</label>
                <input type="text" id="updateName" name="name" required/>
            </div>
            <div>
                <label for="updateUserName">登录名:</label>
                <input type="text" id="updateUserName" name="userName" required/>
            </div>
            <div>
                <label for="updatePhone">电话:</label>
                <input type="text" id="updatePhone" name="phone" required/>
            </div>
            <div>
                <label for="updateDepartmentID">部门:</label>
                <select id="updateDepartmentID" name="departmentID">
                    <option value="">请选择</option>
                    <c:forEach var="entry" items="${departmentMap}">
                        <option value="${entry.key}">${entry.value.name}</option>
                    </c:forEach>
                </select>
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

    function updateDepartmentAdmin(id, name, userName, phone, departmentID) {
        document.getElementById("updateId").value = id;
        document.getElementById("updateName").value = name;
        document.getElementById("updateUserName").value = userName;
        document.getElementById("updatePhone").value = phone;
        document.getElementById("updateDepartmentID").value = departmentID;
        showUpdatePopup();
    }

    function deleteDepartmentAdmin(id) {
        window.location.href = "${pageContext.request.contextPath}/admin/departmentAdmin/delete?id=" + id;
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

    function validateAddForm() {
        const addPhone = document.getElementById("addPhone");
        if (!validatePhone(addPhone.value)) {
            alert('手机号格式不正确');
            return false;
        }
        return true
    }

    function validateUpdateForm() {
        const updatePhone = document.getElementById("updatePhone");
        if (!validatePhone(updatePhone.value)) {
            alert('手机号格式不正确');
            return false;
        }
        return true
    }

    function validatePhone(phone) {
        const phoneRegex = /^1[3-9]\d{9}$/;
        return phoneRegex.test(phone);
    }
</script>
</body>
</html>
