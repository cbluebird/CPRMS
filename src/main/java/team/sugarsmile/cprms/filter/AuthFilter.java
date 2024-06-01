package team.sugarsmile.cprms.filter;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import team.sugarsmile.cprms.exception.BizException;
import team.sugarsmile.cprms.model.Admin;
import team.sugarsmile.cprms.service.AdminService;
import team.sugarsmile.cprms.service.RoleService;

import java.io.IOException;
import java.net.URLEncoder;

@Slf4j
@WebFilter("/admi/*")
public class AuthFilter implements Filter {

    private final RoleService roleService = new RoleService();
    private final AdminService adminService = new AdminService();

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        HttpSession session = request.getSession();
        try {
            Admin admin = (Admin) session.getAttribute("admin");
            String uri = request.getRequestURI();
            Admin adminCheck = adminService.getAdminByID(admin.getId());
            roleService.checkPermission(uri, adminCheck);
        } catch (BizException e) {
            response.sendRedirect("/login.jsp");
        }
    }

    @Override
    public void destroy() {

    }
}
