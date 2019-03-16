package jachs.elasticsearch;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.elasticsearch.action.ActionFuture;
import org.elasticsearch.action.bulk.BulkItemResponse;
import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexRequestBuilder;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.TransportAddress;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;


public class add {
	private TransportClient client;
	private String EsIndex;
	private String EsType;

	@Before
	public void init() {
		Properties properties = new Properties();
		try {
			properties.load(getClass().getResourceAsStream("/config.properties"));
			String ClusterName = properties.getProperty("ClusterName");
			String EsIp = properties.getProperty("EsIp");
			String EsPort = properties.getProperty("EsPort");
			EsIndex = properties.getProperty("EsIndex");
			EsType = properties.getProperty("EsType");
			Settings settings = Settings.builder().put("cluster.name", ClusterName).build();
			try {
				client = new PreBuiltTransportClient(settings).addTransportAddress(
						new TransportAddress(InetAddress.getByName(EsIp), Integer.parseInt(EsPort)));
			} catch (UnknownHostException e) {
				e.printStackTrace();
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@After
	public void destroy() {
		if (client != null) {
			client.close();
		}
	}

	// 单条随机ID
	@Test
	public void add() {
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("name", "zhangsan");
		data.put("age", 18);
		IndexRequestBuilder indexRequestBuilder = client.prepareIndex(EsIndex, EsType);
		indexRequestBuilder.setSource(data);
		indexRequestBuilder.execute();
	}

	// 单条制定ID
	@Test
	public void add1() {
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("name", "zhangsan");
		data.put("age", 18);
		IndexRequestBuilder indexRequestBuilder = client.prepareIndex(EsIndex, EsType, "id01");
		indexRequestBuilder.setSource(data);
		indexRequestBuilder.execute();
	}

	// 单条
	@Test
	public void add2() {
		IndexRequestBuilder indexRequestBuilder = client.prepareIndex(EsIndex, EsType);

		indexRequestBuilder.setSource("name", "zhangsan", "age", "18");
		indexRequestBuilder.execute();
	}

	// 多条
	@Test
	public void add3() {
		Map<String, Object>map=new HashMap<String, Object>();
		map.put("name", "zhangsan");
		map.put("age", 18);
		
		Map<String, Object>map1=new HashMap<String, Object>();
		map1.put("name", "lisi");
		map1.put("age", 19);
		
		BulkRequestBuilder bulkRequestBuilder=client.prepareBulk();
		bulkRequestBuilder.add(client.prepareIndex(EsIndex, EsType).setSource(map));
		bulkRequestBuilder.add(client.prepareIndex(EsIndex, EsType).setSource(map1));
		BulkResponse bulkResponse=bulkRequestBuilder.execute().actionGet();
		Iterator<BulkItemResponse>bulkItemResponse= bulkResponse.iterator();
		while(bulkItemResponse.hasNext()){
			BulkItemResponse itemResponse=bulkItemResponse.next();
			System.out.println(itemResponse.getItemId());
		}
	}
}
