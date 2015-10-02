package org.sample.elastic.services.db;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.Test;
import org.sample.elastic.services.core.ElasticSampleConfiguration;

public class ElasticSearchTest {

    @Test
    public void testStart() throws Exception {
        ElasticSearch es = new ElasticSearch(new ElasticSampleConfiguration());
        //System.out.println(es.toString());
        assertThat(es.toString().substring(0, 44)).isEqualTo("org.sample.elastic.services.db.ElasticSearch");
    }
}