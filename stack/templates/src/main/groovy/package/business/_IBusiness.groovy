package <%=packageName%>.business;

import <%=packageName%>.domain.search.<%=entityClass%>;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

interface I<%=entityClass%>Business {

	<%=entityClass%> findOne(String id);

	<%=entityClass%> save(<%=entityClass%> <%=entityInstance%>);

	Page<<%=entityClass%>> findAll(Pageable pageable);

	void delete(<%=entityClass%> <%=entityInstance%>);
}