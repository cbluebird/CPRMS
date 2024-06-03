package team.sugarsmile.cprms.controller.user.publicAppointment;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import team.sugarsmile.cprms.dto.PaginationDto;
import team.sugarsmile.cprms.exception.BizException;
import team.sugarsmile.cprms.exception.ErrorCode;
import team.sugarsmile.cprms.model.PublicAppointment;
import team.sugarsmile.cprms.service.PublicAppointmentService;

import java.io.IOException;

/**
 * @author XiMo
 */

@WebServlet("/user/appointment/public/list")
public class ListAppointmentServlet extends HttpServlet {
    private final PublicAppointmentService publicAppointmentService = new PublicAppointmentService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        BizException be = null;
        PaginationDto<PublicAppointment> pagination = null;
        try {
            String name = (String) request.getSession().getAttribute("name");
            String idCard = (String) request.getSession().getAttribute("idCard");
            String phone = (String) request.getSession().getAttribute("phone");
            if (name == null || idCard == null || phone == null) {
                response.sendRedirect(request.getContextPath() + "/user/appointment.jsp");
                return;
            }
            int pageNum = Integer.parseInt(request.getParameter("pageNum"));
            int pageSize = Integer.parseInt(request.getParameter("pageSize"));
            pagination = publicAppointmentService.findPublicAppointmentList(name, idCard, phone, pageNum, pageSize);
        } catch (NumberFormatException e) {
            be = new BizException(ErrorCode.PARAM_ERROR.getCode(), e.getMessage());
        }
        if (be != null) {
            request.getSession().setAttribute("error", ErrorCode.getByCode(be.getCode()).getMessage());
            response.sendRedirect(request.getContextPath() + "/user/publicAppointmentHistory.jsp");
            throw be;
        } else {
            request.setAttribute("pagination", pagination);
            request.getRequestDispatcher("/user/publicAppointmentHistory.jsp").forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
}
