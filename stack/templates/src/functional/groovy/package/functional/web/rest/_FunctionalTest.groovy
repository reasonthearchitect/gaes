package <%=packageName%>.functional.web.rest


import com.jayway.restassured.RestAssured
import <%=packageName%>.functional.AbstractFunctionTest
import <%=packageNameGenerated%>.web.filter.CaosFilter
import <%=packageNameGenerated%>.web.filter.LifeTimeSessionAndRequestTokenFilter
import <%=packageNameGenerated%>.web.filter.RolesFilter

import static com.jayway.restassured.RestAssured.*
import org.apache.http.HttpStatus
import static org.hamcrest.Matchers.*
import org.junit.Before
import org.junit.Test
import org.springframework.beans.factory.annotation.Value


class <%=entityClass%>FunctionalTest extends AbstractFunctionTest {

    @Value('${local.server.port}')
    int port;

    @Before
    public void setUp() {
        RestAssured.port = this.port;
    }

    @Test
    public void retreive_<%=entityInstance%>s_list() {

        expect().
            headers(
                LifeTimeSessionAndRequestTokenFilter.X_LIFE_TIME_TOKEN, "LIFETIME_TEST_TOKEN",
                LifeTimeSessionAndRequestTokenFilter.X_SESSION_TOKEN, "SESSION_TEST_TOKEN",
                LifeTimeSessionAndRequestTokenFilter.X_REQUEST_TOKEN, "REQUEST_TEST_TOKEN"
            ).
            statusCode(HttpStatus.SC_OK).
        when().
            with().
                header(LifeTimeSessionAndRequestTokenFilter.X_LIFE_TIME_TOKEN, "LIFETIME_TEST_TOKEN").
                header(LifeTimeSessionAndRequestTokenFilter.X_SESSION_TOKEN, "SESSION_TEST_TOKEN").
                header(LifeTimeSessionAndRequestTokenFilter.X_REQUEST_TOKEN, "REQUEST_TEST_TOKEN" ).
            get('/api/v1/<%=entityInstance%>s')
    }

    @Test
    public void no_headers_passed_only_generated_request_header_returned_for_<%=entityInstance%>s_list() {
        expect().
            headers(
                    LifeTimeSessionAndRequestTokenFilter.X_LIFE_TIME_TOKEN, isEmptyOrNullString(),
                    LifeTimeSessionAndRequestTokenFilter.X_SESSION_TOKEN, isEmptyOrNullString(),
                    LifeTimeSessionAndRequestTokenFilter.X_REQUEST_TOKEN, anything()
            ).
            statusCode(HttpStatus.SC_OK).
        when().
            get('/api/v1/<%=entityInstance%>s')
    }

    @Test
    public void ensure_roles_are_carried_forward_for_<%=entityInstance%>s_list() {
        expect().
            headers(
                    RolesFilter.X_ROLES_HEADER, "ADMIN"
            ).
            statusCode(HttpStatus.SC_OK).
        when().
            with().
                header(RolesFilter.X_ROLES_HEADER, "ADMIN").
            get('/api/v1/<%=entityInstance%>s')
    }

    @Test
    public void ensure_no_roles_are_generated_for_<%=entityInstance%>s_list() {
        expect().
            headers(
                    RolesFilter.X_ROLES_HEADER, isEmptyOrNullString()
            ).
        statusCode(HttpStatus.SC_OK).
        when().
        get('/api/v1/<%=entityInstance%>s')
    }

    @Test
    public void check_the_caos_filter_causes_caos_on_this_uri() {
        String caosUri = "/api/v1/<%=entityInstance%>s";
        expect().
            headers(
                    CaosFilter.X_CAOS_URI, caosUri,
                    CaosFilter.X_CAOS_HTTP_STATUS_CODE, "" + HttpStatus.SC_INTERNAL_SERVER_ERROR,
                    CaosFilter.X_I_PERFORMED_CAOS, caosUri,

            ).
            statusCode(HttpStatus.SC_INTERNAL_SERVER_ERROR).
        when().
            with().
                header(CaosFilter.X_CAOS_URI, caosUri).
                header(CaosFilter.X_CAOS_HTTP_STATUS_CODE, HttpStatus.SC_INTERNAL_SERVER_ERROR).
        get('/api/v1/<%=entityInstance%>s')
    }

    @Test
    public void check_the_caos_filter_causes_caos_on_this_uri_with_default_404() {
        String caosUri = "/api/v1/<%=entityInstance%>s";
        expect().
                headers(
                        CaosFilter.X_CAOS_URI, caosUri,
                        CaosFilter.X_I_PERFORMED_CAOS, caosUri,
                ).
                statusCode(HttpStatus.SC_NOT_FOUND).
        when().
            with().
                header(CaosFilter.X_CAOS_URI, "/api/v1/<%=entityInstance%>s").
        get('/api/v1/<%=entityInstance%>s')
    }

    @Test
    public void check_the_caos_filter_causes_caos_on_this_uri_with_bad_status_defaults_to_404() {
        String caosUri = "/api/v1/<%=entityInstance%>s";
        expect().
                headers(
                        CaosFilter.X_CAOS_URI, caosUri,
                        CaosFilter.X_I_PERFORMED_CAOS, caosUri,
                ).
                statusCode(HttpStatus.SC_NOT_FOUND).
        when().
            with().
                header(CaosFilter.X_CAOS_URI, caosUri).
                header(CaosFilter.X_CAOS_HTTP_STATUS_CODE, "This is not a status code").
        get('/api/v1/<%=entityInstance%>s')
    }

    @Test
    public void check_the_caos_filter_causes_no_caos_on_this_uri_but_returns_headers() {
        String caosUri = "/api/v1/notapplicableForFuncitonalTesting";
        String getUri = "/api/v1/<%=entityInstance%>s";
        int caosStatusCode = HttpStatus.SC_FAILED_DEPENDENCY;
        expect().
                headers(
                        CaosFilter.X_CAOS_URI, caosUri,
                        CaosFilter.X_CAOS_HTTP_STATUS_CODE, "" + caosStatusCode,
                        CaosFilter.X_I_PERFORMED_CAOS, getUri
                ).
                statusCode(HttpStatus.SC_OK).
        when().
            with().
                header(CaosFilter.X_CAOS_URI, "/api/v1/notapplicableForFuncitonalTesting").
                header(CaosFilter.X_CAOS_HTTP_STATUS_CODE, caosStatusCode).
        get(getUri)
    }

    @Test
    public void check_the_caos_filter_appends_its_uri_to_performed_caos() {
        String previousCaosUri = "/api/v1/notapplicableForFuncitonalTesting";
        String getUri = "/api/v1/<%=entityInstance%>s";
        int caosStatusCode = HttpStatus.SC_FAILED_DEPENDENCY;
        expect().
            headers(
                CaosFilter.X_CAOS_URI, getUri,
                CaosFilter.X_CAOS_HTTP_STATUS_CODE, "" + caosStatusCode,
                CaosFilter.X_I_PERFORMED_CAOS, previousCaosUri + "::" + getUri
            ).
            statusCode(HttpStatus.SC_FAILED_DEPENDENCY).
        when().
            with().
                header(CaosFilter.X_CAOS_URI, getUri).
                header(CaosFilter.X_CAOS_HTTP_STATUS_CODE, caosStatusCode).
                header(CaosFilter.X_I_PERFORMED_CAOS, previousCaosUri).
        get(getUri)
    }
}


