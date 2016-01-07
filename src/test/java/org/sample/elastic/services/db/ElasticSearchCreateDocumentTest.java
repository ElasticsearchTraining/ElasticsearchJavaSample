package org.sample.elastic.services.db;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.sample.elastic.services.core.ElasticSampleConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.Date;
import static org.assertj.core.api.Assertions.assertThat;

public class ElasticSearchCreateDocumentTest {

    private ElasticSampleConfiguration esConfig;
    private ElasticSearch esClient;
    private Logger defaultLogger;
    private String indexName;

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
        indexName = "products" + new Date().getTime();
    }

    @After
    public void tearDown() throws Exception {
        esClient.deleteIndex(indexName, defaultLogger);
        esClient.stop();
        esClient = null;
    }


    @Test
    public void testCreateDocument() throws Exception {
       boolean documentResult = esClient.createDocument(indexName,"product","{name:\"sony\"}",defaultLogger);
        assertThat(documentResult).isEqualTo(true);
    }
}