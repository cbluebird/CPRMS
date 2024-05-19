package team.sugarsmile.cprms.filter;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import team.sugarsmile.cprms.exception.BizException;

import java.io.IOException;

/**
 * @author XiMo
 */

@Slf4j
@WebFilter("/*")
public class GlobalExceptionFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        Filter.super.init(filterConfig);
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        try {
            filterChain.doFilter(servletRequest, servletResponse);
        } catch (BizException e) {
            log.warn(e.getMessage());
        } catch (Exception e) {
            log.error(e.getMessage(), e.getCause());
            ((HttpServletResponse) servletResponse).sendRedirect("500.jsp");
        }
    }

    @Override
    public void destroy() {
        Filter.super.destroy();
    }
}
