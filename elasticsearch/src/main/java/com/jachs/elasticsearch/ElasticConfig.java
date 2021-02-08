package com.jachs.elasticsearch;

import java.net.InetAddress;
import java.time.Duration;

import org.elasticsearch.client.ElasticsearchClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.TransportAddress;
import org.elasticsearch.script.mustache.SearchTemplateRequestBuilder;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.client.ClientConfiguration;
import org.springframework.data.elasticsearch.client.RestClients;
import org.springframework.data.elasticsearch.config.AbstractElasticsearchConfiguration;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;

/**
 * @author zhanchaohan
 * 
 */
@Configuration
public class ElasticConfig extends AbstractElasticsearchConfiguration {
    @Value("${elasticSearch.host.port}")
    private String hostAndPort;
    @Value("${elasticSearch.user}")
    private String user;
    @Value("${elasticSearch.password}")
    private String password;
    @Value("${elasticSearch.socketTimeout}")
    private long socketTimeout;

    //高版本客户端
    @Override
    @Bean
    public RestHighLevelClient elasticsearchClient() {
        final ClientConfiguration clientConfiguration = ClientConfiguration.builder()
                .connectedTo(hostAndPort)
                .withBasicAuth(user, password)
                .withSocketTimeout(Duration.ofSeconds(socketTimeout))
                .build();
        return RestClients.create(clientConfiguration).rest();
    }
    //spring提供的客户端
    @Bean
    public ElasticsearchRestTemplate restTemplate() throws Exception {
        return new ElasticsearchRestTemplate(elasticsearchClient());
    }
    /***
     * 模板客户端
     * @see https://www.elastic.co/guide/en/elasticsearch/client/java-api/current/java-admin-indices.html
     * @see https://www.elastic.co/guide/en/elasticsearch/client/java-api/current/java-search-template.html
     */
    @Bean
    public TransportClient SearchTemplate() throws Exception {
        TransportClient client = new PreBuiltTransportClient(Settings.EMPTY)
                .addTransportAddress(new TransportAddress(InetAddress.getByName(hostAndPort.split ( ":" )[0]), Integer.parseInt (hostAndPort.split ( ":" )[1])));   
         // on shutdown
//         client.close();
        
        return client;
    }
}