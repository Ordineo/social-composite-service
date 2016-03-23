package be.ordina.ordineo.filter;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * Created by gide on 23/03/16.
 */
public class LinkedInConnectionFilter implements Filter {

    public static final String ORGINAL_URL = "originalUrl";

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        //do nothing
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest)servletRequest;

        if ("POST".equals(request.getMethod())) {
            request.getSession().setAttribute(ORGINAL_URL, request.getHeader("Referer"));
        }

        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
        //do nothing
    }

}
