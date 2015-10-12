package <%=packageName%>.it.repository.search

import <%=packageName%>.it.AbstractItTest
import <%=packageName%>.domain.search.<%=entityClass%>
import <%=packageName%>.repository.search.I<%=entityClass%>SearchRepository

import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired

class <%=entityClass%>SearchRepositoryItSpec extends AbstractItTest {

    @Autowired I<%=entityClass%>SearchRepository <%=entityInstance%>SearchRepository

    @Test
    def "a very basic integration test to ensure that everything is wired together properly"() {

        when:
        def r<%=entityInstance%> = this.<%=entityInstance%>SearchRepository.save(new <%=entityClass%>())

        then:
        r<%=entityInstance%>.id != null
        this.<%=entityInstance%>SearchRepository.delete(r<%=entityInstance%>.id)
    }
}