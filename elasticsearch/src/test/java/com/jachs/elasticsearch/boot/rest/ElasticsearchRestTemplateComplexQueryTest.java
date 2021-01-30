package com.jachs.elasticsearch.boot.rest;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.AggregationBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.terms.ParsedStringTerms;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.jachs.elasticsearch.ElasticsearchApplication;

/****
 * 复杂查询
 * 
 * @author zhanchaohan
 * @see ElasticsearchRestTemplateAddTest.test5
 * 
 * 
 *      Bucket Aggregation,一些满足特定条件的文档的集合 Metric Aggregation,一些数学计算，可以对文档字段统计分析
 *      Pipeline Aggregation,对其他的聚合结果进行二次聚合 Metrix
 *      Aggregation,支持对多个字段的操作并提供一个结果矩阵
 * 
 */
@SpringBootTest( classes = ElasticsearchApplication.class )
public class ElasticsearchRestTemplateComplexQueryTest {
    @Autowired
    private RestHighLevelClient elasticsearchClient;

    private static final String index = "user";

    private static final SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder ();
    private static final SearchRequest rq = new SearchRequest ( index );

    @BeforeAll
    public static void before () {
        searchSourceBuilder.from ( 0 ).size ( 5000 );//设置查询起始,截止下标。不设置默认查询10条数据
    }

    @AfterAll
    public static void after () {
    }

    //简单分组
    @Test
    public void test1 () throws IOException {
        TermsAggregationBuilder aggregationBuilder = AggregationBuilders.terms ( "ct" ).field ( "country.keyword" );

        searchSourceBuilder.aggregation ( aggregationBuilder );
        rq.source ( searchSourceBuilder );
        SearchResponse srr = elasticsearchClient.search ( rq, RequestOptions.DEFAULT );

        Aggregations aggregations = srr.getAggregations ();

        ParsedStringTerms  aga = aggregations.get ( "ct" );
        List<? extends Terms.Bucket> buckets = aga.getBuckets ();
        for ( Terms.Bucket bucket : buckets ) {
            System.out.println ( bucket.getKey ());
            System.out.println ( bucket.getDocCount ());
            System.out.println ( bucket.getDocCountError ());
            System.out.println ("-------------------------------------------------");
        }
    }
}
