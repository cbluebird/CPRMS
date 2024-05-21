package team.sugarsmile.cprms.controller.admin.department;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
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
        try {
            int id = Integer.parseInt(request.getParameter("id"));
            departmentService.deleteDepartment(id);
            response.sendRedirect("/admin/department/list?pageNum=1&pageSize=10");
        } catch (BizException e) {
            request.setAttribute("error", ErrorCode.getByCode(e.getCode()).getMessage());
            request.getRequestDispatcher("/department.jsp").forward(request, response);
            throw e;
        } catch (NumberFormatException e) {
            request.setAttribute("error", ErrorCode.PARAM_ERROR.getMessage());
            request.getRequestDispatcher("/department.jsp").forward(request, response);
            throw new BizException(ErrorCode.PARAM_ERROR.getCode(), e.getMessage());
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
