package team.sugarsmile.cprms.controller.admin.department;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import team.sugarsmile.cprms.dto.PaginationDto;
import team.sugarsmile.cprms.exception.BizException;
import team.sugarsmile.cprms.exception.ErrorCode;
import team.sugarsmile.cprms.model.Department;
import team.sugarsmile.cprms.service.DepartmentService;

import java.io.IOException;

/**
 * @author XiMo
 */

@WebServlet("/admin/department/list")
public class ListDepartmentServlet extends HttpServlet {
    private final DepartmentService departmentService = new DepartmentService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        BizException be = null;
        PaginationDto<Department> pagination = null;
        try {
            int pageNum = Integer.parseInt(request.getParameter("pageNum"));
            int pageSize = Integer.parseInt(request.getParameter("pageSize"));
            pagination = departmentService.findDepartmentList(pageNum, pageSize);
        } catch (NumberFormatException e) {
            be = new BizException(ErrorCode.PARAM_ERROR.getCode(), e.getMessage());
        }
        if (be != null) {
            request.getSession().setAttribute("error", ErrorCode.getByCode(be.getCode()).getMessage());
            response.sendRedirect(request.getContextPath() + "/department.jsp");
            throw be;
        } else {
            request.setAttribute("pagination", pagination);
            request.getRequestDispatcher(request.getContextPath()+"/department.jsp").forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
}
