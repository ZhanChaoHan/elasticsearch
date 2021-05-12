package com.jachs.elasticsearch;

import java.time.Duration;

import org.elasticsearch.client.RestHighLevelClient;
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
}