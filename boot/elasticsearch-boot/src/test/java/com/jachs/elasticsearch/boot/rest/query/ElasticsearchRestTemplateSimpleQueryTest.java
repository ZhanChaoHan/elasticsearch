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
	/****
	 * 区间查询</br>
	 * gt>,lt<;gte>=,lte<=</br>
	 * 1. 核心数据类型</br>
                    （1）字符串类型： text, keyword</br>
                    （2）数字类型：long, integer, short, byte, double, float, half_float, scaled_float</br>
                    （3）日期：date</br>
                    （4）日期 纳秒：date_nanos</br>
                    （5）布尔型：boolean</br>
                    （6）Binary：binary</br>
                    （7）Range: integer_range, float_range, long_range, double_range, date_range</br>
       2. 复杂数据类型</br>
                    （1）Object: object(for single JSON objects)</br>
                    （2）Nested: nested (for arrays of JSON objects)</br>
       3. 地理数据类型</br>
                    （1）Geo-point： geo_point （for lat/lon points）</br>
                    （2）Geo-shape: geo_shape (for complex shapes like polygons)</br>
       4. 特殊数据类型</br>
                    （1）IP:  ip (IPv4 和 IPv6 地址)</br>
                    （2）Completion类型：completion （to provide auto-complete suggestions）</br>
                    （3）Token count：token_count (to count the number of tokens in a string)</br>
                    （4）mapper-murmur3：murmur3(to compute hashes of values at index-time and store them in the index)</br>
                    （5）mapper-annotated-text：annotated-text （to index text containing special markup (typically used for identifying named entities)）</br>
                    （6）Percolator：（Accepts queries from the query-dsl）</br>
                    （7）Join：（Defines parent/child relation for documents within the same index）</br>
                    （8）Alias：（Defines an alias to an existing field.）</br>
                    （9）Rank feature：（Record numeric feature to boost hits at query time.）</br>
                    （10）Rank features：（Record numeric features to boost hits at query time.）</br>
                    （11）Dense vector：（Record dense vectors of float values.）</br>
                    （12）Sparse vector：（Record sparse vectors of float values.）</br>
                    （13）Search-as-you-type：（A text-like field optimized for queries to implement as-you-type completion）</br>
       5.数组类型</br>
                            在Elasticsearch中，数组不需要一个特定的数据类型，任何字段都默认可以包含一个或多个值，当然，这多个值都必须是字段指定的数据类型。</br>
                    6.Multi-fields</br>
                    Multi-fields 通常用来以不同的方式或目的索引同一个字段。比如，一个字符串类型字段可以同时被映射为 text 类型以用于全文检索、 keyword字段用于排序或聚合。又或者，你可以用standard分析器、english分析器和french分析器来索引同一个 text字段。
	 * @author zhanchaohan
	 */
	@Test
	public void test2() throws IOException {
		 rq.source(searchSourceBuilder);
		 //数据区间查询
//		 searchSourceBuilder.query(QueryBuilders.rangeQuery("userAge").from ( 15 ).to ( 20 ));//userAge>=15&&userAge<=20
//		 searchSourceBuilder.query(QueryBuilders.rangeQuery("userAge").from ( 15,false ).to ( 20 ,false));
	 
//		 searchSourceBuilder.query(QueryBuilders.rangeQuery("userAge").gt(15).lt(20));
//		 searchSourceBuilder.query(QueryBuilders.rangeQuery("userAge").gte(15).lte(20));
		 
//		 searchSourceBuilder.query(QueryBuilders.rangeQuery("userMoney").gte(new Float ( 0.6046738164042479 )).lte(new Float ( 0.8046738164042479 )));
//		 searchSourceBuilder.query(QueryBuilders.rangeQuery("userMoney").gte(0.6046738164042479).lte(0.8046738164042479));
		 
		 //时间区间查询
//		 searchSourceBuilder.query(QueryBuilders.rangeQuery("timestamp1").from ( 14870224164535L ).to ( 14870224174535L ));
		 
//		 searchSourceBuilder.query(QueryBuilders.rangeQuery("mtime.keyword").gte ( "1520-03-06T08:45:35.200Z" )
//		         .lte ( "1530-03-06T08:45:35.200Z" ));
		 
		 
		 SearchResponse srr=elasticsearchClient.search(rq, RequestOptions.DEFAULT);
		 
		 SearchHit[] searchHit=srr.getHits().getHits();
		 System.out.println(searchHit.length);
		 printSearchHit(searchHit);
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
