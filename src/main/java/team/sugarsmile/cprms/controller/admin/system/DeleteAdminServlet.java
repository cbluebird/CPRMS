package team.sugarsmile.cprms.controller.admin.system;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import team.sugarsmile.cprms.exception.BizException;
import team.sugarsmile.cprms.exception.ErrorCode;
import team.sugarsmile.cprms.service.AdminService;

import java.io.IOException;
@WebServlet("/admin/system/delete")
public class DeleteAdminServlet extends HttpServlet {
    final private AdminService adminService=new AdminService();
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        BizException be = null;
        try {
            int id = Integer.parseInt(request.getParameter("id"));
            adminService.deleteAdminByID(id);
        } catch (BizException e) {
            be = e;
        } catch (NumberFormatException e) {
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

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
}
