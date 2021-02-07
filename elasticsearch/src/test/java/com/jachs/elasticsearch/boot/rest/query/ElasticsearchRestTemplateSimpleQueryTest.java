package com.jachs.elasticsearch.boot.rest.query;

import java.io.IOException;
import java.util.Map;

import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.BoolQueryBuilder;
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
 *条件查询
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
	//简单查询
	@Test
	public void test4() throws IOException {
		 rq.source(searchSourceBuilder);
		 String[] includeFields = new String[] {"country"};//将要查询出的字段
		 
		//不分词查询,因为不分词所有汉字只能查一个字,不会对搜索词进行分词处理，而是作为一个整体与目标字段进行匹配，若完全匹配，则可查询到。
//		 searchSourceBuilder.query(QueryBuilders.termQuery ( "country.keyword", "大米" ));
		 //多个参数查询
//		 searchSourceBuilder.query(QueryBuilders.termsQuery ( "country.keyword", "大米","german" ));
		 
		//全查
//		 searchSourceBuilder.query(QueryBuilders.matchAllQuery());
		 //常用的字符串查询，左右查询
//		 searchSourceBuilder.query ( QueryBuilders.queryStringQuery ( "虾米" ).field ( "country" ) );
		//前缀查询,指定分词
//		 searchSourceBuilder.query(QueryBuilders.prefixQuery ( "country.keyword", "大" ));
		 //不指定前缀，like查询
//		 searchSourceBuilder.query(QueryBuilders.prefixQuery ( "country", "大" ));
		//匹配上一个字符就查出来
//		 searchSourceBuilder.query(QueryBuilders.matchQuery("country", "澳大利亚"));
//		 searchSourceBuilder.query(QueryBuilders.matchBoolPrefixQuery("country", "澳大利亚"));
//		 searchSourceBuilder.query(QueryBuilders.multiMatchQuery("大", "country","group"));//将要匹配的字符串,...想要匹配的列
//		 searchSourceBuilder.query(QueryBuilders.matchPhrasePrefixQuery("country", "大虾米"));//前缀查询全匹配
		 
		 //通配符查询
		 searchSourceBuilder.query(QueryBuilders.wildcardQuery ( "country", "大" ));
		 
		 searchSourceBuilder.fetchSource(includeFields, null);
		 SearchResponse srr=elasticsearchClient.search(rq, RequestOptions.DEFAULT);
		 
		 SearchHit[] searchHit=srr.getHits().getHits();
		 printSearchHit(searchHit);
		 System.out.println(searchHit.length);
	}
	/***
	 * QueryBuilders.boolQuery ();布尔条件判断
	 * QueryBuilders.boolQuery ().must ();必须完全匹配,and  &&
       QueryBuilders.boolQuery ().mustNot ();必须不匹配,not !
       QueryBuilders.boolQuery ().should ();至少满足一个,相当or ||
	 */
	@Test
	public void test5() throws IOException {
	    String[] includeFields = new String[] {"country","group"};//将要查询出的字段
	    searchSourceBuilder.fetchSource(includeFields, null);
        
        BoolQueryBuilder boolQueryBuilder= QueryBuilders.boolQuery ();
        
        //country==意大利&&group==小电驴
//        boolQueryBuilder.must ( QueryBuilders.termQuery ( "country.keyword", "意大利" ) )
//        .must ( QueryBuilders.termQuery ( "group.keyword", "小电驴" ) );
        
        
        //country==大虾米||group==小电驴,前条件满足了后条件就不执行了
        boolQueryBuilder.must ( QueryBuilders.termQuery ( "country.keyword", "大虾米" ) )
      .should( QueryBuilders.termQuery ( "group.keyword", "小电驴" ) );
        
        searchSourceBuilder.query ( boolQueryBuilder );
        
        
        rq.source(searchSourceBuilder);
        
        SearchResponse srr=elasticsearchClient.search(rq, RequestOptions.DEFAULT);
        SearchHit[] searchHit=srr.getHits().getHits();
        printSearchHit(searchHit);
        System.out.println(searchHit.length);
	}
}
