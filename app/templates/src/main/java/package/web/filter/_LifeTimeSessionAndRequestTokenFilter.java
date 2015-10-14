package <%=packageNameGenerated%>.web.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class LifeTimeSessionAndRequestTokenFilter implements Filter {

    public static final String X_LIFE_TIME_TOKEN  = "X-Lifetime-Token";
    public static final String X_SESSION_TOKEN    = "X-Session-Token";
    public static final String X_REQUEST_TOKEN    = "X-Request-Token";

    public static final String NO_LIFETIME_TOKEN  = "NO_LIFETIME_TOKEN";
    public static final String NO_SESSION_TOKEN   = "NO_SESSION_TOKEN";

    @Override
    public void doFilter(ServletRequest req, ServletResponse res,
                         FilterChain chain) throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest)req;
        HttpServletResponse response = (HttpServletResponse) res;

        this.doLifeTimeToken(request, response);
        this.doSessionToken(request, response);
        this.doRequestToken(request, response);
        chain.doFilter(req, res);
    }

    private void doLifeTimeToken(final HttpServletRequest request, final HttpServletResponse response) {
        String lifetimeToken = request.getHeader(X_LIFE_TIME_TOKEN);
        response.setHeader(X_LIFE_TIME_TOKEN, ( lifetimeToken != null) ? lifetimeToken : NO_LIFETIME_TOKEN);
    }

    private void doSessionToken(final HttpServletRequest request, final HttpServletResponse response) {
        String sessionToken = request.getHeader(X_SESSION_TOKEN);
        response.setHeader(X_SESSION_TOKEN, ( sessionToken != null) ? sessionToken : NO_SESSION_TOKEN);
    }

    private void doRequestToken(final HttpServletRequest request, final HttpServletResponse response) {
        String requestToken = request.getHeader(X_REQUEST_TOKEN);
        response.setHeader(X_REQUEST_TOKEN, ( requestToken != null) ? requestToken : java.util.UUID.randomUUID().toString());
    }

    @Override
    public void destroy() {}

    @Override
    public void init(FilterConfig arg0) throws ServletException {}
}
