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
import team.sugarsmile.cprms.model.Admin;
import team.sugarsmile.cprms.model.Department;
import team.sugarsmile.cprms.model.PublicAppointment;
import team.sugarsmile.cprms.service.DepartmentService;
import team.sugarsmile.cprms.service.PublicAppointmentService;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

@WebServlet("/admin/appointment/public/list")
public class ListAppointmentServlet extends HttpServlet {
    private final PublicAppointmentService publicAppointmentService = new PublicAppointmentService();
    private final DepartmentService departmentService = new DepartmentService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        BizException be = null;
        PaginationDto<PublicAppointment> pagination = null;
        HashMap<Integer, Department> departmentMap = new HashMap<Integer, Department>();
        try {
            HttpSession session = request.getSession();
            Admin admin = (Admin) session.getAttribute("admin");
            int pageNum = Integer.parseInt(request.getParameter("pageNum"));
            int pageSize = Integer.parseInt(request.getParameter("pageSize"));
            List<Department> departmentList = departmentService.getAll();
            for (Department department : departmentList) {
                departmentMap.put(department.getId(), department);
            }
            if (admin.getAdminType() == Admin.AdminType.DEPARTMENT) {
                Department department = departmentMap.get(admin.getDepartmentID());
                if (!department.getSocial()) {
                    response.sendRedirect(request.getContextPath() + "/admin/home.jsp");
                    return;
                }
            }
            pagination = publicAppointmentService.findPublicAppointmentList(pageNum, pageSize);
        } catch (NumberFormatException e) {
            be = new BizException(ErrorCode.PARAM_ERROR.getCode(), e.getMessage());
        }
        if (be != null) {
            request.getSession().setAttribute("error", ErrorCode.getByCode(be.getCode()).getMessage());
            response.sendRedirect(request.getContextPath() + "/admin/appointment/public/list?pageNum=1&pageSize=10");
            throw be;
        } else {
            request.setAttribute("pagination", pagination);
            request.setAttribute("departmentMap", departmentMap);
            request.getRequestDispatcher("/admin/publicAppointment.jsp").forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
}