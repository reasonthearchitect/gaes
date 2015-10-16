package <%=packageNameGenerated%>.web.filter;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * This filter simply copies the roles for the request so that down stream services also have access to the roles.
 */
public class RolesFilter implements Filter {

    public static String X_ROLES_HEADER = "X-ROLES";

    @Override
    public void doFilter(ServletRequest req, ServletResponse res,
                         FilterChain chain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest)req;
        HttpServletResponse response = (HttpServletResponse) res;
        String rolesHeader  = request.getHeader(X_ROLES_HEADER);
        if (rolesHeader != null ) {
            response.setHeader(X_ROLES_HEADER, rolesHeader);
        }
        chain.doFilter(req, res);
    }

    @Override
    public void destroy() {}

    @Override
    public void init(FilterConfig arg0) throws ServletException {}
}
