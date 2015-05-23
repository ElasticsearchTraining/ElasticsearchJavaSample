package org.sample.elastic.services.resources;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
//import com.wordnik.swagger.annotations.ApiParam;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
//import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;
//import javax.ws.rs.PathParam;
//import javax.ws.rs.QueryParam;
//import javax.ws.rs.core.MediaType;

@Path("/test")
@Api("/test")
public class HelloResource {

    private ObjectMapper mapper = new ObjectMapper();

    @GET
    @ApiOperation("Hello")
    @Path("/hello")
    public Response get() throws Exception {
        String json = mapper.writeValueAsString("Hello from ElasticSample!");
        return Response.ok(json).build();
    }

}