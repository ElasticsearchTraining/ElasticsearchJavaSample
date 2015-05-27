package org.sample.elastic.services.db;

import io.dropwizard.lifecycle.Managed;
import org.apache.lucene.queryparser.xml.builders.FilteredQueryBuilder;
import org.apache.lucene.queryparser.xml.builders.RangeFilterBuilder;
import org.elasticsearch.action.WriteConsistencyLevel;
import org.elasticsearch.action.admin.indices.create.CreateIndexRequestBuilder;
import org.elasticsearch.action.admin.indices.mapping.put.PutMappingRequestBuilder;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.common.settings.ImmutableSettings;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.xcontent.*;
import org.elasticsearch.index.query.FilterBuilders;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.QueryStringQueryBuilder;
import org.elasticsearch.node.Node;
import org.elasticsearch.node.NodeBuilder;
import org.json.JSONObject;
import org.slf4j.Logger;
import java.util.Iterator;
import static org.elasticsearch.common.xcontent.XContentFactory.*;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;

public class ElasticSearch implements Managed{

    private Client elasticClient;
    private Node elasticNode;

    //Method is called start so that it implements doStart from Dropwizard Managed
    public void start() throws Exception {
        final Settings esSettings = ImmutableSettings.settingsBuilder()
                .put("node.name", "javaclient")
                .put("http.port", "10080")
                .put("discovery.zen.ping.multicast.enabled",false)
                .put("discovery.zen.ping.unicast.hosts","localhost[9300-9400]")
                .build();

        elasticNode = new NodeBuilder()
                .settings(esSettings)
                .clusterName("elasticsearch-local")
                .client(true)
                .build()
                .start();

        //this.elasticNode = nodeBuilder().client(true).clusterName("elasticsearch-local").data(false).settings()
        this.elasticClient = elasticNode.client();
    }

    //Method is called stop so that it implements doStop from Dropwizard Managed
    public void stop() throws Exception {
        this.elasticClient.close();
        this.elasticNode.stop();
    }

    public boolean createIndex(String indexName, String indexAnalyzer, String indexType, String indexFields,
                               Logger esLogger) throws Exception {

        XContentBuilder buildIndexSettings = XContentFactory.jsonBuilder()
                .startObject()
                .startObject("index")
                .startObject("analysis")
                .startObject("analyzer")
                .startObject(indexAnalyzer)
                .array("filter", new String[]{"standard", "lowercase", "asciifolding"})
                .field("type", indexAnalyzer)
                .field("tokenizer", "standard")
                .endObject()
                .endObject()
                .endObject()
                .endObject()
                .endObject();

        CreateIndexRequestBuilder createIndexRequestBuilder = elasticClient.admin()
                .indices()
                .prepareCreate(indexName)
                .setSettings(buildIndexSettings);

        JSONObject jObject = new JSONObject(indexFields.trim());
        Iterator<?> fields = jObject.keys();

        XContentBuilder buildMapping = jsonBuilder()
                .prettyPrint()
                .humanReadable(true)
                .startObject()
                .startObject(indexType)
                .startObject("properties");

        while( fields.hasNext() ) {
            String field = (String) fields.next();
            if (jObject.get(field) instanceof JSONObject) {
                buildMapping.startObject(field);
                JSONObject propertiesValues = jObject.getJSONObject(field);
                Iterator<?> properties = propertiesValues.keys();
                while (properties.hasNext()) {
                    String property = (String) properties.next();
                    if (propertiesValues instanceof JSONObject) {
                        buildMapping.field(property, propertiesValues.getString(property));
                    }
                }
                buildMapping.endObject();
            }
        }

        buildMapping.endObject()
                .endObject()
                .endObject();

        esLogger.info(buildMapping.string());

        PutMappingRequestBuilder putMappingRequestBuilder = elasticClient
                .admin()
                .indices()
                .preparePutMapping(indexName)
                .setType(indexType)
                .setSource(buildMapping);

        if (createIndexRequestBuilder.execute().actionGet().isAcknowledged()) {
            return putMappingRequestBuilder.execute()
                    .actionGet()
                    .isAcknowledged();
        } else
            return false;
    }

    public boolean createDocument(String indexName, String indexType, String document,
                                  Logger esLogger) throws Exception {
        esLogger.info(document);
        IndexResponse indexResponse = elasticClient.prepareIndex(indexName, indexType)
                .setConsistencyLevel(WriteConsistencyLevel.DEFAULT)
                .setSource(document)
                .execute()
                .actionGet();
        return indexResponse.isCreated();
    }

    public SearchResponse search(String indexName, String indexType, String term,
                                 int startPage, int pageSize, float startPrice, float endPrice,
                                 Logger esLogger) throws Exception {
        esLogger.info(term);
        QueryStringQueryBuilder queryStringQueryBuilder = new QueryStringQueryBuilder(term);

        SearchResponse searchResponse = elasticClient.prepareSearch(indexName)
                .setTypes(indexType)
                .setSearchType(SearchType.DEFAULT)
                .setQuery(queryStringQueryBuilder.buildAsBytes())
                .setPostFilter(FilterBuilders.rangeFilter("price").from(startPrice).to(endPrice))
                .setFrom(startPage).setSize(pageSize).setExplain(false)
                .execute()
                .actionGet();
        return searchResponse;
    }

}
