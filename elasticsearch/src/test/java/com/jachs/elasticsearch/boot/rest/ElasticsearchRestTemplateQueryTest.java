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
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
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
	//高亮
	@Test
	public void test4() throws IOException {
		String preTags = "<strong>";
	    String postTags = "</strong>";
	        
		SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
		//创建一个新的HighlightBuilder
		HighlightBuilder highlightBuilder = new HighlightBuilder(); 
		//创建title的高亮
		HighlightBuilder.Field highlightTitle =
		        new HighlightBuilder.Field("title"); 
		//设置高亮类型
		highlightTitle.highlighterType("unified");  
		//将高亮类型加入到highlightBuilder中
		highlightBuilder.field(highlightTitle);  
//		highlightBuilder.field(new HighlightBuilder.Field("money"));
		
		highlightBuilder.preTags(preTags);//设置前缀
	    highlightBuilder.postTags(postTags);//设置后缀
	    highlightBuilder.requireFieldMatch(false);//多次段高亮需要设置为false
		searchSourceBuilder.highlighter(highlightBuilder);
		searchSourceBuilder.query(QueryBuilders.matchQuery("title", "標題"));//查询符合高亮条件的数据
		SearchRequest searchRequest = new SearchRequest(index).source(searchSourceBuilder);
		SearchResponse srs=elasticsearchClient.search(searchRequest, RequestOptions.DEFAULT);
		System.out.println(srs);
	}
	@Test
	public void test5() throws IOException {
		SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
		
//		searchSourceBuilder.query(new BoolQueryBuilder().must(QueryBuilders.wildcardQuery("id", "我的測試3")));
		
		//match query：会对查询语句进行分词，分词后查询语句中的任何一个词项被匹配，文档就会被搜索到。如果想查询匹配所有关键词的文档，可以用and操作符连接；
//		searchSourceBuilder.query(new BoolQueryBuilder().should(QueryBuilders.matchPhraseQuery("id", "我的測試3")));
		//match_phrase query：满足下面两个条件才会被搜索到
		//（1）分词后所有词项都要出现在该字段中
		//（2）字段中的词项顺序要一致
		searchSourceBuilder.query(new BoolQueryBuilder().should(QueryBuilders.matchQuery("id", "我的測試3")));
		
		SearchRequest searchRequest = new SearchRequest(index).source(searchSourceBuilder);
		SearchResponse srs=elasticsearchClient.search(searchRequest, RequestOptions.DEFAULT);
		System.out.println(srs.getHits().getHits().length);
	}
	
}
