package org.sample.elastic.services;

import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import io.federecio.dropwizard.swagger.SwaggerBundle;
import io.federecio.dropwizard.swagger.SwaggerBundleConfiguration;
import org.sample.elastic.services.api.ElasticApi;
import org.sample.elastic.services.api.HelloApi;
import org.sample.elastic.services.core.ElasticSampleConfiguration;
import org.sample.elastic.services.db.*;
import org.slf4j.LoggerFactory;
import org.eclipse.jetty.servlets.CrossOriginFilter;
import javax.servlet.DispatcherType;
import javax.servlet.FilterRegistration.Dynamic;
import java.util.EnumSet;

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

        configureCors(environment);
        environment.jersey().register(new HelloApi(defaultLogger));
        environment.jersey().register(new ElasticApi(elasticSearch, esLogger));
     }

    private void configureCors(Environment environment) {
        Dynamic filter = environment.servlets().addFilter("CORS", CrossOriginFilter.class);
        filter.addMappingForUrlPatterns(EnumSet.allOf(DispatcherType.class), true, "/*");
        filter.setInitParameter(CrossOriginFilter.ALLOWED_METHODS_PARAM, "GET,PUT,POST,DELETE,OPTIONS");
        filter.setInitParameter(CrossOriginFilter.ALLOWED_ORIGINS_PARAM, "*");
        filter.setInitParameter(CrossOriginFilter.ACCESS_CONTROL_ALLOW_ORIGIN_HEADER, "*");
        filter.setInitParameter(CrossOriginFilter.PREFLIGHT_MAX_AGE_PARAM,"86400");
        filter.setInitParameter(CrossOriginFilter.ACCESS_CONTROL_ALLOW_HEADERS_HEADER, "Content-Type,Authorization,X-Requested-With,Content-Length,Accept,Origin");
        filter.setInitParameter(CrossOriginFilter.ALLOW_CREDENTIALS_PARAM, "true");
    }
}
