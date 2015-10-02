package org.sample.elastic.services.health;

import org.junit.Test;
import org.sample.elastic.services.core.ElasticSampleConfiguration;
import org.sample.elastic.services.db.ElasticSearch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.Date;
import static org.assertj.core.api.Assertions.assertThat;
import com.codahale.metrics.health.HealthCheck;

public class ElasticHealthTest {

    private ElasticSampleConfiguration esConfig;
    private ElasticSearch esClient;
    private Logger defaultLogger;
    private String indexName;
    private ElasticHealth esHealth;

    @Test
    public void testCheck() throws Exception {
        defaultLogger = LoggerFactory.getLogger(ElasticSearch.class);
        esConfig = new ElasticSampleConfiguration();
        esConfig.setElasticsearchClientNodeName("testjavaclient");
        esConfig.setElasticsearchClientPort("10080");
        esConfig.setElasticsearchCluster("elasticsearch-local");
        esConfig.setElasticsearchHost("localhost[9300-9400]");
        esClient = new ElasticSearch(esConfig);
        esHealth = new ElasticHealth(esClient);
        assertThat(esHealth.check()).isNotNull();
    }

}