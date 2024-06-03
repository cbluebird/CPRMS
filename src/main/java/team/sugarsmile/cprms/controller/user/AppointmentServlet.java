package team.sugarsmile.cprms.controller.user;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import team.sugarsmile.cprms.exception.BizException;
import team.sugarsmile.cprms.exception.ErrorCode;
import team.sugarsmile.cprms.service.OfficialAppointmentService;
import team.sugarsmile.cprms.service.PublicAppointmentService;

import java.io.IOException;

/**
 * @author XiMo
 */

@WebServlet("/user/appointment")
public class AppointmentServlet extends HttpServlet {
    private final PublicAppointmentService publicAppointmentService = new PublicAppointmentService();
    private final OfficialAppointmentService officialAppointmentService = new OfficialAppointmentService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        BizException be = null;
        try {
            String name = request.getParameter("name");
            String idCard = request.getParameter("idCard");
            String phone = request.getParameter("phone");
            if (publicAppointmentService.getTotal(name, idCard, phone) + officialAppointmentService.getTotal(name, idCard, phone) == 0) {
                throw new BizException(ErrorCode.APPOINTMENT_HISTORY_NOT_EXIST);
            }
            request.getSession().setAttribute("name", name);
            request.getSession().setAttribute("idCard", idCard);
            request.getSession().setAttribute("phone", phone);
        } catch (BizException e) {
            be = e;
        }
        if (be != null) {
            request.setAttribute("error", ErrorCode.getByCode(be.getCode()).getMessage());
            request.getRequestDispatcher("/user/appointment.jsp").forward(request, response);
            throw be;
        } else {
            response.sendRedirect(request.getContextPath() + "/user/appointment/public/list?pageNum=1&pageSize=10");
        }
    }
}