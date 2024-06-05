package team.sugarsmile.cprms.controller.admin.officialAppointment;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import team.sugarsmile.cprms.exception.BizException;
import team.sugarsmile.cprms.exception.ErrorCode;
import team.sugarsmile.cprms.model.Admin;
import team.sugarsmile.cprms.model.Audit;
import team.sugarsmile.cprms.model.OfficialAppointment;
import team.sugarsmile.cprms.service.AuditService;
import team.sugarsmile.cprms.service.OfficialAppointmentService;

import java.io.IOException;

@WebServlet("/admin/appointment/official/review")
public class ReviewAppointmentServlet extends HttpServlet {
    private final OfficialAppointmentService officialAppointmentService = new OfficialAppointmentService();
    private final AuditService auditService = new AuditService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        BizException be = null;
        try {
            Admin admin = (Admin) request.getSession().getAttribute("admin");
            int id = Integer.parseInt(request.getParameter("id"));
            int status = Integer.parseInt(request.getParameter("status"));
            officialAppointmentService.getOfficialAppointmentById(id);
            officialAppointmentService.reviewAppointment(id, OfficialAppointment.Status.getType(status));
            auditService.createAudit("审批公务预约", Audit.AuditType.UPDATE, admin.getId());
        } catch (BizException e) {
            be = e;
        } catch (IllegalArgumentException e) {
            be = new BizException(ErrorCode.PARAM_ERROR.getCode(), e.getMessage());
        }
        if (be != null) {
            request.getSession().setAttribute("error", ErrorCode.getByCode(be.getCode()).getMessage());
            response.sendRedirect(request.getContextPath() + "/admin/appointment/official/list?pageNum=1&pageSize=10");
            throw be;
        } else {
            response.sendRedirect(request.getContextPath() + "/admin/appointment/official/list?pageNum=1&pageSize=10");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
}
