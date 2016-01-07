package org.sample.elastic.services.resources;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.sample.elastic.services.core.ElasticSampleConfiguration;
import org.sample.elastic.services.db.ElasticSearch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.core.Response;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.*;

public class ElasticResourceTest {

    private ElasticSampleConfiguration esConfig;
    private ElasticResource esResource;
    private ElasticSearch esClient;
    private Logger defaultLogger;

    @Before
    public void setUp() throws Exception {
        defaultLogger = LoggerFactory.getLogger(ElasticSearch.class);
        esConfig = new ElasticSampleConfiguration();
        esConfig.setElasticsearchClientNodeName("testjavaclient");
        esConfig.setElasticsearchClientPort("10080");
        esConfig.setElasticsearchCluster("elasticsearch-local");
        esConfig.setElasticsearchHost("localhost[9300-9400]");
        esClient = new ElasticSearch(esConfig);
        esClient.start();
        esResource = new ElasticResource(esClient, defaultLogger);
    }

    @After
    public void tearDown() throws Exception {
        esClient.stop();
        esClient = null;
    }

    @Test
    public void testGetClientInfo() throws Exception {
        Response response = esResource.getClientInfo();
        assertThat(response.getEntity().toString().substring(0,45)).isEqualTo("\"org.sample.elastic.services.db.ElasticSearch");
    }

}