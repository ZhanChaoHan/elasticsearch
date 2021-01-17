package com.jachs.elasticsearch.boot.rest;

import java.io.IOException;
import java.util.Date;

import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.google.gson.Gson;
import com.jachs.elasticsearch.entity.BlogModel;

public class ElasticsearchRestTemplateAddTest {
	@Autowired
	private RestHighLevelClient elasticsearchClient;
	
	private static final String index="test";
	//单条添加
	@Test
	public void test1() throws IOException {
		 IndexRequest indexRequest = new IndexRequest(index);
		 BlogModel bm=new BlogModel();
		 bm.setId("我的測試");
		 bm.setTime(new Date(2025, 02, 25));
		 bm.setTitle("測試標題");
		 indexRequest.source(new Gson().toJson(bm), XContentType.JSON);
		 
		 elasticsearchClient.index(indexRequest, RequestOptions.DEFAULT);
	}
	//多条添加
	@Test
	public void test2() throws IOException {
		BulkRequest br=new BulkRequest();
		for (int ko = 0; ko < 5; ko++) {
			 IndexRequest indexRequest = new IndexRequest(index);
			 BlogModel bm=new BlogModel();
			 bm.setId("我的測試"+ko);
			 bm.setTime(new Date(2025, 02, 25));
			 bm.setTitle("測試標題"+ko);
			 indexRequest.source(new Gson().toJson(bm), XContentType.JSON);
			 
			 br.add(indexRequest);
		}
		 elasticsearchClient.bulk(br, RequestOptions.DEFAULT);
	}
}
