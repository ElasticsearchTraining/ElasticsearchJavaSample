package org.sample.elastic.services.db;

import io.dropwizard.lifecycle.Managed;
import org.elasticsearch.client.Client;
import org.elasticsearch.common.settings.ImmutableSettings;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.node.Node;
import org.elasticsearch.node.NodeBuilder;

public class ElasticSearch implements Managed{

    private Client elasticClient;
    private Node elasticNode;

    //Method is called start so that it implements doStart from Dropwizard Managed
    public void start() throws Exception {
        final Settings esSettings = ImmutableSettings.settingsBuilder()
                .put("node.name", "javaclient")
                .put("http.port", "10080")
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

}
