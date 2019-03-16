package jachs.elasticsearch;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Properties;

import org.elasticsearch.action.get.GetRequestBuilder;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.TransportAddress;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class query {
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
	//单条查询
	@Test
	public void test(){
		GetRequestBuilder getRequestBuilder=client.prepareGet(EsIndex, EsType,"Ps69hGkBfH7K1RnigH-e");
		GetResponse getResponse=getRequestBuilder.execute().actionGet();
		System.out.println(getResponse.getSourceAsString());
	}
	@Test
	public void test1(){
		GetRequestBuilder getRequestBuilder=client.prepareGet(EsIndex, EsType,"Ps69hGkBfH7K1RnigH-e");
		GetResponse getResponse=getRequestBuilder.execute().actionGet();
		System.out.println(getResponse.getSourceAsString());
	}
	//全查询
	@Test
	public void test2(){
		SearchRequestBuilder searchRequestBuilder=client.prepareSearch("index");
		SearchResponse searchResponse=searchRequestBuilder.execute().actionGet();
		for (SearchHit searchHit : searchResponse.getHits().getHits()) {
			System.out.println(searchHit.getSourceAsString());
		}
	}
	
}
