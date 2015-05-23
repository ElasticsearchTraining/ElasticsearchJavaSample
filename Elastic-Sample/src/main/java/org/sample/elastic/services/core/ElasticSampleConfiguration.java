package org.sample.elastic.services.core;

import io.dropwizard.Configuration;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.federecio.dropwizard.swagger.SwaggerBundleConfiguration;
import org.hibernate.validator.constraints.*;

public class ElasticSampleConfiguration extends Configuration {

    @NotEmpty
    @JsonProperty("defaultname")
    public String defaultName;

    @JsonProperty("swagger")
    public SwaggerBundleConfiguration swaggerBundleConfiguration;

    public String getDefaultName() {
        return defaultName;
    }
}
