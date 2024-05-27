package team.sugarsmile.cprms.controller.admin.department;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import team.sugarsmile.cprms.exception.BizException;
import team.sugarsmile.cprms.exception.ErrorCode;
import team.sugarsmile.cprms.service.DepartmentService;

import java.io.IOException;

/**
 * @author XiMo
 */

@WebServlet("/admin/department/delete")
public class DeleteDepartmentServlet extends HttpServlet {
    private final DepartmentService departmentService = new DepartmentService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        BizException be = null;
        try {
            int id = Integer.parseInt(request.getParameter("id"));
            departmentService.deleteDepartment(id);
        } catch (BizException e) {
            be = e;
        } catch (NumberFormatException e) {
            be = new BizException(ErrorCode.PARAM_ERROR.getCode(), e.getMessage());
        }
        if (be != null) {
            request.getSession().setAttribute("error", ErrorCode.getByCode(be.getCode()).getMessage());
            response.sendRedirect(request.getContextPath() + "/admin/department/list?pageNum=1&pageSize=10");
            throw be;
        } else {
            response.sendRedirect(request.getContextPath() + "/admin/department/list?pageNum=1&pageSize=10");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
}
