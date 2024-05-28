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

import java.io.IOException;

@WebServlet("/admin/departmentAdmin/add")
public class AddDepartmentAdminServlet extends HttpServlet {
    private final AdminService adminService =new AdminService();
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        BizException be = null;
        try {
            String name = request.getParameter("name");
            String phone = request.getParameter("phone");
            String userName = request.getParameter("userName");
            System.out.println(name);
            System.out.println(phone);
            System.out.println(userName);
            Integer departmentID=Integer.parseInt(request.getParameter("departmentID"));
            if (name.isEmpty()||userName.isEmpty()||phone.isEmpty()) {
                throw new BizException(ErrorCode.PARAM_ERROR.getCode(), "参数不能为空");
            }
            adminService.addAdmin(Admin.AdminType.DEPARTMENT,phone,name,userName,departmentID);
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
