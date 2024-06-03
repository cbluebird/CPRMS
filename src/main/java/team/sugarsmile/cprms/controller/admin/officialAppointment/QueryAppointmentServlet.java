package team.sugarsmile.cprms.controller.admin.officialAppointment;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import team.sugarsmile.cprms.dto.PaginationDto;
import team.sugarsmile.cprms.exception.BizException;
import team.sugarsmile.cprms.exception.ErrorCode;
import team.sugarsmile.cprms.model.Admin;
import team.sugarsmile.cprms.model.Audit;
import team.sugarsmile.cprms.model.Department;
import team.sugarsmile.cprms.model.OfficialAppointment;
import team.sugarsmile.cprms.service.AuditService;
import team.sugarsmile.cprms.service.DepartmentService;
import team.sugarsmile.cprms.service.OfficialAppointmentService;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

@WebServlet("/admin/appointment/official/query")
public class QueryAppointmentServlet extends HttpServlet {
    private final OfficialAppointmentService officialAppointmentService = new OfficialAppointmentService();
    private final DepartmentService departmentService = new DepartmentService();
    private final AuditService auditService = new AuditService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        BizException be = null;
        PaginationDto<OfficialAppointment> pagination = null;
        HashMap<Integer, Department> departmentMap = new HashMap<Integer, Department>();
        try {
            HttpSession session = request.getSession();
            Admin admin = (Admin) session.getAttribute("admin");
            int pageNum = parseIntegerOrDefault(request.getParameter("pageNum"), 1);
            int pageSize = parseIntegerOrDefault(request.getParameter("pageSize"), 10);
            String applyDate = request.getParameter("create_time");
            String appointmentDate = request.getParameter("start_time");
            Integer campus = parseIntegerOrNull(request.getParameter("campus"));
            String unit = request.getParameter("unit");
            String name = request.getParameter("name");
            String idCard = request.getParameter("idCard");
            String receptionist = request.getParameter("receptionist");
            Integer status = parseIntegerOrNull(request.getParameter("status"));
            Integer departmentId = parseIntegerOrNull(request.getParameter("department"));
            List<Department> departmentList = departmentService.getAll();
            for (Department department : departmentList) {
                departmentMap.put(department.getId(), department);
            }
            if (admin.getAdminType() == Admin.AdminType.DEPARTMENT) {
                Department department = departmentMap.get(admin.getDepartmentID());
                if (department.getBusiness()) {
                    pagination = officialAppointmentService.searchAppointments(applyDate, appointmentDate, campus, unit, name, idCard, receptionist, status, departmentId, pageNum, pageSize);
                } else {
                    pagination = officialAppointmentService.searchAppointments(applyDate, appointmentDate, campus, unit, name, idCard, receptionist, status, department.getId(), pageNum, pageSize);
                }
            } else {
                pagination = officialAppointmentService.searchAppointments(applyDate, appointmentDate, campus, unit, name, idCard, receptionist, status, departmentId, pageNum, pageSize);
            }
            auditService.createAudit("查询公务预约", Audit.AuditType.QUERY, admin.getId());
        } catch (NumberFormatException e) {
            be = new BizException(ErrorCode.PARAM_ERROR.getCode(), e.getMessage());
        }
        if (be != null) {
            request.getSession().setAttribute("error", ErrorCode.getByCode(be.getCode()).getMessage());
            response.sendRedirect(request.getContextPath() + "/officialAppointment.jsp");
            throw be;
        } else {
            request.setAttribute("pagination", pagination);
            request.setAttribute("departmentMap", departmentMap);
            request.getRequestDispatcher("/officialAppointment.jsp").forward(request, response);
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
