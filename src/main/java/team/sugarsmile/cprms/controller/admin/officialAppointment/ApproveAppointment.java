package team.sugarsmile.cprms.controller.admin.officialAppointment;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpSession;
import team.sugarsmile.cprms.model.Admin;
import team.sugarsmile.cprms.model.Audit;
import team.sugarsmile.cprms.model.OfficialAppointment;
import team.sugarsmile.cprms.service.AuditService;
import team.sugarsmile.cprms.service.DepartmentService;
import team.sugarsmile.cprms.service.OfficialAppointmentService;

@WebServlet("/admin/appointment/official/approve")
public class ApproveAppointment extends HttpServlet {
    private final OfficialAppointmentService officialAppointmentService=new OfficialAppointmentService();
    private final DepartmentService departmentService = new DepartmentService();
    private final AuditService auditService = new AuditService();
    @Override
    protected void doGet(jakarta.servlet.http.HttpServletRequest request, jakarta.servlet.http.HttpServletResponse response) throws java.io.IOException, ServletException {
        HttpSession session = request.getSession();
        Admin admin = (Admin) session.getAttribute("admin");
        int id= Integer.parseInt(request.getParameter("id"));
        int status = Integer.parseInt(request.getParameter("status"));
        OfficialAppointment a=officialAppointmentService.getOfficialAppointmentById(id);
        officialAppointmentService.approveAppointment(id,OfficialAppointment.Status.getType(status));
        auditService.createAudit("审批公务预约", Audit.AuditType.UPDATE,admin.getId());
        response.sendRedirect(request.getContextPath()+"/admin/appointment/official/list?pageNum=1&pageSize=10");
    }

    @Override
    protected void doPost(jakarta.servlet.http.HttpServletRequest request, jakarta.servlet.http.HttpServletResponse response) throws java.io.IOException, ServletException {
        doGet(request, response);
    }

}
