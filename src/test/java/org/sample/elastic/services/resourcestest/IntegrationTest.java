package org.sample.elastic.services.resourcestest;

import org.sample.elastic.services.core.ElasticSampleConfiguration;
import org.sample.elastic.services.ElasticSampleApplication;
import io.dropwizard.testing.ResourceHelpers;
import io.dropwizard.testing.junit.DropwizardAppRule;
import org.glassfish.jersey.client.JerseyClientBuilder;
import org.junit.Rule;
import org.junit.Test;
import org.junit.Ignore;
import javax.ws.rs.client.Client;
import static org.assertj.core.api.Assertions.assertThat;
import javax.ws.rs.core.Response;

public class IntegrationTest {

	@Rule
    public final DropwizardAppRule<ElasticSampleConfiguration> RULE =
        new DropwizardAppRule<ElasticSampleConfiguration>(ElasticSampleApplication.class,
            ResourceHelpers.resourceFilePath("local.yaml"));

	@Ignore("Runs locally but not on travis-ci and codeship")  @Test
    public void runServerTest() {
        Client client = new JerseyClientBuilder().build();
        String result = client.target(
            String.format("http://localhost:%d/test/hello", RULE.getLocalPort())
        ).request().get(String.class);
        assertThat(result).isEqualTo("\"Hello from ElasticSample!\"");
    }			
}