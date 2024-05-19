package team.sugarsmile.cprms.controller.department;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import team.sugarsmile.cprms.exception.BizException;
import team.sugarsmile.cprms.exception.ErrorCode;
import team.sugarsmile.cprms.model.Department;
import team.sugarsmile.cprms.service.DepartmentService;

import java.io.IOException;
import java.net.BindException;

/**
 * @author XiMo
 */

@WebServlet("/admin/department/add")
public class AddDepartmentServlet extends HttpServlet {
    private final DepartmentService departmentService = new DepartmentService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            Department.Type type = Department.Type.getType(Integer.parseInt(request.getParameter("type")));
            String name = request.getParameter("name");
            if (name.isEmpty()){
                throw new BizException(ErrorCode.PARAM_ERROR.getCode(),"param name is empty");
            }
            departmentService.addDepartment(type, name);
            response.sendRedirect("/admin/department/list?pageNum=1&pageSize=10");
        } catch (BizException e) {
            request.setAttribute("error", ErrorCode.getByCode(e.getCode()).getMessage());
            request.getRequestDispatcher("/department.jsp").forward(request, response);
            throw e;
        } catch (IllegalArgumentException e) {
            request.setAttribute("error", ErrorCode.PARAM_ERROR.getMessage());
            request.getRequestDispatcher("/department.jsp").forward(request, response);
            throw new BizException(ErrorCode.PARAM_ERROR.getCode(), e.getMessage());
        }
    }
}
