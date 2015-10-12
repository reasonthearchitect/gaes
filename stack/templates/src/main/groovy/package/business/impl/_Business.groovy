package <%=packageName%>.business.impl;

import <%=packageName%>.business.I<%=entityClass%>Business;
import <%=packageName%>.domain.search.<%=entityClass%>;
import <%=packageName%>.repository.search.I<%=entityClass%>SearchRepository;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import org.springframework.beans.factory.annotation.Autowired;

@Service
class <%=entityClass%>Business implements I<%=entityClass%>Business {

	@Autowired(required = true)
	private I<%=entityClass%>SearchRepository <%=entityInstance%>Repository;

	@Override
	public <%=entityClass%> findOne(String id) {
		return this.<%=entityInstance%>Repository.findOne(id);
	}

	@Override
	public <%=entityClass%> save( <%=entityClass%> <%=entityInstance%>) {
		return this.<%=entityInstance%>Repository.save(<%=entityInstance%>);
	}

	@Override
	public Page<<%=entityClass%>> findAll(Pageable pageable) {
		return this.<%=entityInstance%>Repository.findAll(pageable);
	}

	@Override
	public void delete(<%=entityClass%> <%=entityInstance%>) {
		this.<%=entityInstance%>Repository.delete(<%=entityInstance%>);
	}
}