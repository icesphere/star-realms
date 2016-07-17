package org.smartreaction.starrealms.view;

import javax.faces.application.ResourceHandler;
import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebFilter(filterName = "LoginFilter", urlPatterns = { "*.xhtml" })
public class LoginFilter implements Filter {

    public LoginFilter() {
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response,
                         FilterChain chain) throws IOException, ServletException {
        try {
            HttpServletRequest servletRequest = (HttpServletRequest) request;
            HttpServletResponse servletResponse = (HttpServletResponse) response;

            UserSession userSession = (UserSession) servletRequest.getSession().getAttribute("userSession");

            if (userSession != null && userSession.getUser() != null) {
                userSession.getUser().updateLastActivity();
            }

            String requestURI = servletRequest.getRequestURI();
            if (requestURI.contains("/login.xhtml")
                    || (userSession != null && userSession.getUser() != null)
                    || requestURI.contains(ResourceHandler.RESOURCE_IDENTIFIER)) {
                chain.doFilter(request, response);
            } else {
                servletResponse.sendRedirect(servletRequest.getContextPath() + "/login.xhtml");
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public void destroy() {

    }
}
