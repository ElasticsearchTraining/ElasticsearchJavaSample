package org.sample.elastic.services.resources;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

@Api("Test")
@Path("/test")
public class HelloApi {

    private ObjectMapper mapper = new ObjectMapper();
    private Logger defaultLogger;

    public HelloApi(Logger defaultLogger) {
        this.defaultLogger = defaultLogger;
    }

    @GET
    @ApiOperation("Hello")
    @Path("/hello")
    public Response get() throws Exception {
        defaultLogger.info("Getting Hello");
        String json = mapper.writeValueAsString("Hello from ElasticSample!");
        return Response.ok(json).build();
    }

}