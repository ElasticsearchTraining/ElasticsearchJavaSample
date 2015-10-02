package org.sample.elastic.services.db;

import io.dropwizard.lifecycle.Managed;
import org.elasticsearch.action.WriteConsistencyLevel;
import org.elasticsearch.action.admin.indices.create.CreateIndexRequestBuilder;
import org.elasticsearch.action.admin.indices.mapping.put.PutMappingRequestBuilder;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.ImmutableSettings;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.common.xcontent.*;
import org.elasticsearch.index.query.FilterBuilders;
import org.elasticsearch.index.query.QueryStringQueryBuilder;
import org.elasticsearch.node.Node;
import org.elasticsearch.node.NodeBuilder;
import org.json.JSONObject;
import org.slf4j.Logger;
import java.util.Iterator;
import static org.elasticsearch.common.xcontent.XContentFactory.*;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.sample.elastic.services.core.ElasticSampleConfiguration;

public class ElasticSearch implements Managed{

    private Client elasticClient;
    private Node elasticNode;
    private static String elasticHost;
    private static String elasticClusterName;
    private static String elasticClientName;
    private static String elasticPort;

    public ElasticSearch(ElasticSampleConfiguration configuration){
        this.elasticHost = configuration.getElasticsearchHost();
        this.elasticClusterName = configuration.getElasticsearchCluster();
        this.elasticClientName = configuration.getElasticsearchClientNodeName();
        this.elasticPort = configuration.getElasticsearchClientPort();
    }

    //Method is called start so that it implements doStart from Dropwizard Managed
    //All these settings can be loaded from elasticsearch or a custom yml file or environment variables
    public void start() throws Exception {

        final Settings esSettings = ImmutableSettings.settingsBuilder()
                .put("node.name", elasticClientName) //name of the node
                .put("http.port", elasticPort) //port on which the node runs in client machine
                .put("discovery.zen.ping.multicast.enabled", false) //disable multicast
                .put("discovery.zen.ping.unicast.hosts", elasticHost) //set the unicast hosts
                .build();

        elasticNode = new NodeBuilder()
                .settings(esSettings)
                .clusterName(elasticClusterName)    //cluster to connect to
                .data(true)
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
                                 int startPage, int pageSize, String filterField, float startFilter,
                                 float endFilter, Logger esLogger) throws Exception {
        esLogger.info(term);
        QueryStringQueryBuilder queryStringQueryBuilder = new QueryStringQueryBuilder(term);

        SearchRequestBuilder searchRequestBuilder = elasticClient.prepareSearch(indexName)
                .setTypes(indexType)
                .setSearchType(SearchType.DEFAULT)
                .setQuery(queryStringQueryBuilder.buildAsBytes())
                .setFrom(startPage).setSize(pageSize).setExplain(false);

        if (filterField!=null && !filterField.isEmpty() && startFilter >= 0 && endFilter > 0)
                searchRequestBuilder.setPostFilter(FilterBuilders.rangeFilter(filterField).from(startFilter).to(endFilter));

        SearchResponse searchResponse = searchRequestBuilder
                .execute()
                .actionGet();

        return searchResponse;
    }

}
