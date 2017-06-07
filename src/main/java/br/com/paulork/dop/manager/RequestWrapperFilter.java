package br.com.paulork.dop.manager;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

/**
 * @author Paulo R. Kraemer <paulork10@gmail.com>
 */
public class RequestWrapperFilter implements Filter {

    public void init(FilterConfig config) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws java.io.IOException, ServletException {
        HttpServletRequest hsr = (HttpServletRequest) request;
        if (hsr.getRequestURI().contains("upload-file")) {
            chain.doFilter(hsr, response);
        } else {
            MultiReadHttpServletRequest wrappedRequest = new MultiReadHttpServletRequest(hsr);
            chain.doFilter(wrappedRequest, response);
        }
    }

    public void destroy() {
    }
}
