package com.jachs.elasticsearch;

import java.io.FileNotFoundException;
import java.io.IOException;

import org.elasticsearch.action.delete.DeleteRequestBuilder;
import org.elasticsearch.client.transport.TransportClient;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/****
 * 
 * @author zhanchaohan
 *
 */
public class delete {
    
    private TransportClient client;

	@Before
	public void init() throws FileNotFoundException, IOException {
		ElasticConfig ec=new ElasticConfig();
    	client=ec.init();
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
