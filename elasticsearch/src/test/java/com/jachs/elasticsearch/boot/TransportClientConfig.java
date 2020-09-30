package com.jachs.elasticsearch.boot;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import org.elasticsearch.action.ActionFuture;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.support.WriteRequest.RefreshPolicy;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.TransportAddress;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.data.elasticsearch.config.ElasticsearchConfigurationSupport;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.test.context.ContextConfiguration;

/***
 * Transport Client
 * 
 * @author zhanchaohan
 * @see https://docs.spring.io/spring-data/elasticsearch/docs/4.0.4.RELEASE/reference/html/#elasticsearch.clients
 *
 */
@SpringBootTest
@ContextConfiguration(locations = { "/applicationContext-elasticsearch.xml" })
public class TransportClientConfig extends ElasticsearchConfigurationSupport {
    @Value( "${cluster.name}" )
    private String ClusterName;
    @Value( "${network.host}" )
    private String EsIp;
    @Value( "${http.port}" )
    private int EsPort;
    @Value( "${elasticsearch.index}" )
    private String EsIndex;
    @Value( "${elasticsearch.index.type}" )
    private String EsType;
    
    @Bean
    public Client elasticsearchClient () throws UnknownHostException {
        Settings settings = Settings.builder ().put ( "cluster.name", ClusterName ).build ();
        TransportClient client = new PreBuiltTransportClient ( settings );
        client.addTransportAddress ( new TransportAddress ( InetAddress.getByName ( EsIp ), EsPort ) );
        return client;
    }

    @Bean( name = { "elasticsearchOperations", "elasticsearchTemplate" } )
    public ElasticsearchTemplate elasticsearchTemplate () throws UnknownHostException {
        return new ElasticsearchTemplate ( elasticsearchClient () );
    }

    @Test
    public void tt () throws UnknownHostException {
        Random random=new Random ();
        Map<String, String>map=new HashMap<> ();
        map.put ( "一个字符串", "西巴西巴巴" );
        map.put ( "AantherString", "阿巴阿巴阿巴" );
        IndexRequest request = new IndexRequest ( "spring-data", "elasticsearch", random.nextInt ()+"" )
                .source ( map )
                .setRefreshPolicy ( RefreshPolicy.IMMEDIATE );
        
        ActionFuture<IndexResponse> response =  elasticsearchClient ().index ( request );
        
        System.out.println ( response.isDone () );
    }
}
