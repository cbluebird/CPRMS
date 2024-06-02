package team.sugarsmile.cprms.controller.admin.audit;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import team.sugarsmile.cprms.dto.PaginationDto;
import team.sugarsmile.cprms.exception.BizException;
import team.sugarsmile.cprms.exception.ErrorCode;
import team.sugarsmile.cprms.model.Audit;
import team.sugarsmile.cprms.model.Department;
import team.sugarsmile.cprms.service.AuditService;
import team.sugarsmile.cprms.service.DepartmentService;

import java.util.HashMap;
import java.util.List;

@WebServlet("/admin/audit/list")
public class ListAudit extends HttpServlet {
    private final AuditService auditService = new AuditService();
    private final DepartmentService departmentService = new DepartmentService();

    @Override
    protected void doGet(jakarta.servlet.http.HttpServletRequest request, jakarta.servlet.http.HttpServletResponse response) throws java.io.IOException, ServletException {
        BizException be = null;
        PaginationDto<Audit> pagination = null;
        HashMap<Integer, Department> departmentMap = new HashMap<Integer, Department>();
        try {
            int pageNum = Integer.parseInt(request.getParameter("pageNum"));
            int pageSize = Integer.parseInt(request.getParameter("pageSize"));
            pagination = auditService.findAuditList(pageNum, pageSize);
            List<Department> d = departmentService.getAll();
            for (Department department : d) {
                departmentMap.put(department.getId(), department);
            }
        } catch (NumberFormatException e) {
            be = new BizException(ErrorCode.PARAM_ERROR.getCode(), e.getMessage());
        }
        if (be != null) {
            request.getSession().setAttribute("error", ErrorCode.getByCode(be.getCode()).getMessage());
            response.sendRedirect(request.getContextPath() + "/audit.jsp");
            throw be;
        } else {
            request.setAttribute("pagination", pagination);
            request.setAttribute("departmentMap", departmentMap);
            request.getRequestDispatcher("/audit.jsp").forward(request, response);
        }
    }

    @Override
    protected void doPost(jakarta.servlet.http.HttpServletRequest request, jakarta.servlet.http.HttpServletResponse response) throws java.io.IOException, ServletException {
        doGet(request, response);
    }
}
