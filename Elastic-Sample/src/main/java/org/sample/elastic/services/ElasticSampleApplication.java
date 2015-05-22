package org.sample.elastic.services;

import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import org.sample.elastic.services.resources.*;
import org.sample.elastic.services.db.*;
import org.slf4j.LoggerFactory;

public class ElasticSampleApplication extends Application<ElasticSampleConfiguration> {

    public static void main(final String[] args) throws Exception {
        new ElasticSampleApplication().run(args);
    }

    @Override
    public String getName() {
        return "ElasticSample";
    }

    @Override
    public void initialize(final Bootstrap<ElasticSampleConfiguration> bootstrap) {

    }

    @Override
    public void run(final ElasticSampleConfiguration configuration,
                    final Environment environment) {

        final org.slf4j.Logger esLogger = LoggerFactory.getLogger(ElasticResource.class);

        ElasticSearch elasticSearch = new ElasticSearch();

        environment.lifecycle().manage(elasticSearch);
        environment.jersey().register(new HelloResource());
        environment.jersey().register(new ElasticResource(elasticSearch, esLogger));

    }

}
