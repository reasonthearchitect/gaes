package <%=packageName%>.repository.search

import <%=packageName%>.domain.<%=entityClass%>
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository

/**
 * Spring Data ElasticSearch repository for the <%=entityClass%> entity.
 */
interface <%=entityClass%>SearchRepository extends ElasticsearchRepository<<%=entityClass%>, String> {
}
