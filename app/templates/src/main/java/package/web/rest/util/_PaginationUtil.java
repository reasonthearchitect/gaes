package <%=packageNameGenerated%>.web.rest.util;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;

import java.net.URI;
import java.net.URISyntaxException;

/**
 * Utility class for handling pagination.
 *
 * <p>
 * Pagination uses the same principles as the <a href="https://developer.github.com/v3/#pagination">Github API</api>,
 * and follow <a href="http://tools.ietf.org/html/rfc5988">RFC 5988 (Link header)</a>.
 * </p>
 */
public class PaginationUtil {

    public static HttpHeaders generatePaginationHttpHeaders(Page<?> page, String baseUrl)
        throws URISyntaxException {

        HttpHeaders headers = new HttpHeaders();
        headers.add("X-Total-Count", "" + page.getTotalElements());
        String link = "";
        if ((page.getNumber() + 1) < page.getTotalPages()) {
            headers.add("LINK-NEXT", "<" + (new URI(baseUrl + "?page=" + (page.getNumber() + 1) + "&size=" + page.getSize())).toString() + ">");
        }
        // prev link
        if ((page.getNumber()) > 0) {
            headers.add("LINK-PREV", "<" + (new URI(baseUrl +"?page=" + (page.getNumber() - 1) + "&size=" + page.getSize())).toString() + ">");
        }
        // last and first link
        headers.add("LINK-LAST", "<" + (new URI(baseUrl +"?page=" + (page.getTotalPages() - 1) + "&size=" + page.getSize())).toString() + ">");
        headers.add("LINK-FIRST", "<" + (new URI(baseUrl +"?page=" + 0 + "&size=" + page.getSize()).toString()) + ">");
        return headers;
    }
}
