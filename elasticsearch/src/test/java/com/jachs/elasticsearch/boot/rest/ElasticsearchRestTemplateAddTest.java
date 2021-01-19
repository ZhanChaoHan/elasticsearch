package com.jachs.elasticsearch.boot.rest;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import org.apache.commons.lang.RandomStringUtils;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
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
public class ElasticsearchRestTemplateAddTest {
	@Autowired
	private RestHighLevelClient elasticsearchClient;
	
	private static final String index="test";
	//单条添加,创建索引
	@Test
	public void test1() throws IOException {
		 IndexRequest indexRequest = new IndexRequest(index);
		 BlogModel bm=new BlogModel();
		 bm.setId("我的測試");
		 bm.setTime(new Date(2025, 02, 25));
		 bm.setTitle("測試標題");
		 bm.setAge(12);
		 bm.setMoney(66.66);
		 indexRequest.source(new Gson().toJson(bm), XContentType.JSON);
		 
		 elasticsearchClient.index(indexRequest, RequestOptions.DEFAULT);
	}
	//多条添加
	@Test
	public void test2() throws IOException {
		BulkRequest br=new BulkRequest();
		Random rd=new Random();
		for (int ko = 0; ko < 5; ko++) {
			 IndexRequest indexRequest = new IndexRequest(index);
			 BlogModel bm=new BlogModel();
			 bm.setId("我的測試"+ko);
			 bm.setTime(new Date(2025, rd.nextInt(12), rd.nextInt(30)));
//			 bm.setTitle("測試標題"+ko);
			 bm.setTitle("we goo"+ko);
			 bm.setAge(rd.nextInt(100));
			 bm.setMoney(rd.nextDouble());
			 
			 indexRequest.source(new Gson().toJson(bm), XContentType.JSON);
			 
			 br.add(indexRequest);
		}
		 elasticsearchClient.bulk(br, RequestOptions.DEFAULT);
	}
	//一个批处理添加多个索引数据
	@Test
	public void test3() throws IOException {
		BulkRequest request = new BulkRequest(); 
		request.add(new IndexRequest("test")
		        .source(XContentType.JSON,"tcc", "decription1"));
		request.add(new IndexRequest("blog")  
		        .source(XContentType.JSON,"tcc", "decription2"));
		request.add(new IndexRequest("iprogram")  
		        .source(XContentType.JSON,"tcc", "decription3"));
		
		 elasticsearchClient.bulk(request, RequestOptions.DEFAULT);
	}
	@Test
	public void test5() throws IOException {
		BulkRequest br=new BulkRequest();
		Random random=new Random();
		
		for (int kk = 0; kk <50; kk++) {
			IndexRequest indexRequest = new IndexRequest("user");
			Map<String, Object>dateMap=new HashMap<String, Object>();
			if(kk<=10) {
				dateMap.put("group", "自行车");
			}
			if(kk>10&&kk<=20) {
				dateMap.put("group", "小电驴");
			}
			if(kk>20&&kk<=30) {
				dateMap.put("group", "小汽车");
			}
			if(kk>30&&kk<=40) {
				dateMap.put("group", "步行");
			}
			if(kk>40) {
				dateMap.put("group", "tank");
			}
			dateMap.put("userName", RandomStringUtils.random(6, 0x4e00, 0x9fa5, false,false));
			dateMap.put("userAge", random.nextInt(100));
			dateMap.put("userMoney", random.nextDouble());
			indexRequest.source(dateMap);
			
			br.add(indexRequest);
		}
		elasticsearchClient.bulk(br, RequestOptions.DEFAULT);
	}
}
