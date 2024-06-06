package team.sugarsmile.cprms.controller.user.officialAppointment;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import team.sugarsmile.cprms.exception.BizException;
import team.sugarsmile.cprms.exception.ErrorCode;
import team.sugarsmile.cprms.model.OfficialAppointment;
import team.sugarsmile.cprms.service.OfficialAppointmentService;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

/**
 * @author XiMo
 */

@WebServlet("/user/appointment/official/add")
public class AddAppointmentServlet extends HttpServlet {
    private final OfficialAppointmentService officialAppointmentService = new OfficialAppointmentService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        BizException be = null;
        try {
            OfficialAppointment.Campus campus = OfficialAppointment.Campus.getType(Integer.parseInt(request.getParameter("campus")));
            LocalDate appointmentTime = LocalDate.parse(request.getParameter("appointmentTime"));
            String unit = request.getParameter("unit");
            int departmentID = Integer.parseInt(request.getParameter("departmentID"));
            String receptionist = request.getParameter("receptionist");
            String reason = request.getParameter("reason");
            String[] names = request.getParameterValues("names");
            String[] idCards = request.getParameterValues("idCards");
            String[] phones = request.getParameterValues("phones");
            String[] transportations = request.getParameterValues("transportations");
            String[] licensePlates = request.getParameterValues("licensePlates");
            for (int i = 0; i < names.length; i++) {
                officialAppointmentService.addOfficialAppointment(OfficialAppointment.builder()
                        .createTime(LocalDate.now())
                        .name(names[i])
                        .idCard(idCards[i])
                        .phone(phones[i])
                        .campus(campus)
                        .appointmentTime(appointmentTime)
                        .unit(unit)
                        .transportation(OfficialAppointment.Transportation.getType(Integer.parseInt(transportations[i])))
                        .licensePlate(licensePlates[i])
                        .departmentId(departmentID)
                        .receptionist(receptionist)
                        .reason(reason)
                        .status(OfficialAppointment.Status.UNREVIEWED)
                        .build());
            }
        } catch (BizException e) {
            be = e;
        } catch (IllegalArgumentException | DateTimeParseException e) {
            be = new BizException(ErrorCode.PARAM_ERROR.getCode(), e.getMessage());
        }
        if (be != null) {
            request.getSession().setAttribute("error", ErrorCode.getByCode(be.getCode()).getMessage());
            response.sendRedirect(request.getContextPath() + "/user/appointment/apply?type=official");
            throw be;
        } else {
            response.sendRedirect(request.getContextPath() + "/user/appointment.jsp");
        }
    }
}


