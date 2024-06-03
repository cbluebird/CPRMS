package team.sugarsmile.cprms.filter;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import team.sugarsmile.cprms.exception.BizException;
import team.sugarsmile.cprms.model.Admin;
import team.sugarsmile.cprms.service.AdminService;
import team.sugarsmile.cprms.service.RoleService;

import java.io.IOException;

public class AuthFilter implements Filter {
    private final RoleService roleService = new RoleService();
    private final AdminService adminService = new AdminService();

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        Filter.super.init(filterConfig);
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        try {
            Admin admin = (Admin) request.getSession().getAttribute("admin");
            String path = request.getServletPath();
            adminService.getAdminByID(admin.getId());
            roleService.checkPermission(path, admin);
            filterChain.doFilter(servletRequest, servletResponse);
        } catch (BizException e) {
            response.sendRedirect(request.getContextPath() + "/login.jsp");
        }
    }

    @Override
    public void destroy() {
        Filter.super.destroy();
    }
}

