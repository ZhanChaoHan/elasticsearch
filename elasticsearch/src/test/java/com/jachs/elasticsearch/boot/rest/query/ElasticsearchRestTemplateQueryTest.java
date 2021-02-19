package com.jachs.elasticsearch.boot.rest.query;

import java.io.IOException;

import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchScrollAction;
import org.elasticsearch.action.search.SearchScrollRequest;
import org.elasticsearch.action.search.SearchScrollRequestBuilder;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.Scroll;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.alibaba.fastjson.JSONObject;
import com.jachs.elasticsearch.ElasticsearchApplication;

/****
索引简单查询
 * 分页查询,排序
 * @author zhanchaohan
 *
 */
@SpringBootTest(classes = ElasticsearchApplication.class)
public class ElasticsearchRestTemplateQueryTest {
	@Autowired
	private RestHighLevelClient elasticsearchClient;
	
	private static final String index="test";
	private static final String xbIndex="xbindex";
	
	
	private void printSearchHit(SearchHit[] searchHit) {
        for (SearchHit sh : searchHit) {
            JSONObject json=new JSONObject().parseObject ( sh.getSourceAsString () );
            
            JSONObject properties= json.getJSONObject ( "mytest" ).getJSONObject ( "properties" );
            
            JSONObject user= properties.getJSONObject ( "user" );
            JSONObject postDate= properties.getJSONObject ( "postDate" );
            JSONObject message= properties.getJSONObject ( "message" );
            JSONObject address= properties.getJSONObject ( "address" );
            JSONObject no= properties.getJSONObject ( "no" );
            
            System.out.println ("user:"+ user.getString ( "type" )+"\t\t"+user.getString ( "index" ));
            System.out.println ("postDate:"+ postDate.getString ( "type" ));
            System.out.println ("message:"+ message.getString ( "type" )+"\t\t"+message.getString ( "index" ));
            System.out.println ("address:"+ address.getString ( "type" ));
            System.out.println ("no:"+no.getString ( "index" )+"\t\t"+ no.getString ( "type" ));
            System.out.println ( "---------------------------------------------------------------" );
       }
   }

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
	
	/****
	 * 数据来源：com.jachs.elasticsearch.boot.rest.add.ElasticsearchRestTemplateAddTest.test6()
	 * 分页查询,from-size"浅"分页
	 */
	@Test
	public void test6() throws IOException {
	    SearchRequest rq = new SearchRequest(xbIndex);
	    SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
	    
	    searchSourceBuilder.from ( 0 );
	    searchSourceBuilder.size ( 20 );
	    
	    rq.source ( searchSourceBuilder );
	    SearchResponse srr=elasticsearchClient.search(rq, RequestOptions.DEFAULT);
	    SearchHits searchHits=srr.getHits();
	    
        System.out.println(searchHits.getHits().length);
        
        printSearchHit(searchHits.getHits ());
	}
	/***
	 * from + size浅分页查询全部,数据量10000-50000
	 */
	@Test
	public void test7() throws IOException {
	    int pageCount=10;//一页展示多少条
	    int page=1;//当前页
	    
	    while(true) {
    	    SearchRequest rq = new SearchRequest(xbIndex);
            SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
            
            searchSourceBuilder.from ( (page-1)*page );
            searchSourceBuilder.size ( page*pageCount );
            
            rq.source ( searchSourceBuilder );
            SearchResponse srr=elasticsearchClient.search(rq, RequestOptions.DEFAULT);
            SearchHits searchHits=srr.getHits();
            
            if(searchHits.getHits().length==0) {
                break;
            }
            System.out.println(searchHits.getHits().length);
            printSearchHit(searchHits.getHits ());
            page++;
	    }
	}
	//报错找不到id待解决
	@Test
	public void test8() throws IOException {
	    SearchScrollRequest rq = new SearchScrollRequest(xbIndex);

	    Scroll scroll=new Scroll(TimeValue.timeValueMillis ( 1000 ));
	    scroll.keepAlive ();
	    
	    rq.scroll ( scroll );
	    
	    rq.scrollId ( "OidCf3cBgoU9kxH5Zl15" );
	    rq.scroll ( "1m");
        
	    
        SearchResponse srr=elasticsearchClient.scroll ( rq, RequestOptions.DEFAULT );
        SearchHits searchHits=srr.getHits();
        
        srr.getScrollId ();
        
        printSearchHit(searchHits.getHits ());
	}
}
