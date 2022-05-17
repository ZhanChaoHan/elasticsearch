package com.jachs.restful.rest;

import java.io.IOException;
import java.util.Map;
import java.util.Set;

import org.apache.http.HttpHost;
import org.elasticsearch.action.admin.indices.alias.get.GetAliasesRequest;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.support.master.AcknowledgedResponse;
import org.elasticsearch.client.GetAliasesResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.cluster.metadata.AliasMetadata;
import org.junit.Before;
import org.junit.Test;


/****
 * 
 * @author zhanchaohan
 *
 */
public class ElasticsearchRestTemplateIndex {
	private RestHighLevelClient elasticsearchClient;
	
	@Before
	public void init() {
		elasticsearchClient=new RestHighLevelClient(RestClient.builder(
                HttpHost.create("localhost:1365")
                ));
	}
	
	//创建空的index
	@Test
	public void test1() throws IOException {
		CreateIndexRequest cir=new CreateIndexRequest("user");
		elasticsearchClient.indices().create(cir, RequestOptions.DEFAULT);
	}
	//获取全部索引
	@Test
	public void test2() throws IOException {
	    GetAliasesRequest request = new GetAliasesRequest();
        GetAliasesResponse getAliasesResponse =  elasticsearchClient.indices().getAlias(request,RequestOptions.DEFAULT);
        Map<String, Set<AliasMetadata>> map = getAliasesResponse.getAliases();
        Set<String> indices = map.keySet();
        for (String key : indices) {
            System.out.println(key);
        }
	}
	//单索引删除
	@Test
	public void test3() throws IOException {
	    AcknowledgedResponse acknowledgedResponse=elasticsearchClient.indices().delete ( new DeleteIndexRequest ( "2021-01-10" ), RequestOptions.DEFAULT );
	    
	    System.out.println ( acknowledgedResponse.isFragment () );
	}
	//全量索引删除
	@Test
    public void test4() throws IOException {
	    GetAliasesRequest request = new GetAliasesRequest();
        GetAliasesResponse getAliasesResponse =  elasticsearchClient.indices().getAlias(request,RequestOptions.DEFAULT);
        Map<String, Set<AliasMetadata>> map = getAliasesResponse.getAliases();
        Set<String> indices = map.keySet();
        for (String key : indices) {
            AcknowledgedResponse acknowledgedResponse=elasticsearchClient.indices().delete ( new DeleteIndexRequest ( key ), RequestOptions.DEFAULT );
            
            System.out.println (key+":"+acknowledgedResponse.isFragment () );
        }
    }
}
