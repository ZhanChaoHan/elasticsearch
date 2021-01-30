package com.jachs.elasticsearch.boot.rest;

import java.io.IOException;
import java.util.Map;

import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.jachs.elasticsearch.ElasticsearchApplication;

/****
 * 简单查询
 * @author zhanchaohan
 * @see ElasticsearchRestTemplateAddTest.test5
 */
@SpringBootTest(classes = ElasticsearchApplication.class)
public class ElasticsearchRestTemplateSimpleQueryTest {
	@Autowired
	private RestHighLevelClient elasticsearchClient;
	
	private static final String index="user";
	
	private static final SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
	private static final SearchRequest rq = new SearchRequest(index);
	@BeforeAll
	public static void before() {
		searchSourceBuilder.from(0).size(5000);//设置查询起始,截止下标。不设置默认查询10条数据
	}
	@AfterAll
	public static void after() {
	}
	
	private void printSearchHit(SearchHit[] searchHit) {
		 for (SearchHit sh : searchHit) {
			 Map<String, Object> shData= sh.getSourceAsMap();
			 for (String key : shData.keySet()) {
				System.out.println(key+"\t\t"+shData.get(key));
			}
			 System.out.println("华丽的分割线--------------------------------------------------------------------------------------------------");
		}
	}
	//全查
	@Test
	public void test1() throws IOException {
		 rq.source(searchSourceBuilder);
		 SearchResponse srr=elasticsearchClient.search(rq, RequestOptions.DEFAULT);
		 
		 SearchHit[] searchHit=srr.getHits().getHits();
		 printSearchHit(searchHit);
		 System.out.println(searchHit.length);
	}
	//区间查询
	//gt>,lt<;gte>=,lte<=
	@Test
	public void test2() throws IOException {
		 rq.source(searchSourceBuilder);
		 //数据区间查询
//		 searchSourceBuilder.query(QueryBuilders.rangeQuery("userAge").from ( 15 ).to ( 20 ));//userAge>=15&&userAge<=20
//		 searchSourceBuilder.query(QueryBuilders.rangeQuery("userAge").from ( 15,false ).to ( 20 ,false));
	 
//		 searchSourceBuilder.query(QueryBuilders.rangeQuery("userAge").gt(15).lt(20));
//		 searchSourceBuilder.query(QueryBuilders.rangeQuery("userAge").gte(15).lte(20));
		 
		 
		 //时间区间查询
		 searchSourceBuilder.query(QueryBuilders.rangeQuery("timestamp1").from ( 20210129174011L ).to ( 20210229174011L ));
		 
		 
		 SearchResponse srr=elasticsearchClient.search(rq, RequestOptions.DEFAULT);
		 
		 SearchHit[] searchHit=srr.getHits().getHits();
		 printSearchHit(searchHit);
		 System.out.println(searchHit.length);
	}
	@Test
	public void test3() throws IOException {
		 rq.source(searchSourceBuilder);
		 
//		 searchSourceBuilder.query(QueryBuilders.matchAllQuery());//全查
//		 searchSourceBuilder.query(QueryBuilders.matchQuery("country", "澳大利亚"));//匹配上一个字符就查出来
		 
//		 searchSourceBuilder.query(QueryBuilders.matchBoolPrefixQuery("country", "澳大利亚"));
		 
//		 searchSourceBuilder.query(QueryBuilders.multiMatchQuery("大", "country","group"));//将要匹配的字符串,...想要匹配的列
		 
		 searchSourceBuilder.query(QueryBuilders.matchPhrasePrefixQuery("country", "大虾米"));//前缀查询全匹配
		 
		 SearchResponse srr=elasticsearchClient.search(rq, RequestOptions.DEFAULT);
		 
		 SearchHit[] searchHit=srr.getHits().getHits();
		 printSearchHit(searchHit);
		 System.out.println(searchHit.length);
	}
}
