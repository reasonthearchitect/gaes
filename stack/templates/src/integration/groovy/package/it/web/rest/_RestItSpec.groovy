package <%=packageName%>.it.web.rest

import <%=packageName%>.domain.search.<%=entityClass%>
import <%=packageName%>.it.AbstractItTest
import <%=packageName%>.repository.search.I<%=entityClass%>SearchRepository
import <%=packageName%>.web.rest.<%=entityClass%>Rest
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.PageRequest
import org.springframework.http.HttpStatus

/**
 * Generated
 */
class <%=entityClass%>RestItSpec extends AbstractItTest {

    @Autowired
    <%=entityClass%>Rest <%=entityInstance%>Rest

    @Autowired
    I<%=entityClass%>SearchRepository <%=entityInstance%>SearchRepository

    def setup() {
        this.<%=entityInstance%>SearchRepository.deleteAll()
    }

    @Test
    def "make sure I cannot post a record with an id"() {

        when:
        def response = this.<%=entityInstance%>Rest.create<%=entityClass%>(new <%=entityClass%>(id: "1"))

        then:
        response.getStatusCode() == HttpStatus.BAD_REQUEST
        response.getHeaders().get("Failure") != null
    }

    @Test
    def "make sure i can post a <%=entityInstance%> without an id"() {

        when:
        def response = this.<%=entityInstance%>Rest.create<%=entityClass%>(new <%=entityClass%>())

        then:
        response.getStatusCode() == HttpStatus.CREATED
        response.getBody().id != null
    }

    @Test
    def "make sure I cannot put a record with an id"() {

        when:
        def response = this.<%=entityInstance%>Rest.update<%=entityClass%>(new <%=entityClass%>(id: "1"))

        then:
        response.getStatusCode() == HttpStatus.OK
        response.getBody().id != null
    }

    @Test
    def "make sure i can put a <%=entityInstance%> without an id"() {

        when:
        def response = this.<%=entityInstance%>Rest.update<%=entityClass%>(new <%=entityClass%>())

        then:
        response.getStatusCode() == HttpStatus.CREATED
        response.getBody().id != null
    }

    @Test
    def "returns a no content status code and message on the header"() {

        when:
        def response = this.<%=entityInstance%>Rest.delete<%=entityClass%>("10000000")

        then:
        response.getStatusCode() == HttpStatus.NO_CONTENT
        response.getHeaders().get('failure') != null
    }

    @Test
    def "returns an ok status code when deleting a <%=entityInstance%> that exists"() {

        setup:
        def <%=entityInstance%> = this.<%=entityInstance%>SearchRepository.save(new <%=entityClass%>())

        when:
        def response = this.<%=entityInstance%>Rest.delete<%=entityClass%>(<%=entityInstance%>.id)

        then:
        <%=entityInstance%>.id
        response.getStatusCode() == HttpStatus.OK
        this.<%=entityInstance%>SearchRepository.findOne(<%=entityInstance%>.id) == null
    }

    @Test
    def "cannot find the entity with the id" () {

        when:
        def response = this.<%=entityInstance%>Rest.get<%=entityClass%>("doesNotExist")

        then:
        response.getStatusCode() == HttpStatus.NOT_FOUND
    }

    @Test
    def "finds the entity ith the id" () {

        setup:
        def r<%=entityInstance%> = this.<%=entityInstance%>SearchRepository.save(new <%=entityClass%>())

        when:
        def response = this.<%=entityInstance%>Rest.get<%=entityClass%>(r<%=entityInstance%>.id)

        then:
        response.getStatusCode() == HttpStatus.OK
    }

    @Test
    def "we should find no records but get a status of ok"() {

        when:
        def response = this.<%=entityInstance%>Rest.getAll<%=entityClass%>s(new PageRequest(0, 1))

        then:
        response.getStatusCode() == HttpStatus.OK
        response.getBody().size() == 0
        response.getHeaders().get('X-Total-Count').get(0)   == '0'
        response.getHeaders().get('LINK-LAST').get(0)       == '</api/v1/<%=entityInstance%>s?page=-1&size=1>'
        response.getHeaders().get('LINK-FIRST').get(0)      == '</api/v1/<%=entityInstance%>s?page=0&size=1>'
    }

    @Test
    def "we should find no records but get a status of ok and be redirected to the previous page"() {

        when:
        def response = this.<%=entityInstance%>Rest.getAll<%=entityClass%>s(new PageRequest(1, 1))

        then:
        response.getStatusCode() == HttpStatus.OK
        response.getBody().size() == 0
        response.getHeaders().get('X-Total-Count').get(0)   == '0'
        response.getHeaders().get('LINK-PREV').get(0)       == '</api/v1/<%=entityInstance%>s?page=0&size=1>'
        response.getHeaders().get('LINK-LAST').get(0)       == '</api/v1/<%=entityInstance%>s?page=-1&size=1>'
        response.getHeaders().get('LINK-FIRST').get(0)      == '</api/v1/<%=entityInstance%>s?page=0&size=1>'
    }

    @Test
    def "we should find three pages and all the headers starting with the second page"() {

        setup:
        this.<%=entityInstance%>SearchRepository.save(new <%=entityClass%>())
        this.<%=entityInstance%>SearchRepository.save(new <%=entityClass%>())
        this.<%=entityInstance%>SearchRepository.save(new <%=entityClass%>())

        when:
        def response = this.<%=entityInstance%>Rest.getAll<%=entityClass%>s(new PageRequest(1, 1))

        then:
        response.getStatusCode()    == HttpStatus.OK
        response.getBody().id       != null
        response.getHeaders().get('X-Total-Count').get(0)   == '3'
        response.getHeaders().get('LINK-PREV').get(0)       == '</api/v1/<%=entityInstance%>s?page=0&size=1>'
        response.getHeaders().get('LINK-NEXT').get(0)       == '</api/v1/<%=entityInstance%>s?page=2&size=1>'
        response.getHeaders().get('LINK-FIRST').get(0)      == '</api/v1/<%=entityInstance%>s?page=0&size=1>'
        response.getHeaders().get('LINK-LAST').get(0)      == '</api/v1/<%=entityInstance%>s?page=2&size=1>'
    }
}
