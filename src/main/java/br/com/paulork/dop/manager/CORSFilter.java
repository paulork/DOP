package br.com.paulork.dop.manager;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Paulo R. Kraemer <paulork10@gmail.com>
 */
@WebFilter("/*")
public class CORSFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {}

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;

        String origin = req.getHeader("origin") != null ? req.getHeader("origin") : "*";
        res.addHeader("Access-Control-Allow-Origin", origin);
        res.addHeader("Access-Control-Allow-Methods", "POST, GET, PUT, UPDATE, DELETE, OPTIONS");
        res.addHeader("Access-Control-Allow-Credentials", "true");
        res.addHeader("Access-Control-Allow-Headers", "Authorization, origin, content-type, accept, x-requested-with, responseType");

        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {}
    
}
