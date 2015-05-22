package org.sample.elastic.services.resources;

import org.sample.elastic.services.db.ElasticSearch;
import org.slf4j.Logger;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/elastic")
@Produces(MediaType.APPLICATION_JSON + "; charset=UTF-8")
@Consumes(MediaType.APPLICATION_JSON + "; charset=UTF-8")
public class ElasticResource {

    private ElasticSearch elasticSearch;
    private Logger esLogger;

    public ElasticResource(ElasticSearch elasticSearch, Logger esLogger) {

        this.elasticSearch = elasticSearch;
        this.esLogger = esLogger;

    }

    @GET
    @Path("/clientinfo")
    public String getClientInfo() {

        esLogger.info("Getting client information");
        return elasticSearch.toString();

    }

}