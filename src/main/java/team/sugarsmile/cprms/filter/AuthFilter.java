package team.sugarsmile.cprms.filter;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import team.sugarsmile.cprms.exception.BizException;
import team.sugarsmile.cprms.exception.ErrorCode;
import team.sugarsmile.cprms.model.Admin;
import team.sugarsmile.cprms.service.AdminService;
import team.sugarsmile.cprms.service.RoleService;

import java.io.IOException;
import java.util.Objects;

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
        BizException be = null;
        try {
            String url = request.getServletPath();
            Admin admin = (Admin) request.getSession().getAttribute("admin");
            if (url.endsWith("/login.jsp")) {
                if (admin != null) {
                    response.sendRedirect(request.getContextPath() + "/admin/home.jsp");
                    return;
                }
            } else {
                if (admin == null) {
                    throw new BizException(ErrorCode.ADMIN_NOT_LOGIN);
                }
                adminService.getAdminByID(admin.getId());
                if (!url.endsWith(".jsp") && !url.endsWith(".css") && !url.endsWith(".js")) {
                    roleService.checkPermission(url, admin);
                }
            }
        } catch (BizException e) {
            be = e;
        }
        if (be != null) {
            if (Objects.equals(be.getCode(), ErrorCode.ADMIN_NOT_EXIST.getCode())) {
                request.getSession().removeAttribute("admin");
            }
            request.setAttribute("error", ErrorCode.getByCode(be.getCode()).getMessage());
            RequestDispatcher rd;
            if (Objects.equals(be.getCode(), ErrorCode.ADMIN_NOT_EXIST.getCode()) || Objects.equals(be.getCode(), ErrorCode.ADMIN_NOT_LOGIN.getCode())) {
                rd = request.getRequestDispatcher("/admin/login.jsp");
            } else {
                rd = request.getRequestDispatcher("/admin/home.jsp");
            }
            rd.forward(request, response);
            throw be;
        } else {
            filterChain.doFilter(servletRequest, servletResponse);
        }
    }

    @Override
    public void destroy() {
        Filter.super.destroy();
    }
}

