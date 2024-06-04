package team.sugarsmile.cprms.controller.admin.audit;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import team.sugarsmile.cprms.dto.PaginationDto;
import team.sugarsmile.cprms.exception.BizException;
import team.sugarsmile.cprms.exception.ErrorCode;
import team.sugarsmile.cprms.model.Audit;
import team.sugarsmile.cprms.model.Department;
import team.sugarsmile.cprms.service.AuditService;
import team.sugarsmile.cprms.service.DepartmentService;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

@WebServlet("/admin/audit/query")
public class QueryAuditServlet extends HttpServlet {
    private final AuditService auditService = new AuditService();
    private final DepartmentService departmentService = new DepartmentService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        BizException be = null;
        PaginationDto<Audit> pagination = null;
        HashMap<Integer, Department> departmentMap = new HashMap<Integer, Department>();
        try {
            int pageNum = parseIntegerOrDefault(request.getParameter("pageNum"), 1);
            int pageSize = parseIntegerOrDefault(request.getParameter("pageSize"), 10);
            String applyDate = request.getParameter("create_time");
            String operate = request.getParameter("operate");
            Integer type = parseIntegerOrNull(request.getParameter("type"));
            Integer adminID = parseIntegerOrNull(request.getParameter("admin_id"));
            pagination = auditService.searchAppointments(operate, type, adminID, applyDate, pageNum, pageSize);
            List<Department> departmentList = departmentService.getAll();
            for (Department department : departmentList) {
                departmentMap.put(department.getId(), department);
            }
        } catch (NumberFormatException e) {
            be = new BizException(ErrorCode.PARAM_ERROR.getCode(), e.getMessage());
        }
        if (be != null) {
            request.getSession().setAttribute("error", ErrorCode.getByCode(be.getCode()).getMessage());
            response.sendRedirect(request.getContextPath() + "/admin/audit/list?pageNum=1&pageSize=10");
            throw be;
        } else {
            request.setAttribute("pagination", pagination);
            request.setAttribute("departmentMap", departmentMap);
            request.getRequestDispatcher("/admin/audit.jsp").forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }

    private Integer parseIntegerOrNull(String value) {
        try {
            return (value != null && !value.trim().isEmpty()) ? Integer.parseInt(value) : null;
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private int parseIntegerOrDefault(String value, int defaultValue) {
        try {
            return (value != null && !value.trim().isEmpty()) ? Integer.parseInt(value) : defaultValue;
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }
}