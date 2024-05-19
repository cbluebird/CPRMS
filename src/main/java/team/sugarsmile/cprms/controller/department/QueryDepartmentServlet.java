package team.sugarsmile.cprms.controller.department;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import team.sugarsmile.cprms.dto.PaginationDto;
import team.sugarsmile.cprms.exception.BizException;
import team.sugarsmile.cprms.exception.ErrorCode;
import team.sugarsmile.cprms.model.Department;
import team.sugarsmile.cprms.service.DepartmentService;

import java.io.IOException;

/**
 * @author XiMo
 */

@WebServlet("/admin/department/query")
public class QueryDepartmentServlet extends HttpServlet {
    private final DepartmentService departmentService = new DepartmentService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            int id = Integer.parseInt(request.getParameter("id"));
            Department department = departmentService.queryDepartment(id);
            request.setAttribute("department", department);
            request.getRequestDispatcher("/department.jsp").forward(request, response);
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
