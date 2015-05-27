package org.sample.elastic.services.resources;

import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import org.elasticsearch.search.SearchHit;
import org.sample.elastic.services.db.ElasticSearch;
import org.sample.elastic.services.models.SearchResult;
import org.slf4j.Logger;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import com.fasterxml.jackson.databind.*;

import java.util.HashMap;
import java.util.Map;

@Api("Elastic")
@Path("/elastic")
@Produces(MediaType.APPLICATION_JSON + "; charset=UTF-8")
@Consumes(MediaType.APPLICATION_JSON + "; charset=UTF-8")
public class ElasticResource {

    private ElasticSearch elasticSearch;
    private Logger esLogger;
    private ObjectMapper mapper;

    public ElasticResource(ElasticSearch elasticSearch, Logger esLogger) {
        this.elasticSearch = elasticSearch;
        this.esLogger = esLogger;
        this.mapper = new ObjectMapper();
    }

    @GET
    @ApiOperation("Get client information")
    @Path("/clientinfo")
    public Response getClientInfo() throws Exception {
        esLogger.info("Getting client information");
        String json = mapper.writeValueAsString(elasticSearch.toString());
        return Response.ok(json).build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Path("/create/index")
    @ApiOperation(
            value = "Create an index",
            notes = "Create an index in ElasticSearch. Provide a valid JSON in Document Fields" +
                    "with all the fields required for Document Type.",
            response = ElasticSearch.class
    )
    public Response createIndex(
            @FormParam("Index Name") @ApiParam(defaultValue = "") String indexName,
            @FormParam("Index Analyzer") @ApiParam(defaultValue = "english") String indexAnalyzer,
            @FormParam("Document Type") @ApiParam(defaultValue = "") String indexType,
            @FormParam("Document Fields") @ApiParam(defaultValue = "") String indexFields
    ) throws Exception {
        esLogger.info("Attempting to create an index: " + indexName);
        return Response.ok(elasticSearch.createIndex(indexName, indexAnalyzer, indexType,
                indexFields,esLogger)).build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Path("/create/document")
    @ApiOperation(
            value = "Create a document",
            notes = "Create a document in the index. Provide a valid JSON in document.",
            response = ElasticSearch.class
    )
    public Response createDocument(
            @FormParam("Index Name") @ApiParam(defaultValue = "") String indexName,
            @FormParam("Document Type") @ApiParam(defaultValue = "") String indexType,
            @FormParam("Document") @ApiParam(defaultValue = "") String document
    ) throws Exception {
        esLogger.info("Attempting to write a document : " + document);
        return Response.ok(elasticSearch.createDocument(indexName, indexType, document,
                esLogger)).build();
    }

    @GET
    @ApiOperation(
            value = "Search for documents",
            notes = "Ensure that the index and documents are already created before searching.",
            response = SearchResult.class
            )
    @Path("/search/{IndexName}/{DocumentType}/{SearchTerm}")
    public Response search(@PathParam("IndexName") String indexName,
                           @PathParam("DocumentType") String indexType,
                           @PathParam("SearchTerm") String term,
                           @QueryParam("StartPage") @ApiParam(defaultValue = "0") int startPage,
                           @QueryParam("PageSize") @ApiParam(defaultValue = "30") int pageSize,
                           @QueryParam("FilterField") @ApiParam(defaultValue = "") String filterField,
                           @QueryParam("StartFilter") @ApiParam(defaultValue = "") float startFilter,
                           @QueryParam("EndFilter") @ApiParam(defaultValue = "") float endFilter) throws Exception {

        esLogger.info("Calling Search with term : " + term);
        Map<String, Object> json = new HashMap<String, Object>();
        java.util.Iterator<SearchHit> hitIterator = elasticSearch.search(indexName, indexType, term,
                startPage, pageSize, filterField, startFilter, endFilter, esLogger).getHits().iterator();
        while(hitIterator.hasNext()){
            SearchHit hit = hitIterator.next();
            SearchResult searchResult = new SearchResult(hit.getId(),hit.getScore(),hit.getSourceAsString());
            json.put(hit.getId(), searchResult);
        }
        return Response.ok(json).build();
    }

}