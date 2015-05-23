package org.sample.elastic.services.api;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeCreator;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import org.sample.elastic.services.db.ElasticSearch;
import org.slf4j.Logger;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import com.fasterxml.jackson.databind.*;

@Path("/elastic")
@Api("/elastic")
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
    @Path("/clientinfo")
    @ApiOperation("Get client information")
    public Response getClientInfo() throws Exception {
        esLogger.info("Getting client information");
        String json = mapper.writeValueAsString(elasticSearch.toString());
        return Response.ok(json).build();
    }

}