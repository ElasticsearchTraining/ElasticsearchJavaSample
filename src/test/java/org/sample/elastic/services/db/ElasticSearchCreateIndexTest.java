package org.sample.elastic.services.db;

import junit.framework.TestCase;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.assertj.core.api.Assertions.assertThat;
import org.sample.elastic.services.core.ElasticSampleConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.Date;

public class ElasticSearchCreateIndexTest extends TestCase {

    private ElasticSampleConfiguration esConfig;
    private ElasticSearch esClient;
    private Logger defaultLogger;
    private String indexName;

    @Test
    public void testCreateIndex() throws Exception {
        defaultLogger = LoggerFactory.getLogger(ElasticSearch.class);
        esConfig = new ElasticSampleConfiguration();
        esConfig.setElasticsearchClientNodeName("javaclient");
        esConfig.setElasticsearchClientPort("10080");
        esConfig.setElasticsearchCluster("elasticsearch-local");
        esConfig.setElasticsearchHost("localhost[9300-9400]");
        esClient = new ElasticSearch(esConfig);
        esClient.start();
        indexName = "products" + new Date().getTime();
        boolean indexResult = esClient.createIndex(indexName,"english","product","{name:string}",defaultLogger);
        esClient.deleteIndex(indexName, defaultLogger);
        assertThat(indexResult).isEqualTo(true);
    }

}