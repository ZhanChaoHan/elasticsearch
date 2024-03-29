package com.jachs.restful.rest.query;

import java.io.IOException;
import java.util.List;

import org.apache.http.HttpHost;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.filter.Filter;
import org.elasticsearch.search.aggregations.bucket.filter.FilterAggregationBuilder;
import org.elasticsearch.search.aggregations.bucket.terms.ParsedStringTerms;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
import org.elasticsearch.search.aggregations.metrics.Avg;
import org.elasticsearch.search.aggregations.metrics.AvgAggregationBuilder;
import org.elasticsearch.search.aggregations.metrics.Max;
import org.elasticsearch.search.aggregations.metrics.MaxAggregationBuilder;
import org.elasticsearch.search.aggregations.metrics.Min;
import org.elasticsearch.search.aggregations.metrics.MinAggregationBuilder;
import org.elasticsearch.search.aggregations.metrics.Sum;
import org.elasticsearch.search.aggregations.metrics.SumAggregationBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.junit.Before;
import org.junit.Test;

/****
 * 复杂查询 聚合查询
 * 
 * @author zhanchaohan
 * @see ElasticsearchRestTemplateAddTest.test5</br>
 *      Bucket Aggregation,一些满足特定条件的文档的集合 Metric
 *      Aggregation,一些数学计算，可以对文档字段统计分析</br>
 *      Pipeline Aggregation,对其他的聚合结果进行二次聚合 Metrix</br>
 *      Aggregation,支持对多个字段的操作并提供一个结果矩阵</br>
 * 
 */
public class ElasticsearchRestTemplateComplexQueryTest {
	private RestHighLevelClient elasticsearchClient;

	private static final String index = "user";

	private static final SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
	private static final SearchRequest rq = new SearchRequest(index);

	@Before
	public  void before() {
		elasticsearchClient=new RestHighLevelClient(RestClient.builder(
                HttpHost.create("localhost:1365")
                ));
		searchSourceBuilder.from(0).size(5000);// 设置查询起始,截止下标。不设置默认查询10条数据
	}

	public static void after() {
	}

	public void printBucket(List<? extends Terms.Bucket> buckList) {
		for (Terms.Bucket bucket : buckList) {
			System.out.println(bucket.getKey());
			System.out.println(bucket.getDocCount());
			System.out.println(bucket.getDocCountError());
			System.out.println("-------------------------------------------------");
		}
	}

	// 简单分组
	@Test
	public void test1() throws IOException {
		TermsAggregationBuilder aggregationBuilder = AggregationBuilders.terms("ct").field("country.keyword");

		searchSourceBuilder.aggregation(aggregationBuilder);
		rq.source(searchSourceBuilder);
		SearchResponse srr = elasticsearchClient.search(rq, RequestOptions.DEFAULT);

		Aggregations aggregations = srr.getAggregations();

		ParsedStringTerms aga = aggregations.get("ct");
		printBucket(aga.getBuckets());
	}

	// 分组求AVG
	@Test
	public void test2() throws IOException {
		TermsAggregationBuilder teamAgg = AggregationBuilders.terms("team").field("country.keyword");

		AvgAggregationBuilder avgAggregationBuilder = AggregationBuilders.avg("user_Age").field("userAge");

		teamAgg.subAggregation(avgAggregationBuilder);
		searchSourceBuilder.aggregation(teamAgg);
		rq.source(searchSourceBuilder);
		SearchResponse srr = elasticsearchClient.search(rq, RequestOptions.DEFAULT);

		Terms userAgg = srr.getAggregations().get("team");

		for (Terms.Bucket bucket : userAgg.getBuckets()) {
			Avg Ua_avg = bucket.getAggregations().get("user_Age");

			System.out.println("分组名称\t\t" + bucket.getKey() + "分组个数\t\t" + bucket.getDocCount() + "\t\t"
					+ Ua_avg.getName() + "\t\t" + Ua_avg.getType() + "\t\t" + Ua_avg.getValueAsString());
		}
	}

	// 分组求多个聚合
	@Test
	public void test3() throws IOException {
		TermsAggregationBuilder teamAgg = AggregationBuilders.terms("team").field("country.keyword");

		AvgAggregationBuilder avgAggregationBuilder = AggregationBuilders.avg("user_Age_avg").field("userAge");
		SumAggregationBuilder sumAggregationBuilder = AggregationBuilders.sum("user_Age_sum").field("userAge");
		MinAggregationBuilder minAggregationBuilder = AggregationBuilders.min("user_Age_min").field("userAge");
		MaxAggregationBuilder maxAggregationBuilder = AggregationBuilders.max("user_Age_max").field("userAge");

		teamAgg.subAggregation(avgAggregationBuilder).subAggregation(sumAggregationBuilder)
				.subAggregation(minAggregationBuilder).subAggregation(maxAggregationBuilder);

		searchSourceBuilder.aggregation(teamAgg);
		rq.source(searchSourceBuilder);
		SearchResponse srr = elasticsearchClient.search(rq, RequestOptions.DEFAULT);

		Terms userAgg = srr.getAggregations().get("team");

		for (Terms.Bucket bucket : userAgg.getBuckets()) {
			Avg Ua_avg = bucket.getAggregations().get("user_Age_avg");
			Sum Ua_sum = bucket.getAggregations().get("user_Age_sum");
			Min Ua_min = bucket.getAggregations().get("user_Age_min");
			Max Ua_max = bucket.getAggregations().get("user_Age_max");

			System.out.println("分组名称\t\t" + bucket.getKey() + "\t\t个数\t\t" + bucket.getDocCount() + "\t\t"
					+ Ua_avg.getName() + "\t\t" + Ua_avg.getType() + "\t\t" + Ua_avg.getValueAsString());

			System.out.println("------------------------------------");

			System.out.println("分组名称\t\t" + bucket.getKey() + "\t\t个数\t\t" + bucket.getDocCount() + "\t\t"
					+ Ua_sum.getName() + "\t\t" + Ua_sum.getType() + "\t\t" + Ua_sum.getValueAsString());
			System.out.println("------------------------------------");

			System.out.println("分组名称\t\t" + bucket.getKey() + "\t\t个数\t\t" + bucket.getDocCount() + "\t\t"
					+ Ua_min.getName() + "\t\t" + Ua_min.getType() + "\t\t" + Ua_min.getValueAsString());
			System.out.println("------------------------------------");

			System.out.println("分组名称\t\t" + bucket.getKey() + "\t\t个数\t\t" + bucket.getDocCount() + "\t\t"
					+ Ua_max.getName() + "\t\t" + Ua_max.getType() + "\t\t" + Ua_max.getValueAsString());
		}
	}

	/***
	 * 分组加条件过滤
	 * 
	 * @see https://www.elastic.co/guide/en/elasticsearch/client/java-api/current/_bucket_aggregations.html
	 */
	@Test
	public void test5() throws IOException {
		TermsAggregationBuilder teamAgg = AggregationBuilders.terms("team").field("country.keyword");

		FilterAggregationBuilder country_filter = AggregationBuilders.filter("country_filter",
				QueryBuilders.matchQuery("country", "大虾"));

		teamAgg.subAggregation(country_filter);
		searchSourceBuilder.aggregation(teamAgg);
		rq.source(searchSourceBuilder);
		SearchResponse srr = elasticsearchClient.search(rq, RequestOptions.DEFAULT);

		Terms userAgg = srr.getAggregations().get("team");

		for (Terms.Bucket bucket : userAgg.getBuckets()) {
			System.out.println(bucket.getKey() + "\t\t" + bucket.getDocCount() + "\t\t");
			Filter filter = bucket.getAggregations().get("country_filter");
			System.out.println(filter.getName() + "\t\t" + filter.getType() + "\t\t" + filter.getDocCount());
			System.out.println("-----------------------------------");
		}
	}

	@Test
	public void test6() throws IOException {
		TermsAggregationBuilder teamAgg = AggregationBuilders.terms("team").field("country.keyword").size(1000);

		AvgAggregationBuilder avgAggregationBuilder = AggregationBuilders.avg("user_Age_avg").field("userAge");

		FilterAggregationBuilder country_filter = AggregationBuilders.filter("age_filter",
				QueryBuilders.rangeQuery("userAge").gt(15));

		teamAgg.subAggregation(avgAggregationBuilder);
		teamAgg.subAggregation(country_filter);

		searchSourceBuilder.aggregation(teamAgg);
		rq.source(searchSourceBuilder);
		SearchResponse srr = elasticsearchClient.search(rq, RequestOptions.DEFAULT);

		Terms userAgg = srr.getAggregations().get("team");

		for (Terms.Bucket bucket : userAgg.getBuckets()) {
			Avg Ua_avg = bucket.getAggregations().get("user_Age_avg");
			Filter filter = bucket.getAggregations().get("age_filter");

			System.out.println(bucket.getKey() + "\t\t" + filter.getName() + "\t\t" + filter.getDocCount());
			System.out.println(bucket.getKey() + "\t\t" + Ua_avg.getName() + "\t\t" + Ua_avg.getValue());
		}
	}

	/****
	 * @author zhanchaohan
	 * @throws IOException
	 * @see https://www.elastic.co/guide/cn/elasticsearch/guide/current/_filtering_queries.html
	 *      求出userAge>50的userAge平均数
	 */
	@Test
	public void test7() throws IOException {
		AvgAggregationBuilder avgAggregationBuilder = AggregationBuilders.avg("user_Age_avg").field("userAge");

		searchSourceBuilder.aggregation(avgAggregationBuilder);

		searchSourceBuilder.query(QueryBuilders.rangeQuery("userAge").gt(50));

		rq.source(searchSourceBuilder);
		SearchResponse srr = elasticsearchClient.search(rq, RequestOptions.DEFAULT);

		Avg avg = srr.getAggregations().get("user_Age_avg");

		System.out.println(
				avg.getName() + "\t\t" + avg.getType() + "\t\t" + avg.getValue() + "\t\t" + avg.getValueAsString());
	}

	/***
	 * test7()基础上添加条件分组根据country分组，求userAge>50的平均数
	 * 
	 * @author zhanchaohan
	 */
	@Test
	public void test8() throws IOException {
		TermsAggregationBuilder teamAgg = AggregationBuilders.terms("team").field("country.keyword").size(1000);

		AvgAggregationBuilder avgAggregationBuilder = AggregationBuilders.avg("user_Age_avg").field("userAge");

		teamAgg.subAggregation(avgAggregationBuilder);

		searchSourceBuilder.aggregation(teamAgg);
		searchSourceBuilder.query(QueryBuilders.rangeQuery("userAge").gt(50));

		rq.source(searchSourceBuilder);
		SearchResponse srr = elasticsearchClient.search(rq, RequestOptions.DEFAULT);

		Terms userAgg = srr.getAggregations().get("team");
		for (Terms.Bucket bucket : userAgg.getBuckets()) {
			Avg Ua_avg = bucket.getAggregations().get("user_Age_avg");

			System.out.println(bucket.getKey() + "\t\t" + Ua_avg.getName() + "\t\t" + Ua_avg.getValue());
		}
	}
}
