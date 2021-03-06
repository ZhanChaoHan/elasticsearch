package com.jachs.elasticsearch.boot.rest;

import java.io.IOException;
import java.util.Map;
import java.util.Set;

import org.elasticsearch.action.admin.indices.alias.get.GetAliasesRequest;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.support.master.AcknowledgedResponse;
import org.elasticsearch.client.GetAliasesResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.cluster.metadata.AliasMetadata;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.jachs.elasticsearch.ElasticsearchApplication;

/****
 * 
 * @author zhanchaohan
 *
 */
@SpringBootTest(classes = ElasticsearchApplication.class)
public class ElasticsearchRestTemplateIndex {
	@Autowired
	private RestHighLevelClient elasticsearchClient;
	
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
