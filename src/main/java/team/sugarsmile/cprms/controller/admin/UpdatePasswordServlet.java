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

import java.io.IOException;

@WebServlet("/admin/password/update")
public class UpdatePasswordServlet extends HttpServlet {
    private final AdminService adminService = new AdminService();
    private final AuditService auditService = new AuditService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        BizException be = null;
        try {
            Admin admin = (Admin) request.getSession().getAttribute("admin");
            String password = request.getParameter("password");
            adminService.updatePasswordByID(admin.getId(), password);
            Boolean updatePassword = (Boolean) request.getSession().getAttribute("updatePassword");
            if (updatePassword != null && updatePassword) {
                request.getSession().removeAttribute("updatePassword");
            }
            auditService.createAudit("更新密码", Audit.AuditType.UPDATE, admin.getId());
        } catch (BizException e) {
            be = e;
        }
        if (be != null) {
            request.setAttribute("error", ErrorCode.getByCode(be.getCode()).getMessage());
            request.getRequestDispatcher("/admin/updatePassword.jsp");
        } else {
            response.sendRedirect(request.getContextPath() + "/admin/home.jsp");
        }
    }
}

