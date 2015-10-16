package <%=packageNameGenerated%>.web.filter;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class CaosFilter implements Filter {

    public static String X_CAOS_URI = "X_CAOS_URI";

    public static String X_CAOS_HTTP_STATUS_CODE = "X_CAOS_HTTP_STATUS_CODE";

    public static String X_I_PERFORMED_CAOS = "X_I_PERFORMED_CAOS";

    @Override
    public void doFilter(ServletRequest req, ServletResponse res,
                         FilterChain chain) throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest)req;
        HttpServletResponse response = (HttpServletResponse) res;

        if (request.getHeader(X_CAOS_URI) != null) {
            addMeToTheCaos(request, response);
            copyCaosHeaders(request, response);
            if (doWeDoCaos(request)) {
                int statusCode = getHttpCaosStatusCode(request);
                response.setStatus(statusCode);
            } else {
                response.setStatus(200);
            }
        } else {
            chain.doFilter(req, res);
        }
    }

    private void addMeToTheCaos(final HttpServletRequest request, final HttpServletResponse response) {
        if(request.getHeader(X_I_PERFORMED_CAOS) == null) {
            response.setHeader(X_I_PERFORMED_CAOS, request.getRequestURI());
        } else {
            response.setHeader(X_I_PERFORMED_CAOS, request.getHeader(X_I_PERFORMED_CAOS) + "::" + request.getRequestURI());
        }
    }

    private void copyCaosHeaders(final HttpServletRequest request, final HttpServletResponse response) {
        response.setHeader(X_CAOS_URI, request.getHeader(X_CAOS_URI));
        String requestStatusCode = request.getHeader(X_CAOS_HTTP_STATUS_CODE);
        if (requestStatusCode != null) {
            response.setHeader(X_CAOS_HTTP_STATUS_CODE, request.getHeader(X_CAOS_HTTP_STATUS_CODE));
        }
    }

    private int getHttpCaosStatusCode(final HttpServletRequest request) {
        int rStatusCode = 404;
        String requestStatusCode = request.getHeader(X_CAOS_HTTP_STATUS_CODE);
        if (requestStatusCode != null ) {
            try {
                rStatusCode = Integer.parseInt(requestStatusCode);
            } catch (RuntimeException t) {
                //t.printStackTrace();
                //do nothing and return the default 404
            }
        }
        return rStatusCode;
    }

    private boolean doWeDoCaos(final HttpServletRequest request) {
        boolean caosEnsues = false;
        String caosUri = request.getHeader(X_CAOS_URI);
        if (caosUri != null && caosUri.equals(request.getRequestURI())) {
            caosEnsues = true;
        }
        return caosEnsues;
    }

    @Override
    public void destroy() {}

    @Override
    public void init(FilterConfig arg0) throws ServletException {}

}
