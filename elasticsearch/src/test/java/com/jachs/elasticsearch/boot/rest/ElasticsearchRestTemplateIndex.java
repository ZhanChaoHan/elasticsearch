package com.jachs.elasticsearch.boot.rest;

import java.io.IOException;

import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.CreateIndexRequest;
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
}
