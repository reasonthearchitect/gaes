package <%=packageName%>.test.facade

import <%=packageName%>.business.I<%=entityClass%>Business
import <%=packageName%>.domain.search.<%=entityClass%>
import <%=packageName%>.facade.I<%=entityClass%>Facade
import <%=packageName%>.facade.impl.<%=entityClass%>Facade
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest
import spock.lang.Specification

/**
 * Unit spec for the <%=entityClass%> facade.
 */
class <%=entityClass%>FacadeUnitSpec extends Specification {

    I<%=entityClass%>Facade <%=entityInstance%>Facade;

    def setup() {
        this.<%=entityInstance%>Facade = new <%=entityClass%>Facade();
    }

    def "simple unit test from the generation framework for the save function provided"() {

        setup:
        def <%=entityInstance%>Business = Mock(I<%=entityClass%>Business);
        this.<%=entityInstance%>Facade.<%=entityInstance%>Business = <%=entityInstance%>Business;
        def <%=entityInstance%> = [id: "1"]as <%=entityClass%>;

        when:
        def r<%=entityInstance%> = this.<%=entityInstance%>Facade.save(<%=entityInstance%>);

        then:
        1 * <%=entityInstance%>Business.save(_) >> <%=entityInstance%>;
    }

    def "simple unit test to find one"(){

        setup:
        def <%=entityInstance%>Business = Mock(I<%=entityClass%>Business);
        this.<%=entityInstance%>Facade.<%=entityInstance%>Business = <%=entityInstance%>Business;
        def r<%=entityInstance%> =[id: "1"]as <%=entityClass%>;

        when:
        def result = this.<%=entityInstance%>Facade.findOne("1");

        then:
        1 * <%=entityInstance%>Business.findOne(_) >> r<%=entityInstance%>;
        r<%=entityInstance%> != null;
    }

    def "simple unit test for deleting an entity"() {

        setup:
        def <%=entityInstance%>Business = Mock(I<%=entityClass%>Business);
        this.<%=entityInstance%>Facade.<%=entityInstance%>Business = <%=entityInstance%>Business;

        when:
        this.<%=entityInstance%>Facade.delete([] as <%=entityClass%>);

        then:
        1 * <%=entityInstance%>Business.delete(_);
    }

    def "simple unit test for finding all the objects with pagination"() {

        setup:
        def <%=entityInstance%>Business = Mock(I<%=entityClass%>Business);
        this.<%=entityInstance%>Facade.<%=entityInstance%>Business = <%=entityInstance%>Business;
        PageImpl<<%=entityClass%>> page = new PageImpl<<%=entityClass%>>([[id:"1"] as <%=entityClass%>, [id:"2"] as <%=entityClass%>],new PageRequest(2, 2), 12);

        when:
        Page<<%=entityClass%>> <%=entityInstance%>s = this.<%=entityInstance%>Facade.findAll(new PageRequest(1, 1))

        then:
        1 * <%=entityInstance%>Business.findAll(_) >> page;
        <%=entityInstance%>s.getContent().size() == 2;
    }
}
