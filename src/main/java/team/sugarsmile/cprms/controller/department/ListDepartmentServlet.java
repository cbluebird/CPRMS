package team.sugarsmile.cprms.controller.department;

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
        try {
            int pageNum = Integer.parseInt(request.getParameter("pageNum"));
            int pageSize = Integer.parseInt(request.getParameter("pageSize"));
            PaginationDto<Department> departmentList = departmentService.findDepartmentList(pageNum, pageSize);
            request.setAttribute("list", departmentList);
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
