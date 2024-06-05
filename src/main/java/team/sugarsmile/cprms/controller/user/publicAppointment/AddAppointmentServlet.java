package team.sugarsmile.cprms.controller.user.publicAppointment;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import team.sugarsmile.cprms.exception.BizException;
import team.sugarsmile.cprms.exception.ErrorCode;
import team.sugarsmile.cprms.model.PublicAppointment;
import team.sugarsmile.cprms.service.PublicAppointmentService;

import java.io.IOException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 * @author XiMo
 */

@WebServlet("/user/appointment/public/add")
public class AddAppointmentServlet extends HttpServlet {
    private final PublicAppointmentService publicAppointmentService = new PublicAppointmentService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        BizException be = null;
        try {
            PublicAppointment.Campus campus = PublicAppointment.Campus.getType(Integer.parseInt(request.getParameter("campus")));
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Timestamp startTime = new Timestamp(dateFormat.parse(request.getParameter("startTime")).getTime());
            Timestamp endTime = new Timestamp(dateFormat.parse(request.getParameter("endTime")).getTime());
            String unit = request.getParameter("unit");
            String[] names = request.getParameterValues("names");
            String[] idCards = request.getParameterValues("idCards");
            String[] phones = request.getParameterValues("phones");
            String[] transportations = request.getParameterValues("transportations");
            String[] licensePlates = request.getParameterValues("licensePlates");
            for (int i = 0; i < names.length; i++) {
                publicAppointmentService.addPublicAppointment(PublicAppointment.builder()
                        .createTime(new Timestamp(System.currentTimeMillis()))
                        .name(names[i])
                        .idCard(idCards[i])
                        .phone(phones[i])
                        .campus(campus)
                        .startTime(startTime)
                        .endTime(endTime)
                        .unit(unit)
                        .transportation(PublicAppointment.Transportation.getType(Integer.parseInt(transportations[i])))
                        .licensePlate(licensePlates[i])
                        .build());
            }
        } catch (IllegalArgumentException | ParseException e) {
            be = new BizException(ErrorCode.PARAM_ERROR.getCode(), e.getMessage());
        }
        if (be != null) {
            request.getSession().setAttribute("error", ErrorCode.getByCode(be.getCode()).getMessage());
            response.sendRedirect(request.getContextPath() + "/user/appointment/apply?type=public");
            throw be;
        } else {
            response.sendRedirect(request.getContextPath() + "/user/appointment.jsp");
        }
    }
}

