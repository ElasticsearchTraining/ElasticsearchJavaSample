package org.sample.elastic.services.resources;

import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import com.wordnik.swagger.util.Json;
import org.elasticsearch.search.SearchHit;
import org.json.JSONObject;
import org.sample.elastic.services.db.ElasticSearch;
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
public class ElasticApi {

    private ElasticSearch elasticSearch;
    private Logger esLogger;
    private ObjectMapper mapper;

    public ElasticApi(ElasticSearch elasticSearch, Logger esLogger) {
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
            notes = "Create an index in ElasticSearch. Provide a valid JSON in indexFields" +
                    "with all the fields required in indexTYpe.",
            response = ElasticSearch.class
    )
    public Response createIndex(
            @FormParam("indexName") @ApiParam(defaultValue = "") String indexName,
            @FormParam("indexAnalyzer") @ApiParam(defaultValue = "english") String indexAnalyzer,
            @FormParam("indexType") @ApiParam(defaultValue = "") String indexType,
            @FormParam("indexFields") @ApiParam(defaultValue = "") String indexFields
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
            @FormParam("indexName") @ApiParam(defaultValue = "") String indexName,
            @FormParam("indexType") @ApiParam(defaultValue = "") String indexType,
            @FormParam("document") @ApiParam(defaultValue = "") String document
    ) throws Exception {
        esLogger.info("Attempting to write a document : " + document);
        return Response.ok(elasticSearch.createDocument(indexName, indexType, document,
                esLogger)).build();
    }

    @GET
    @ApiOperation("Search for documents")
    @Path("/search/{indexName}/{indexType}/{term}")
    public Response search(@PathParam("indexName") String indexName,
                           @PathParam("indexType") String indexType,
                           @PathParam("term") String term) throws Exception {
        esLogger.info("Search");
        Map<String, Object> json = new HashMap<String, Object>();
        java.util.Iterator<SearchHit> hitIterator = elasticSearch.search(indexName, indexType, term, esLogger).getHits().iterator();
        while(hitIterator.hasNext()){
            SearchHit hit = hitIterator.next();
            json.put(hit.getId(),hit.getSource());
        }
        return Response.ok(json).build();
    }

}