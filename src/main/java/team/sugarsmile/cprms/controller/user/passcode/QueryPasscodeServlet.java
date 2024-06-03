package team.sugarsmile.cprms.controller.user.passcode;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import team.sugarsmile.cprms.dto.PasscodeDto;
import team.sugarsmile.cprms.exception.BizException;
import team.sugarsmile.cprms.exception.ErrorCode;
import team.sugarsmile.cprms.service.OfficialAppointmentService;
import team.sugarsmile.cprms.service.PublicAppointmentService;

import java.io.IOException;

/**
 * @author XiMo
 */

@WebServlet("/user/passcode/query")
public class QueryPasscodeServlet extends HttpServlet {
    private static final PublicAppointmentService publicAppointmentService = new PublicAppointmentService();
    private static final OfficialAppointmentService officialAppointmentService = new OfficialAppointmentService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        BizException be = null;
        String type = null;
        int id = 0;
        PasscodeDto passcode = null;
        try {
            String name = (String) request.getSession().getAttribute("name");
            String idCard = (String) request.getSession().getAttribute("idCard");
            String phone = (String) request.getSession().getAttribute("phone");
            if (name == null || idCard == null || phone == null) {
                response.sendRedirect(request.getContextPath() + "/user/appointment.jsp");
                return;
            }
            type = request.getParameter("type");
            id = Integer.parseInt(request.getParameter("id"));
            if (!"public".equals(type) && !"official".equals(type)) {
                type = "public";
            }
            if (id <= 0) {
                throw new BizException(ErrorCode.PARAM_ERROR.getCode(), "参数id为空或有误");
            }
            switch (type) {
                case "public" -> {
                    passcode = publicAppointmentService.getPasscode(id, name, idCard, phone);
                }
                case "official" -> {
                    passcode = officialAppointmentService.getPasscode(id, name, idCard, phone);
                }
            }
        } catch (BizException e) {
            be = e;
        } catch (NumberFormatException e) {
            be = new BizException(ErrorCode.PARAM_ERROR.getCode(), e.getMessage());
        }
        if (be != null) {
            request.getSession().setAttribute("error", ErrorCode.getByCode(be.getCode()).getMessage());
            response.sendRedirect(request.getContextPath() + "/user/appointment/" + type + "/list?pageNum=1&pageSize=10");
            throw be;
        } else {
            request.setAttribute("passcode", passcode);
            request.getRequestDispatcher("/user/passcode.jsp?type=" + type + "&id=" + id).forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
}