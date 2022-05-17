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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.elasticsearch.client.ClientConfiguration;
import org.springframework.data.elasticsearch.client.RestClients;
import org.springframework.data.elasticsearch.config.AbstractElasticsearchConfiguration;


/***
 * High Level REST Client
 * 
 * @author zhanchaohan
 * @see
 *
 */
@SpringBootTest
public class RestClientConfig {
	@Autowired
    private RestHighLevelClient restHighLevelClient;

    @Test
    public void tt () throws IOException {
        RandomString rs=new RandomString ();
        Random random=new Random ();
        
        RestClient lowLevelClient = restHighLevelClient.getLowLevelClient ();

        Map<String, String>dateMap=new HashMap<String, String>();
        dateMap.put ( "sssssss", "aaaaaaaa" );
        dateMap.put ( "cccccccc", "222222225" );
        dateMap.put ( "sssaaaaaaaavvvcc", "222222225" );
        IndexRequest request = new IndexRequest ( "spring-data", rs.nextString (),random.nextInt ()+"" )
                .source (dateMap).setRefreshPolicy ( RefreshPolicy.IMMEDIATE );

        IndexResponse response = restHighLevelClient.index ( request, RequestOptions.DEFAULT );
        //试试低版本lowLevelClient,算了用不上
        System.out.println ( response.getId () );
    }

}