package org.sample.elastic.services.resources;

import org.junit.Test;
import org.junit.Before;
import static org.assertj.core.api.Assertions.assertThat;
import javax.ws.rs.core.Response;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

public class HelloResourceTest {

	private HelloResource resource;
	private Logger defaultLogger;
	private Response response;

	@Before
    public void setup() {
		defaultLogger = LoggerFactory.getLogger(HelloResource.class);
        resource = new HelloResource(defaultLogger);
    }	

	@Test
    public void runSimpleTest() throws Exception {
        response = resource.get();
        assertThat(response.getEntity()).isEqualTo("\"Hello from ElasticSample!\"");
    }

}