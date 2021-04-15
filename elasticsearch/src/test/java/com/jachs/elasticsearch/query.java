package com.jachs.elasticsearch;

import java.io.FileNotFoundException;
import java.io.IOException;

import org.elasticsearch.action.get.GetRequestBuilder;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.index.query.DisMaxQueryBuilder;
import org.elasticsearch.index.query.PrefixQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.RangeQueryBuilder;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.BucketOrder;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.bucket.terms.Terms.Bucket;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
import org.elasticsearch.search.aggregations.metrics.avg.Avg;
import org.elasticsearch.search.aggregations.metrics.avg.AvgAggregationBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class query {
	private TransportClient client;
	 private String EsIndex="etest";
	    private String EsType="doc";

	@Before
	public void init() throws FileNotFoundException, IOException {
		ElasticConfig ec = new ElasticConfig();
		client = ec.init();
	}

	@After
	public void destroy() {
		if (client != null) {
			client.close();
		}
	}

	// 单条查询
	@Test
	public void test() {
		GetRequestBuilder getRequestBuilder = client.prepareGet(EsIndex, EsType, "Ps69hGkBfH7K1RnigH-e");
		GetResponse getResponse = getRequestBuilder.execute().actionGet();
		System.out.println(getResponse.getSourceAsString());
	}

	@Test
	public void test1() {
		GetRequestBuilder getRequestBuilder = client.prepareGet(EsIndex, EsType, "Ps69hGkBfH7K1RnigH-e");
		GetResponse getResponse = getRequestBuilder.execute().actionGet();
		System.out.println(getResponse.getSourceAsString());
	}

	// 全查询
	@Test
	public void test2() {
		// SearchRequestBuilder
		// searchRequestBuilder=client.prepareSearch("index");
		SearchRequestBuilder searchRequestBuilder = client.prepareSearch(EsIndex, "bools");// 多个Index
		SearchResponse searchResponse = searchRequestBuilder.execute().actionGet();
		for (SearchHit searchHit : searchResponse.getHits().getHits()) {
			System.out.println(searchHit.getSourceAsString());
		}
	}

	/*
	 * 使用QueryBuilder termQuery("key", obj) 完全匹配 termsQuery("key", obj1, obj2..)
	 * 一次匹配多个值 matchQuery("key", Obj) 单个匹配, field不支持通配符, 前缀具高级特性
	 * multiMatchQuery("text", "field1", "field2"..); 匹配多个字段, field有通配符忒行
	 * matchAllQuery(); 匹配所有文件
	 * 
	 * ==，!=
	 */
	@Test
	public void test3() {
		// TermQueryBuilder termQuery = QueryBuilders.termQuery("name",
		// "zhangsan");//条件查询
		// MatchQueryBuilder matchQuery = QueryBuilders.matchQuery("name",
		// "zhangsan");//匹配查询
		PrefixQueryBuilder prefixQuery = QueryBuilders.prefixQuery("name", "zhangsan");// 前缀查询
		SearchRequestBuilder searchRequestBuilder = client.prepareSearch(EsIndex);
		DisMaxQueryBuilder disMaxQueryBuilder = QueryBuilders.disMaxQuery()
				.add(QueryBuilders.matchQuery("name", "zhangsan")).add(QueryBuilders.matchQuery("gent", "男"));// 多查询最大排前

		SearchResponse searchResponse = searchRequestBuilder
				// .setQuery(termQuery)
				// .setQuery(matchQuery)
				// .setQuery(prefixQuery)
				.setQuery(disMaxQueryBuilder).execute().actionGet();
		for (SearchHit searchHit : searchResponse.getHits().getHits()) {
			System.out.println(searchHit.getSourceAsString());
		}
	}

	/*****
	 * ==,!= <,> <=,>= ?-?
	 */
	@Test
	public void test4() {
		// BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();
		// boolQuery.must(QueryBuilders.termQuery("name", "zhangsan0"))
		// .must(QueryBuilders.termQuery("gent",
		// "男"));//多个条件同时满足,&&。must(==),mustNot(!=)

		// BoolQueryBuilder boolQuery2 = QueryBuilders.boolQuery();
		// boolQuery2.should(QueryBuilders.termQuery("name", "zhangsan0"))
		// .should(QueryBuilders.termQuery("name", "zhangsan10"));//一个满足则满足,||

		// RangeQueryBuilder rangeQueryBuilder =
		// QueryBuilders.rangeQuery("age").gt("5");//>5
		// RangeQueryBuilder rangeQueryBuilder =
		// QueryBuilders.rangeQuery("age").lt("5");//<5
		RangeQueryBuilder rangeQueryBuilder1 = QueryBuilders.rangeQuery("age").from(3).to(6);// 3-6所有

		SearchRequestBuilder searchRequestBuilder = client.prepareSearch(EsIndex);

		SearchResponse searchResponse = searchRequestBuilder
				// .setQuery(boolQuery)
				// .setQuery(boolQuery2)
				// .setQuery(rangeQueryBuilder)
				.setQuery(rangeQueryBuilder1).addSort("age", SortOrder.DESC)// 排序
				.execute().actionGet();
		for (SearchHit searchHit : searchResponse.getHits().getHits()) {
			System.out.println(searchHit.getSourceAsString());
		}
	}

	/****
	 * 单分组
	 * https://www.elastic.co/guide/en/elasticsearch/client/java-api/current/_bucket_aggregations.html
	 */
	@Test
	public void test5() {
		SearchRequestBuilder searchRequestBuilder = client.prepareSearch(EsIndex);
		TermsAggregationBuilder teamAgg = AggregationBuilders.terms("region_s").field("region.keyword");// 分组

		SearchResponse searchResponse = searchRequestBuilder.addAggregation(teamAgg).execute().actionGet();

		Aggregations aggregations = searchResponse.getAggregations();
		Terms terms = aggregations.get("region_s");
		for (Bucket bucket : terms.getBuckets()) {
			System.out.println(bucket.getKey() + "\t\t" + bucket.getDocCount());
		}
	}

	// 多个分组
	@Test
	public void test6() {
		SearchRequestBuilder searchRequestBuilder = client.prepareSearch(EsIndex);
		TermsAggregationBuilder teamAgg = AggregationBuilders.terms("region_s").field("region.keyword");// 分组
		TermsAggregationBuilder gentAgg = AggregationBuilders.terms("gent_s").field("gent.keyword")
				.order(BucketOrder.count(true));// 分组,排序

		SearchResponse searchResponse = searchRequestBuilder.addAggregation(teamAgg.subAggregation(gentAgg)).execute()
				.actionGet();

		Aggregations aggregations = searchResponse.getAggregations();
		Terms terms = aggregations.get("region_s");
		Terms terms2;
		for (Terms.Bucket bucket : terms.getBuckets()) {
			System.out.println("地区=" + bucket.getKey());
			terms2 = bucket.getAggregations().get("gent_s");
			for (Terms.Bucket bucket2 : terms2.getBuckets()) {
				System.out.println("性别=" + bucket2.getKey() + ";个数=" + bucket2.getDocCount());
			}
		}
	}

	// 取分组最大值,最小值,平均值
	// https://www.elastic.co/guide/en/elasticsearch/client/java-api/current/_metrics_aggregations.html
	@Test
	public void test7() {
		SearchRequestBuilder searchRequestBuilder = client.prepareSearch(EsIndex);
		TermsAggregationBuilder teamAgg = AggregationBuilders.terms("region_s").field("region.keyword");// 分组
//		MaxAggregationBuilder ageAgg = AggregationBuilders.max("age_s").field("age");
//		MinAggregationBuilder ageAgg=AggregationBuilders.min("age_s").field("age");
		AvgAggregationBuilder avgAgg = AggregationBuilders.avg("age_s").field("age");
		SearchResponse searchResponse = searchRequestBuilder.addAggregation(teamAgg.subAggregation(avgAgg)).execute()
				.actionGet();

		Aggregations aggregations = searchResponse.getAggregations();
		Terms terms = aggregations.get("region_s");
//		Max max;
//		Min min;
		Avg avg;
		for (Terms.Bucket bucket : terms.getBuckets()) {
			System.out.println("地区=" + bucket.getKey());
//			max = bucket.getAggregations().get("age_s");
//			System.out.println("最大数据名称="+max.getName());
//			System.out.println("最大数据值="+max.getValue());

//			min=bucket.getAggregations().get("age_s");
//			System.out.println("最小数据名称="+min.getName());
//			System.out.println("最小数据值="+min.getValue());

			avg = bucket.getAggregations().get("age_s");
			System.out.println("平均数据名称=" + avg.getName());
			System.out.println("平均数据值=" + avg.getValue());
		}
	}
}
