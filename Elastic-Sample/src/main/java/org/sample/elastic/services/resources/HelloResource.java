package org.sample.elastic.services.resources;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

@Path("")
@Produces(MediaType.APPLICATION_JSON + "; charset=UTF-8")
@Consumes(MediaType.APPLICATION_JSON + "; charset=UTF-8")
public class HelloResource {

    @GET
    @Path("/hello")
    public String getGreeting() {
        return "Hello from ElasticSample!";
    }

}