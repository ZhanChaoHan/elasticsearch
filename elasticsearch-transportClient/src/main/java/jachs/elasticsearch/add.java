package jachs.elasticsearch;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.elasticsearch.action.index.IndexRequestBuilder;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.TransportAddress;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.junit.After;
import org.junit.Before;
import org.junit.jupiter.api.Test;

public class add {
	private TransportClient client;
	private String EsIndex;
	private String EsType;
	@Before
	public void init() {
		Properties properties = new Properties();
		try {
			properties.load(getClass().getResourceAsStream("config.properties"));
			String ClusterName = properties.getProperty("ClusterName");
			String EsIp = properties.getProperty("EsIp");
			String EsPort = properties.getProperty("EsPort");
			EsIndex = properties.getProperty("EsIndex");
			EsType = properties.getProperty("EsType");
			Settings settings = Settings.builder().put("cluster.name", ClusterName).build();
			try {
				client = new PreBuiltTransportClient(settings).addTransportAddress(new TransportAddress(
						InetAddress.getByName(EsIp), Integer.parseInt(EsPort)));
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
		if(client!=null) {
			client.close();
		}
	}

	@Test
	public void add() {
		Map<String, Object>data=new HashMap<String, Object>();
		data.put("name", "zhangsan");
		data.put("age", 18);
		IndexRequestBuilder indexRequestBuilder= client.prepareIndex(EsIndex, EsType);
		indexRequestBuilder.setSource(data);
//		client.prepareIndex(index, type, id)
		indexRequestBuilder.execute();
	}
}
