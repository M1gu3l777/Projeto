package controller;

import java.io.IOException;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebFilter("/*")
public class AuthFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request,
                         ServletResponse response,
                         FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest req =
            (HttpServletRequest) request;

        HttpServletResponse resp =
            (HttpServletResponse) response;

        String path = req.getServletPath();

        boolean loggedIn =
            req.getSession().getAttribute("user_logged") != null;

        boolean loginPage =
            path.equals("/login")
            || path.equals("/logout")
            || path.equals("/login.jsp");

        boolean publicResource =
            path.startsWith("/css/")
            || path.startsWith("/js/")
            || path.startsWith("/images/")
            || path.startsWith("/fonts/")
            || path.equals("/favicon.ico");

        if (loggedIn || loginPage || publicResource) {
            chain.doFilter(request, response);
        } else {
            resp.sendRedirect(
                req.getContextPath() + "/login.jsp");
        }
    }
}
