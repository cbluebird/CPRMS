package team.sugarsmile.cprms.controller.admin;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import team.sugarsmile.cprms.exception.BizException;
import team.sugarsmile.cprms.model.Audit;
import team.sugarsmile.cprms.service.AdminService;
import team.sugarsmile.cprms.service.AuditService;

import java.io.IOException;
import java.net.URLEncoder;

@WebServlet("/auth/rePassword")
public class UpdatePasswordServlet extends HttpServlet {
    private final AdminService adminService = new AdminService();
    private final AuditService auditService = new AuditService();
    Integer userID = 0;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.sendRedirect("/resetPassword.jsp");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            String password = request.getParameter("password");
            userID = Integer.parseInt(request.getParameter("id"));
            adminService.updatePasswordByID(userID, password);
            auditService.createAudit("更新密码", Audit.AuditType.UPDATE, userID);
            response.sendRedirect(request.getContextPath() + "/login.jsp");
        } catch (BizException e) {
            response.sendRedirect(request.getContextPath() + "/login.jsp?error=" + URLEncoder.encode(e.getMessage(), "UTF-8"));
        }
    }
}

