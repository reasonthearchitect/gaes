package <%=packageName%>.facade.impl;

import <%=packageName%>.business.I<%=entityClass%>Business;
import <%=packageName%>.domain.search.<%=entityClass%>;
import <%=packageName%>.facade.I<%=entityClass%>Facade;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;

/**
 * Facade code for the <%=entityClass%>.
 */
@Service
public class <%=entityClass%>Facade implements I<%=entityClass%>Facade {

    //@Inject private <%=entityClass%>Mapper <%=entityInstance%>Mapper;

    @Inject
    private I<%=entityClass%>Business <%=entityInstance%>Business;

    @Override
    @Transactional
    public <%=entityClass%> save(<%=entityClass%> <%=entityInstance%>) {
        return this.<%=entityInstance%>Business.save(<%=entityInstance%>);
    }

    @Override
    @Transactional(readOnly = true)
    public <%=entityClass%> findOne(String id){
       return this.<%=entityInstance%>Business.findOne(id);
    }

    @Override
    @Transactional
    public void delete(<%=entityClass%> <%=entityInstance%>) {
    	this.<%=entityInstance%>Business.delete(<%=entityInstance%>);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<<%=entityClass%>> findAll(Pageable pageable) {
    	return this.<%=entityInstance%>Business.findAll(pageable);
    }
}
