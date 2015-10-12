package <%=packageName%>.facade;

import <%=packageName%>.domain.search.<%=entityClass%>;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Interface for the <%=entityClass%> facade pattern.
 */
public interface I<%=entityClass%>Facade {

    <%=entityClass%> save(<%=entityClass%> <%=entityInstance%>);

    <%=entityClass%> findOne(String id);

    void delete(<%=entityClass%> <%=entityInstance%>);

    Page<<%=entityClass%>> findAll(Pageable pageable);
}