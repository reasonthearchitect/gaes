package <%=packageName%>.functional.web.rest


import com.jayway.restassured.RestAssured
import <%=packageName%>.functional.AbstractFunctionTest
import <%=packageNameGenerated%>.web.filter.LifeTimeSessionAndRequestTokenFilter

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
    public void no_headers_passed_only_request_header_returned() {
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
}


