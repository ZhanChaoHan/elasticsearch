package com.jachs.elasticsearch;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Properties;

import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.TransportAddress;
import org.elasticsearch.transport.client.PreBuiltTransportClient;

/**
 * @author zhanchaohan
 * 
 */
public class ElasticConfig {
	String ClusterName;
	String EsIp;
	String EsPort;
	
	
	public TransportClient init() throws FileNotFoundException, IOException {
		Properties properties=new Properties();
		properties.load(new FileReader(ElasticConfig.class.getResource("/elasticsearch.properties").getPath()));
		
		Settings settings = Settings.builder().put("cluster.name", properties.getProperty("ClusterName")).build();
		try {
			TransportClient client = new PreBuiltTransportClient(settings)
					.addTransportAddress(new TransportAddress(InetAddress.getByName(properties.getProperty("EsIp")), Integer.parseInt(properties.getProperty("EsPort"))));
		return client;
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		return null;
	}
}