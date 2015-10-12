package <%=packageName%>.repository.search

import <%=packageName%>.domain.search.<%=entityClass%>
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository

/**
 * Spring Data ElasticSearch repository for the <%=entityClass%> entity.
 */
interface I<%=entityClass%>SearchRepository extends ElasticsearchRepository<<%=entityClass%>, String> {
}
