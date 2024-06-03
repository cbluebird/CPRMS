package team.sugarsmile.cprms.controller.admin;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import team.sugarsmile.cprms.exception.BizException;
import team.sugarsmile.cprms.exception.ErrorCode;
import team.sugarsmile.cprms.model.Admin;
import team.sugarsmile.cprms.model.Audit;
import team.sugarsmile.cprms.service.AdminService;
import team.sugarsmile.cprms.service.AuditService;
import team.sugarsmile.cprms.util.JedisUtil;
import team.sugarsmile.cprms.util.SM3;

import java.net.URLEncoder;
import java.sql.Date;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Objects;

@WebServlet("/auth/login")
public class LoginServlet extends HttpServlet {
    private final AdminService adminService = new AdminService();
    private final AuditService auditService = new AuditService();

    @Override
    protected void doGet(jakarta.servlet.http.HttpServletRequest req, jakarta.servlet.http.HttpServletResponse resp) throws java.io.IOException {
        resp.sendRedirect(req.getContextPath()+"/login.jsp");
    }

    @Override
    protected void doPost(jakarta.servlet.http.HttpServletRequest req, jakarta.servlet.http.HttpServletResponse resp) throws java.io.IOException, ServletException {
        String username = req.getParameter("username");
        String password = req.getParameter("password");
        try {

            Integer a = JedisUtil.getLoginKey(username);
            if (a > 5) {
                throw new BizException(ErrorCode.PASSWORD_ERROR_TO_MANY.getCode(), "用户" + username + "密码错误五次以上,请五分钟后再尝试");
            }

            Admin admin = adminService.checkAdminByPassword(username, password);

            Calendar calendar = new GregorianCalendar();
            calendar.setTime(admin.getDate());
            calendar.add(calendar.DATE, 90);
            java.util.Date utilDate = (java.util.Date) calendar.getTime();
            Date outDate = new Date(utilDate.getTime());

            Date nowDate = new Date(new java.util.Date().getTime());

            if (admin.getPassword().equals(SM3.encrypt("zjut123456")) || outDate.before(nowDate)) {
                resp.sendRedirect(req.getContextPath() + "/resetPassword.jsp?id=" + URLEncoder.encode(admin.getId().toString(), "UTF-8"));
                return;
            }

            if (admin != null) {
                req.getSession().setAttribute("admin", admin);
                auditService.createAudit("管理员登录", Audit.AuditType.LOGIN,admin.getId());
                resp.sendRedirect(req.getContextPath() + "/homepage.jsp");
            } else {
                resp.sendRedirect(req.getContextPath() + "/login.jsp");
            }
        } catch (BizException e) {
            req.setAttribute("error", ErrorCode.getByCode(e.getCode()).getMessage());
            if (Objects.equals(e.getCode(), ErrorCode.ADMIN_LOGIN_ERROR.getCode())) {
                JedisUtil.incrementLoginKey(username);
            }
            req.getRequestDispatcher("/login.jsp").forward(req, resp);
            throw e;
        }
    }

}

