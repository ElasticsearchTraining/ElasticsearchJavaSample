package org.sample.elastic.services.health;

import com.codahale.metrics.health.HealthCheck;
import org.sample.elastic.services.db.ElasticSearch;

public class ElasticHealth extends HealthCheck {
    private final ElasticSearch elasticSearch;

    public ElasticHealth(ElasticSearch elasticSearch) {
        this.elasticSearch = elasticSearch;
    }

    @Override
    protected Result check() throws Exception {
        if (elasticSearch.toString().isEmpty()) {
            return Result.unhealthy("Unable to connect to ElasticSearch");
        } else {
            return Result.healthy();
        }
    }
}