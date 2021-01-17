package com.jachs.elasticsearch.boot.rest;

import java.io.IOException;
import java.util.Date;
import java.util.Random;

import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.search.SearchAction;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.google.gson.Gson;
import com.jachs.elasticsearch.ElasticsearchApplication;
import com.jachs.elasticsearch.entity.BlogModel;

/****
 * 
 * @author zhanchaohan
 *
 */
@SpringBootTest(classes = ElasticsearchApplication.class)
public class ElasticsearchRestTemplateQueryTest {
	@Autowired
	private RestHighLevelClient elasticsearchClient;
	
	private static final String index="test";
	
	//全查
	@Test
	public void test1() throws IOException {
		 SearchRequest rq = new SearchRequest(index);
		 
		 SearchResponse srr=elasticsearchClient.search(rq, RequestOptions.DEFAULT);
		 System.out.println(srr.getHits().getHits().length);
	}
	//Id单查
	@Test
	public void test2() throws IOException {
		GetRequest request = new GetRequest(index, "我的測試2");
		System.out.println(elasticsearchClient.get(request, RequestOptions.DEFAULT));
	}
	//过滤查询字段
	@Test
	public void test3() throws IOException {
		String[] includeFields = new String[] {"id", "age"};//将要查询出的字段
				String[] excludeFields = new String[] {"money","time"};//过滤掉的字段
				
				
		SearchRequest searchRequest = new SearchRequest(index); 
		SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
		
		sourceBuilder.fetchSource(includeFields, excludeFields);
		
		
		searchRequest.source(sourceBuilder);
		SearchResponse srs=elasticsearchClient.search(searchRequest, RequestOptions.DEFAULT);
		System.out.println(srs);
	}
}
