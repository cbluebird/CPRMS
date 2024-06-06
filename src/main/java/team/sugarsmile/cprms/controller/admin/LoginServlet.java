package team.sugarsmile.cprms.controller.admin;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import team.sugarsmile.cprms.exception.BizException;
import team.sugarsmile.cprms.exception.ErrorCode;
import team.sugarsmile.cprms.model.Admin;
import team.sugarsmile.cprms.model.Audit;
import team.sugarsmile.cprms.service.AdminService;
import team.sugarsmile.cprms.service.AuditService;
import team.sugarsmile.cprms.util.JedisUtil;
import team.sugarsmile.cprms.util.SM3Util;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Objects;

@WebServlet("/auth/login")
public class LoginServlet extends HttpServlet {
    private final AdminService adminService = new AdminService();
    private final AuditService auditService = new AuditService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        BizException be = null;
        String username = request.getParameter("username");
        Admin admin = null;
        Boolean updatePassword = null;
        try {
            String password = request.getParameter("password");
            int count = JedisUtil.getLoginKey(username);
            if (count >= 5) {
                throw new BizException(ErrorCode.PASSWORD_ERROR_TO_MANY.getCode(), "用户" + username + "密码错误五次以上,请五分钟后再尝试");
            }
            admin = adminService.checkAdminByPassword(username, password);
            request.getSession().setAttribute("admin", admin);
            String phone = admin.getPhone();
            if (admin.getPassword().equals(SM3Util.encrypt("zjut" + phone.substring(phone.length() - 6))) || LocalDate.now().minusDays(90).isAfter(admin.getPasswordUpdateTime())) {
                updatePassword = true;
            }
            auditService.createAudit("管理员登录", Audit.AuditType.LOGIN, admin.getId());
        } catch (BizException e) {
            be = e;
        }
        if (be != null) {
            if (Objects.equals(be.getCode(), ErrorCode.ADMIN_LOGIN_ERROR.getCode())) {
                JedisUtil.incrementLoginKey(username);
            }
            request.setAttribute("error", ErrorCode.getByCode(be.getCode()).getMessage());
            request.getRequestDispatcher("/admin/login.jsp").forward(request, response);
            throw be;
        } else {
            request.getSession().setAttribute("admin", admin);
            request.getSession().setAttribute("updatePassword", updatePassword);
            JedisUtil.delLoginKey(username);
            response.sendRedirect(request.getContextPath() + "/admin/home.jsp");
        }
    }
}

