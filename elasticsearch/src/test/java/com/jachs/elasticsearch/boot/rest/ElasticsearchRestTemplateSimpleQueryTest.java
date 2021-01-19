package com.jachs.elasticsearch.boot.rest;

import java.io.IOException;
import java.util.Map;

import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
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
	//全查
	@Test
	public void test1() throws IOException {
		 searchSourceBuilder.from(0).size(5000);//设置查询起始,截止下标。不设置默认查询10条数据
		 SearchRequest rq = new SearchRequest(index);
		 
		 rq.source(searchSourceBuilder);
		 SearchResponse srr=elasticsearchClient.search(rq, RequestOptions.DEFAULT);
		 
		 SearchHit[] searchHit=srr.getHits().getHits();
		 for (SearchHit sh : searchHit) {
			 Map<String, Object> shData= sh.getSourceAsMap();
			 for (String key : shData.keySet()) {
				System.out.println(key+"\t\t"+shData.get(key));
			}
			 System.out.println("华丽的分割线--------------------------------------------------------------------------------------------------");
		}
		 
		 System.out.println(searchHit.length);
	}
}
