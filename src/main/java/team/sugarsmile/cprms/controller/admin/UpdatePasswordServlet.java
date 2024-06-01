package team.sugarsmile.cprms.controller.admin;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import team.sugarsmile.cprms.exception.BizException;
import team.sugarsmile.cprms.service.AdminService;

import java.net.URLEncoder;

@WebServlet("/auth/rePassword")
public class UpdatePasswordServlet extends HttpServlet {
    private final AdminService adminService = new AdminService();
    Integer userID = 0;

    @Override
    protected void doGet(jakarta.servlet.http.HttpServletRequest req, jakarta.servlet.http.HttpServletResponse resp) throws java.io.IOException {
        resp.sendRedirect("/resetPassword.jsp");
    }

    @Override
    protected void doPost(jakarta.servlet.http.HttpServletRequest req, jakarta.servlet.http.HttpServletResponse resp) throws java.io.IOException {
        try {
            String password = req.getParameter("password");
            userID = Integer.parseInt(req.getParameter("id"));
            adminService.updatePasswordByID(userID, password);
            resp.sendRedirect(req.getContextPath() + "/login.jsp");
        } catch (BizException e) {
            resp.sendRedirect(req.getContextPath() + "/login.jsp?error=" + URLEncoder.encode(e.getMessage(), "UTF-8"));
        }
    }

}

