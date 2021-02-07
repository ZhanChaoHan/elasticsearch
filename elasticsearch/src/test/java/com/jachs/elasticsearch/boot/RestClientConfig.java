package com.jachs.elasticsearch.boot;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import org.assertj.core.internal.bytebuddy.utility.RandomString;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.support.WriteRequest.RefreshPolicy;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.elasticsearch.client.ClientConfiguration;
import org.springframework.data.elasticsearch.client.RestClients;
import org.springframework.data.elasticsearch.config.AbstractElasticsearchConfiguration;
import org.springframework.test.context.ContextConfiguration;


/***
 * High Level REST Client
 * 
 * @author zhanchaohan
 * @see
 *
 */
@SpringBootTest
@ContextConfiguration( locations = { "/applicationContext-elasticsearch.xml" } )
public class RestClientConfig extends AbstractElasticsearchConfiguration {
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

    @Override
    public RestHighLevelClient elasticsearchClient () {

        final ClientConfiguration clientConfiguration = ClientConfiguration.builder ()
                .connectedTo ( EsIp + ":"+EsPort ).build ();

        return RestClients.create ( clientConfiguration ).rest ();
    }

    @Test
    public void tt () throws IOException {
        RandomString rs=new RandomString ();
        Random random=new Random ();
        RestHighLevelClient highLevelClient=elasticsearchClient ();
        
        RestClient lowLevelClient = highLevelClient.getLowLevelClient ();

        Map<String, String>dateMap=new HashMap<String, String>();
        dateMap.put ( "sssssss", "aaaaaaaa" );
        dateMap.put ( "cccccccc", "222222225" );
        dateMap.put ( "sssaaaaaaaavvvcc", "222222225" );
        IndexRequest request = new IndexRequest ( "spring-data", rs.nextString (),random.nextInt ()+"" )
                .source (dateMap).setRefreshPolicy ( RefreshPolicy.IMMEDIATE );

        IndexResponse response = highLevelClient.index ( request, RequestOptions.DEFAULT );
        //试试低版本lowLevelClient,算了用不上
        System.out.println ( response.getId () );
    }

}