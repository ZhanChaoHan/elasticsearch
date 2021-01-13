package com.jachs.elasticsearch.api;

import java.net.InetAddress;
import java.net.UnknownHostException;

import org.elasticsearch.action.delete.DeleteRequestBuilder;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.TransportAddress;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/applicationContext-elasticsearch.xml" })
public class delete {
    @Value( "${cluster.name}" )
    private String ClusterName;
    @Value( "${network.host}" )
    private String EsIp;
    @Value( "${tcp.port}" )
    private String EsPort;
    @Value( "${elasticsearch.index}" )
    private String EsIndex;
    @Value( "${elasticsearch.index.type}" )
    private String EsType;
    
    
    private TransportClient client;

	@Before
	public void init() {
	    Settings settings = Settings.builder ().put ( "cluster.name", ClusterName ).build ();
        try {
            client = new PreBuiltTransportClient ( settings ).addTransportAddress (
                    new TransportAddress ( InetAddress.getByName ( EsIp ), Integer.parseInt ( EsPort ) ) );
        }
        catch ( UnknownHostException e ) {
            e.printStackTrace ();
        }
	}

	@After
	public void destroy() {
		if (client != null) {
			client.close();
		}
	}

	@Test
	public void delete() {
		DeleteRequestBuilder deleteRequestBuilder=client.prepareDelete();
		deleteRequestBuilder.setId("");
		deleteRequestBuilder.execute();
	}
}
