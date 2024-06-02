package team.sugarsmile.cprms.controller.admin.publicAppointment;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import team.sugarsmile.cprms.dto.PaginationDto;
import team.sugarsmile.cprms.exception.BizException;
import team.sugarsmile.cprms.exception.ErrorCode;
import team.sugarsmile.cprms.model.*;
import team.sugarsmile.cprms.service.AuditService;
import team.sugarsmile.cprms.service.DepartmentService;
import team.sugarsmile.cprms.service.OfficialAppointmentService;
import team.sugarsmile.cprms.service.PublicAppointmentService;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

@WebServlet("/admin/appointment/public/query")
public class QueryAppointment extends HttpServlet {
    private final PublicAppointmentService publicAppointmentService = new PublicAppointmentService();
    private final DepartmentService departmentService = new DepartmentService();
    private final AuditService auditService = new AuditService();
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        BizException be = null;
        PaginationDto<PublicAppointment> pagination = null;
        HashMap<Integer, Department> departmentMap=new HashMap<Integer, Department>() ;

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
            pagination = publicAppointmentService.searchAppointments(applyDate, appointmentDate, campus, unit, name,idCard,pageNum, pageSize);
            List<Department> d= departmentService.getAll();
            for(Department department:d){
                departmentMap.put(department.getId(),department);
            }
            auditService.createAudit("查询社会预约", Audit.AuditType.QUERY,admin.getId());
        } catch (NumberFormatException e) {
            be = new BizException(ErrorCode.PARAM_ERROR.getCode(), e.getMessage());
        }

        if (be != null) {
            request.getSession().setAttribute("error", ErrorCode.getByCode(be.getCode()).getMessage());
            response.sendRedirect(request.getContextPath() + "/admin/appointment/public/list?pageNum=1&pageSize=10.jsp");
            throw be;
        } else {
            request.setAttribute("pagination", pagination);
            request.setAttribute("departmentMap",departmentMap);
            request.getRequestDispatcher("/publicAppointment.jsp").forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
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
