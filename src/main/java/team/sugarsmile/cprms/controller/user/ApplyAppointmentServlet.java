package team.sugarsmile.cprms.controller.user;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import team.sugarsmile.cprms.model.Department;
import team.sugarsmile.cprms.service.DepartmentService;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

/**
 * @author XiMo
 */

@WebServlet("/user/appointment/apply")
public class ApplyAppointmentServlet extends HttpServlet {
    private final DepartmentService departmentService = new DepartmentService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HashMap<Integer, Department> departmentMap = new HashMap<>();
        String type = request.getParameter("type");
        if (!"public".equals(type) && !"official".equals(type)) {
            type = "public";
        }
        if ("official".equals(type)) {
            List<Department> departmentList = departmentService.getAll();
            for (Department department : departmentList) {
                departmentMap.put(department.getId(), department);
            }
        }
        request.setAttribute("type", type);
        request.setAttribute("departmentMap", departmentMap);
        request.getRequestDispatcher("/user/applyAppointment.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
}