package team.sugarsmile.cprms.controller.admin.departmentAdmin;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import team.sugarsmile.cprms.exception.BizException;
import team.sugarsmile.cprms.exception.ErrorCode;
import team.sugarsmile.cprms.model.Admin;
import team.sugarsmile.cprms.model.Department;
import team.sugarsmile.cprms.service.AdminService;
import team.sugarsmile.cprms.service.DepartmentService;

import java.io.IOException;

@WebServlet("/admin/departmentAdmin/update")
public class UpdateDepartmentAdminServlet extends HttpServlet {
    private final AdminService adminService = new AdminService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        BizException be = null;
        try {
            int id = Integer.parseInt(request.getParameter("id"));
            String name = request.getParameter("name");
            String userName = request.getParameter("userName");
            String phone = request.getParameter("phone");
            Integer departmentID =Integer.parseInt( request.getParameter("departmentID"));
            if (name.isEmpty()) {
                throw new BizException(ErrorCode.PARAM_ERROR.getCode(), "参数不能为空");
            }
            adminService.updateAdmin(id, Admin.AdminType.DEPARTMENT,phone,name,userName,departmentID);
        } catch (BizException e) {
            be = e;
        } catch (IllegalArgumentException e) {
            be = new BizException(ErrorCode.PARAM_ERROR.getCode(), e.getMessage());
        }
        if (be != null) {
            request.getSession().setAttribute("error", ErrorCode.getByCode(be.getCode()).getMessage());
            response.sendRedirect(request.getContextPath() + "/admin/departmentAdmin/list?pageNum=1&pageSize=10");
            throw be;
        } else {
            response.sendRedirect(request.getContextPath() + "/admin/departmentAdmin/list?pageNum=1&pageSize=10");
        }
    }
}
