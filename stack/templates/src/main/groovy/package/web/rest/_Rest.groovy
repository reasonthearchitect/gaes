package <%=packageName%>.web.rest

import com.codahale.metrics.annotation.Timed
import <%=packageNameGenerated%>.web.rest.util.HeaderUtil
import <%=packageName%>.domain.search.<%=entityClass%>
import <%=packageName%>.facade.I<%=entityClass%>Facade
import <%=packageNameGenerated%>.web.rest.util.PaginationUtil
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.http.HttpHeaders

import javax.inject.Inject
import java.net.URI
import java.net.URISyntaxException
import java.util.List
import java.util.Optional

@RestController
@RequestMapping("/api/v1")
public class <%=entityClass%>Rest {

	@Inject
	private I<%=entityClass%>Facade <%=entityInstance%>Facade;

	@RequestMapping(value = "/<%=entityInstance%>s",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<<%=entityClass%>> create<%=entityClass%>(@RequestBody <%=entityClass%> <%=entityInstance%>) throws URISyntaxException {
    	if (<%=entityInstance%>.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new <%=entityInstance%> cannot already have an ID").body(null);
        }
        <%=entityClass%> result = this.<%=entityInstance%>Facade.save(<%=entityInstance%>);
        return ResponseEntity.created(new URI("/api/v1/<%=entityInstance%>s/" + result.getId()))
                .headers(HeaderUtil.createEntityCreationAlert("<%=entityInstance%>", result.getId().toString()))
                .body(result);
    }

    @RequestMapping(value = "/<%=entityInstance%>s",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<<%=entityClass%>> update<%=entityClass%>(@RequestBody <%=entityClass%> <%=entityInstance%>) throws URISyntaxException {
        //log.debug("REST request to update <%=entityClass%> : {}", bird);
        if (<%=entityInstance%>.getId() == null) {
            return create<%=entityClass%>(<%=entityInstance%>);
        }
        <%=entityClass%> result = this.<%=entityInstance%>Facade.save(<%=entityInstance%>);
        return ResponseEntity.ok()
        		.headers(HeaderUtil.createEntityUpdateAlert("<%=entityInstance%>", result.getId().toString()))
                .body(result);
    }

    @RequestMapping(value = "/<%=entityInstance%>s/{id}",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> delete<%=entityClass%>(@PathVariable String id) {
        <%=entityClass%> <%=entityInstance%> = this.<%=entityInstance%>Facade.findOne(id)
        ResponseEntity result = null;
        if (<%=entityInstance%> == null) {
            result = ResponseEntity.noContent().header('failure', 'entity does not exist').body(null)
        } else {
            this.<%=entityInstance%>Facade.delete(<%=entityInstance%>);
            result = ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("<%=entityInstance%>", id.toString())).build();
        }
        return result
    }

    @RequestMapping(value = "/<%=entityInstance%>s/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<<%=entityClass%>> get<%=entityClass%>(@PathVariable String id) {
        <%=entityClass%> <%=entityInstance%> = <%=entityInstance%>Facade.findOne(id)
        ResponseEntity result = null;
        if (<%=entityInstance%> != null) {
            result = new ResponseEntity<>(<%=entityInstance%>, HttpStatus.OK)
        } else {
            result = new ResponseEntity<>(HttpStatus.NOT_FOUND)
        }
        return result
    }
    /**
     * GET  /<%=entityInstance%>s -> get all the <%=entityInstance%>s.
     */
    @RequestMapping(value = "/<%=entityInstance%>s",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<<%=entityClass%>>> getAll<%=entityClass%>s(Pageable pageable)
            throws URISyntaxException {
        Page<<%=entityClass%>> <%=entityInstance%>s = this.<%=entityInstance%>Facade.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(<%=entityInstance%>s, "/api/v1/<%=entityInstance%>s");
        return new ResponseEntity<>(<%=entityInstance%>s.getContent(), headers, HttpStatus.OK);
    }
}