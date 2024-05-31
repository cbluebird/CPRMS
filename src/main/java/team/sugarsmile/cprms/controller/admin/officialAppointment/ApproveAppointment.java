package team.sugarsmile.cprms.controller.admin.officialAppointment;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import team.sugarsmile.cprms.model.OfficialAppointment;
import team.sugarsmile.cprms.service.DepartmentService;
import team.sugarsmile.cprms.service.OfficialAppointmentService;

@WebServlet("/admin/appointment/official/approve")
public class ApproveAppointment extends HttpServlet {
    private final OfficialAppointmentService officialAppointmentService=new OfficialAppointmentService();
    private final DepartmentService departmentService = new DepartmentService();
    @Override
    protected void doGet(jakarta.servlet.http.HttpServletRequest request, jakarta.servlet.http.HttpServletResponse response) throws java.io.IOException, ServletException {
        int id= Integer.parseInt(request.getParameter("id"));
        int status = Integer.parseInt(request.getParameter("status"));
        OfficialAppointment a=officialAppointmentService.getOfficialAppointmentById(id);
        officialAppointmentService.approveAppointment(id,OfficialAppointment.Status.getType(status));
        response.sendRedirect(request.getContextPath()+"/admin/appointment/official/list?pageNum=1&pageSize=10");
    }

}
