package <%=packageName%>.domain.search;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field
import org.springframework.data.elasticsearch.annotations.FieldIndex
import org.springframework.data.elasticsearch.annotations.FieldType
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
/**
 * A <%= entityClass %>.
 */
@Document(indexName = "CHANGE_ME", type="<%= entityInstance.toLowerCase() %>")
class <%= entityClass %> implements Serializable {

    @Id
    @Field(type = FieldType.String,
            index = FieldIndex.analyzed,
            searchAnalyzer = "standard",
            indexAnalyzer = "standard",
            store = true)
    String id

<% for (fieldId in fields) { %>
	@Field(<% if (fields[fieldId].fieldType == 'BigDecimal') { %>
    	type=FieldType.Double<% } %><% if (fields[fieldId].fieldType == 'DateTime') { %>
    	type=FieldType.Date<% } %><% if (fields[fieldId].fieldType == 'Boolean') { %>
    	type=FieldType.Boolean<% } %><% if (fields[fieldId].fieldType == 'String') { %>
    	type=FieldType.String<% } %>,
    	index = FieldIndex.analyzed,
        searchAnalyzer = "standard",
        indexAnalyzer = "standard",
        store = true)
    <%= fields[fieldId].fieldType %> <%= fields[fieldId].fieldName %>;
<% } %>
}
