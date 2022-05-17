package com.jachs.elasticsearch;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutionException;

import org.elasticsearch.action.ActionFuture;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexResponse;
import org.elasticsearch.action.admin.indices.stats.IndexStats;
import org.elasticsearch.action.admin.indices.stats.IndicesStatsRequest;
import org.elasticsearch.action.admin.indices.stats.IndicesStatsResponse;
import org.elasticsearch.client.IndicesAdminClient;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.cluster.node.DiscoveryNode;
import org.elasticsearch.common.Strings;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * 定义es index格式
 * 
 * @author zhanchaohan
 * @see https://www.elastic.co/guide/en/elasticsearch/client/java-api/current/java-docs-index.html
 */
public class index {
	private TransportClient client;

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

	@Test
	public void testIndex() throws IOException {
		// 最简单粗暴的方式
		String json = "{" + "\"user\":\"kimchy\"," + "\"postDate\":\"2013-01-30\","
				+ "\"message\":\"trying out Elasticsearch\"" + "}";
		// 通过Map
		Map<String, Object> mapJson = new HashMap<String, Object>();
		mapJson.put("user", "kimchy");
		mapJson.put("postDate", new Date());
		mapJson.put("message", "trying out Elasticsearch");
		// Jackson实现
//        ObjectMapper mapper = new ObjectMapper(); // create once, reuse
//        byte[] json = mapper.writeValueAsBytes(yourbeaninstance);

		// 官方给出的对象实现
		XContentBuilder builder = jsonBuilder();
		String builderJson = Strings.toString(builder);
		System.out.println(builderJson);
	}

	private XContentBuilder jsonBuilder() throws IOException {
		XContentBuilder builder = XContentFactory.jsonBuilder().startObject().startObject("properties")
				.startObject("user").field("type", "string").field("index", "not_analyzed").endObject()
				.startObject("postDate").field("type", "date").endObject().startObject("message")
				.field("type", "string").field("index", "not_analyzed").endObject().startObject("address")
				.field("type", "string").endObject().startObject("车牌号").field("type", "string").field("index", "ik")
				.endObject().endObject().endObject();
		return builder;
	}

	/***
	 * 获取全部节点
	 */
	@Test
	public void discoveryNode() {
		List<DiscoveryNode> dnList = client.listedNodes();
		for (DiscoveryNode discoveryNode : dnList) {
			System.out.println(discoveryNode.getHostName());
		}
	}

	/***
	 * 获取索引
	 */
	@Test
	public void getIndex() {
		ActionFuture<IndicesStatsResponse> isr = client.admin().indices().stats(new IndicesStatsRequest().all());
		IndicesAdminClient indicesAdminClient = client.admin().indices();
		Map<String, IndexStats> indexStatsMap = isr.actionGet().getIndices();
		Set<String> set = isr.actionGet().getIndices().keySet();

		for (String key : set) {
			System.out.println("索引名称:" + key);
		}
	}

	/***
	 * 删除全部索引
	 * @throws ExecutionException 
	 * @throws InterruptedException 
	 */
	@Test
	public void deleteAllIndex() throws InterruptedException, ExecutionException {
		ActionFuture<IndicesStatsResponse> isr = client.admin().indices().stats(new IndicesStatsRequest().all());
		Set<String> set = isr.actionGet().getIndices().keySet();

		for (String key : set) {
			DeleteIndexResponse deleteIndexResponse= client.admin().indices().prepareDelete(key).execute().get();
		
			System.out.println(key+":\t"+deleteIndexResponse.isAcknowledged());
		}

	}

	/***
	 * 删除指定索引
	 */
	@Test
	public void deleteIndex() {
		ActionFuture<DeleteIndexResponse> dResponse = client.admin().indices()
				.delete(new DeleteIndexRequest("ui_template"));
		DeleteIndexResponse deleteIndexResponse = dResponse.actionGet();

		System.out.println(deleteIndexResponse.isAcknowledged());
	}
}
