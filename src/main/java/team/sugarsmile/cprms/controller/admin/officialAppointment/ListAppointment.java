package team.sugarsmile.cprms.controller.admin.officialAppointment;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpSession;
import team.sugarsmile.cprms.dto.PaginationDto;
import team.sugarsmile.cprms.exception.BizException;
import team.sugarsmile.cprms.exception.ErrorCode;
import team.sugarsmile.cprms.model.Admin;
import team.sugarsmile.cprms.model.Department;
import team.sugarsmile.cprms.model.OfficialAppointment;
import team.sugarsmile.cprms.service.DepartmentService;
import team.sugarsmile.cprms.service.OfficialAppointmentService;

import java.util.HashMap;
import java.util.List;

@WebServlet("/admin/appointment/official/list")
public class ListAppointment extends HttpServlet {
    private final OfficialAppointmentService officialAppointmentService = new OfficialAppointmentService();
    private final DepartmentService departmentService = new DepartmentService();

    @Override
    protected void doGet(jakarta.servlet.http.HttpServletRequest request, jakarta.servlet.http.HttpServletResponse response) throws java.io.IOException, ServletException {
        BizException be = null;
        PaginationDto<OfficialAppointment> pagination = null;
        HashMap<Integer, Department> departmentMap = new HashMap<Integer, Department>();
        try {
            HttpSession session = request.getSession();
            Admin admin = (Admin) session.getAttribute("admin");
            int pageNum = Integer.parseInt(request.getParameter("pageNum"));
            int pageSize = Integer.parseInt(request.getParameter("pageSize"));
            List<Department> d = departmentService.getAll();
            for (Department department : d) {
                departmentMap.put(department.getId(), department);
            }

            if (admin.getAdminType() == Admin.AdminType.DEPARTMENT) {
                Department department = departmentMap.get(admin.getDepartmentID());
                if (department.getBusiness()) {
                    pagination = officialAppointmentService.findOfficialAppointmentList(pageNum, pageSize);
                } else {
                    pagination = officialAppointmentService.searchAppointments(null, null, null, null, null, null, null, null, department.getId(), pageNum, pageSize);
                    departmentMap.clear();
                    departmentMap.put(department.getId(), department);
                }
            } else {
                pagination = officialAppointmentService.findOfficialAppointmentList(pageNum, pageSize);
            }

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
    protected void doPost(jakarta.servlet.http.HttpServletRequest request, jakarta.servlet.http.HttpServletResponse response) throws java.io.IOException, ServletException {
        doGet(request, response);
    }
}
