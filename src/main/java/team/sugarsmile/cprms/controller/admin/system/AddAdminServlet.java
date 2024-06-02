package team.sugarsmile.cprms.controller.admin.system;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import team.sugarsmile.cprms.exception.BizException;
import team.sugarsmile.cprms.exception.ErrorCode;
import team.sugarsmile.cprms.model.Admin;
import team.sugarsmile.cprms.model.Audit;
import team.sugarsmile.cprms.service.AdminService;
import team.sugarsmile.cprms.service.AuditService;

import java.io.IOException;

@WebServlet("/admin/system/add")
public class AddAdminServlet extends HttpServlet {
    private final AdminService adminService =new AdminService();
    private final AuditService auditService = new AuditService();
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        BizException be = null;
        try {
            HttpSession session = request.getSession();
            Admin admin = (Admin) session.getAttribute("admin");
            String name = request.getParameter("name");
            String phone = request.getParameter("phone");
            String userName = request.getParameter("userName");
            Admin.AdminType type= Admin.AdminType.getType(Integer.parseInt(request.getParameter("type")));
            if (name.isEmpty()||userName.isEmpty()||phone.isEmpty()) {
                throw new BizException(ErrorCode.PARAM_ERROR.getCode(), "参数不能为空");
            }
            adminService.addAdmin(type,phone,name,userName,0);
            auditService.createAudit("添加管理员", Audit.AuditType.ADD,admin.getId());
        } catch (BizException e) {
            be = e;
        } catch (IllegalArgumentException e) {
            be = new BizException(ErrorCode.PARAM_ERROR.getCode(), e.getMessage());
        }
        if (be != null) {
            request.getSession().setAttribute("error", ErrorCode.getByCode(be.getCode()).getMessage());
            response.sendRedirect(request.getContextPath() + "/admin/system/list?pageNum=1&pageSize=10");
            throw be;
        } else {
            response.sendRedirect(request.getContextPath() + "/admin/system/list?pageNum=1&pageSize=10");
        }
    }

}
