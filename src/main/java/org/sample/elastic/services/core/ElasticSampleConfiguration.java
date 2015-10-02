package org.sample.elastic.services.core;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.dropwizard.Configuration;
import io.federecio.dropwizard.swagger.SwaggerBundleConfiguration;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

public class ElasticSampleConfiguration extends Configuration{

    @JsonProperty("swagger")
    public SwaggerBundleConfiguration swaggerBundleConfiguration;

    @Valid
    @NotNull
    @JsonProperty("elasticsearch.host")
    private String elasticsearchHost;

    public String getElasticsearchHost() {
        return elasticsearchHost;
    }

    public void setElasticsearchHost(String elasticsearchHost) {
        this.elasticsearchHost = elasticsearchHost;
    }

    @Valid
    @NotNull
    @JsonProperty("elasticsearch.cluster")
    private String elasticsearchCluster;

    public String getElasticsearchCluster() {
        return elasticsearchCluster;
    }

    public void setElasticsearchCluster(String elasticsearchCluster) {
        this.elasticsearchCluster = elasticsearchCluster;
    }

    @Valid
    @NotNull
    @JsonProperty("elasticsearch.clientnode")
    private String elasticsearchClientNodeName;

    public String getElasticsearchClientNodeName() {
        return elasticsearchClientNodeName;
    }

    public void setElasticsearchClientNodeName(String elasticsearchClientNodeName) {
        this.elasticsearchClientNodeName = elasticsearchClientNodeName;
    }

    @Valid
    @NotNull
    @JsonProperty("elasticsearch.clientport")
    private String elasticsearchClientPort;

    public String getElasticsearchClientPort() {
        return elasticsearchClientPort;
    }

    public void setElasticsearchClientPort(String elasticsearchClientPort) {
        this.elasticsearchClientPort = elasticsearchClientPort;
    }

}
