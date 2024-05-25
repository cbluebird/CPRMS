package team.sugarsmile.cprms.controller.admin;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import team.sugarsmile.cprms.exception.BizException;
import team.sugarsmile.cprms.exception.ErrorCode;
import team.sugarsmile.cprms.model.Admin;
import team.sugarsmile.cprms.service.AdminService;
import team.sugarsmile.cprms.util.JWT;

import java.net.URLEncoder;
import java.sql.Date;
import java.util.Calendar;
import java.util.GregorianCalendar;

@WebServlet("/auth/login")
public class LoginServlet extends HttpServlet {
    private final AdminService adminService = new AdminService();

    @Override
    protected void doGet(jakarta.servlet.http.HttpServletRequest req, jakarta.servlet.http.HttpServletResponse resp) throws java.io.IOException {
        resp.sendRedirect("/login.jsp");
    }

    @Override
    protected void doPost(jakarta.servlet.http.HttpServletRequest req, jakarta.servlet.http.HttpServletResponse resp) throws java.io.IOException, ServletException {
        String username = req.getParameter("username");
        String password = req.getParameter("password");
        Admin admin= Admin.builder().build();
                try {
                    admin = adminService.checkAdminByPassword(username,password);

                    Calendar calendar =new GregorianCalendar();
                    calendar.setTime(admin.getDate());
                    calendar.add(calendar.DATE, 90);
                    java.util.Date utilDate = (java.util.Date)calendar.getTime();
                    Date outDate =new Date(utilDate.getTime());

                    Date nowDate=new Date(new java.util.Date().getTime());

                    if(admin.getPassword().equals("zjut123456")||outDate.before(nowDate)){
                        resp.sendRedirect("/resetPassword.jsp?id="+URLEncoder.encode(admin.getId().toString(),"UTF-8"));
                        return ;
                    }

                    if (admin != null) {
                        req.getSession().setAttribute("user_id", String.valueOf(admin.getId()));
                        if (admin.getAdminType() == Admin.AdminType.SYSTEM) {
                            resp.sendRedirect("/system.jsp");
                        } else if (admin.getAdminType() == Admin.AdminType.SCHOOL) {
                            resp.sendRedirect("/school.jsp");
                        } else if (admin.getAdminType() == Admin.AdminType.DEPARTMENT) {
                            resp.sendRedirect("/admin/department/list?pageNum=1&pageSize=10");
                        } else if (admin.getAdminType() == Admin.AdminType.AUDIT) {
                            resp.sendRedirect("/audit.jsp");
                        } else {
                            resp.sendRedirect("/login.jsp");
                        }
                    } else {
                        resp.sendRedirect("/login.jsp");
                    }
                } catch (BizException e) {
                    req.setAttribute("error", ErrorCode.getByCode(e.getCode()).getMessage());
                    req.getRequestDispatcher("/login.jsp").forward(req, resp);
                    throw e;
                }
        }

}

