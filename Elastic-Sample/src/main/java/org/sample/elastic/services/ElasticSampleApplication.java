package org.sample.elastic.services;

import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import io.federecio.dropwizard.swagger.SwaggerBundle;
import io.federecio.dropwizard.swagger.SwaggerBundleConfiguration;
import org.sample.elastic.services.resources.ElasticApi;
import org.sample.elastic.services.resources.HelloApi;
import org.sample.elastic.services.core.ElasticSampleConfiguration;
import org.sample.elastic.services.db.*;
import org.sample.elastic.services.health.ElasticHealth;
import org.slf4j.LoggerFactory;

public class ElasticSampleApplication extends Application<ElasticSampleConfiguration> {

    public static void main(String... args) throws Exception {
        new ElasticSampleApplication().run(args);
    }

    @Override
    public void initialize(Bootstrap<ElasticSampleConfiguration> bootstrap) {
        bootstrap.addBundle(new SwaggerBundle<ElasticSampleConfiguration>() {
            @Override
            protected SwaggerBundleConfiguration getSwaggerBundleConfiguration(ElasticSampleConfiguration elasticSampleConfiguration) {
                // this would be the preferred way to set up swagger, you can also construct the object here programtically if you want
                return elasticSampleConfiguration.swaggerBundleConfiguration;
            }
        });
    }

    @Override
    public void run(ElasticSampleConfiguration configuration, Environment environment) throws Exception {
        final org.slf4j.Logger esLogger = LoggerFactory.getLogger(ElasticApi.class);
        final org.slf4j.Logger defaultLogger = LoggerFactory.getLogger(HelloApi.class);

        ElasticSearch elasticSearch = new ElasticSearch();
        environment.lifecycle().manage(elasticSearch);

        environment.jersey().register(new HelloApi(defaultLogger));
        environment.jersey().register(new ElasticApi(elasticSearch, esLogger));

        environment.healthChecks().register("ElasticSearch", new ElasticHealth(elasticSearch));
     }

}
