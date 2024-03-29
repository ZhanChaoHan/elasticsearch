package com.jachs.restful.rest.add;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.time.DateUtils;
import org.apache.http.HttpHost;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.common.xcontent.XContentType;
import org.junit.Before;
import org.junit.Test;

import com.google.gson.Gson;
import com.jachs.elasticsearch.entity.BlogModel;

/****
 * 
 * @author zhanchaohan
 *
 */
public class ElasticsearchRestTemplateAddTest {
	private RestHighLevelClient elasticsearchClient;
	
	private static final String index="test";
	private static final String xbIndex="xbindex";
	
	@Before
	public void init() {
		elasticsearchClient=new RestHighLevelClient(RestClient.builder(
                HttpHost.create("localhost:1365")
                ));
	}
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
	    SimpleDateFormat sdf=new SimpleDateFormat ( "yyyy-MM-dd HH:mm:ss" );
	    SimpleDateFormat sdf1=new SimpleDateFormat ( "yyyyMMddHHmmss" );
	    
		BulkRequest br=new BulkRequest();
		Random random=new Random();
		DateUtils dus=new DateUtils ();
	    Date date=new Date();
	    
		for (int kk = 0; kk <50; kk++) {
		    Date randomDate= dus.addYears (date, date.getYear ()-random.nextInt (  2020));
			IndexRequest indexRequest = new IndexRequest("user");
			Map<String, Object>dateMap=new HashMap<String, Object>();
			if(kk<=5) {
				dateMap.put("group", "自行车");
				dateMap.put("country", "澳大利亚");
				dateMap.put("mtime", randomDate);
				dateMap.put("timestamp",sdf.format (randomDate));
				dateMap.put("timestamp1",sdf1.format (randomDate));
			}
			if(kk>5&&kk<=10) {
				dateMap.put("group", "小电驴");
				dateMap.put("country", "意大利");
				dateMap.put("mtime", randomDate);
                dateMap.put("timestamp",sdf.format (randomDate));
                dateMap.put("timestamp1",sdf1.format (randomDate));
			}
			if(kk>10&&kk<=15) {
				dateMap.put("group", "小汽车");
				dateMap.put("country", "大虾米");
				dateMap.put("mtime", randomDate);
                dateMap.put("timestamp",sdf.format (randomDate));
                dateMap.put("timestamp1",sdf1.format (randomDate));
			}
			if(kk>15&&kk<=20) {
				dateMap.put("group", "大步行");
				dateMap.put("country", "大米");
				dateMap.put("mtime", randomDate);
                dateMap.put("timestamp",sdf.format (randomDate));
                dateMap.put("timestamp1",sdf1.format (randomDate));
			}
			if(kk>25&&kk<=30) {
				dateMap.put("group", "tank");
				dateMap.put("country", "german");
				dateMap.put("mtime", randomDate);
                dateMap.put("timestamp",sdf.format (randomDate));
                dateMap.put("timestamp1",sdf1.format (randomDate));
			}
			if(kk>35&&kk<=40) {
				dateMap.put("group", "小的汽车");
				dateMap.put("country", "大虾米棒子");
				dateMap.put("mtime", randomDate);
                dateMap.put("timestamp",sdf.format (randomDate));
                dateMap.put("timestamp1",sdf1.format (randomDate));
			}
			if(kk>45&&kk<=50) {
                dateMap.put("group", "new grop");
                dateMap.put("country", "Africaman");
                dateMap.put("mtime", randomDate);
                dateMap.put("timestamp",sdf.format (randomDate));
                dateMap.put("timestamp1",sdf1.format (randomDate));
            }
			
			dateMap.put("userName", RandomStringUtils.random(6, 0x4e00, 0x9fa5, false,false));
			dateMap.put("userAge", random.nextInt(100));
			dateMap.put("userMoney", random.nextDouble());
			indexRequest.source(dateMap);
			
			br.add(indexRequest);
		}
		elasticsearchClient.bulk(br, RequestOptions.DEFAULT);
	}
	//添加大量数据
	@Test
	public void test6() throws IOException {
	    BulkRequest br=new BulkRequest();
	    String type="mytest";
	    
	    for ( int kk = 0 ; kk < 5000 ; kk++ ) {
    	    IndexRequest indexRequest = new IndexRequest(xbIndex);

    	    XContentBuilder xContentBuilder=XContentFactory.jsonBuilder()
    	            .startObject()
                    .startObject(type)
                    .startObject("properties")
                    .startObject("user")
                    .field("type",kk+"nbrrq")
                    .field("index",kk)
                    .endObject()
                    .startObject("postDate")
                    .field("type",kk)
                    .endObject()
                    .startObject("message")
                    .field("type","vbrw")
                    .field("index","not_analyzed")
                    .endObject()
                    .startObject("address")
                    .field("type","127.0.0.1")
                    .endObject()
                    .startObject("no")
                    .field("type","ace")
                    .field("index","ik")
                    .endObject()
                    .endObject()
                    .endObject()
                    .endObject ();
    	    
    	    indexRequest.source ( xContentBuilder );
    	    
    	    br.add ( indexRequest);
	    }
	    elasticsearchClient.bulk(br, RequestOptions.DEFAULT);
	}
}
