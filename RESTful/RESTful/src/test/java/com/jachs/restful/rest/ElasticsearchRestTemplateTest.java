package com.jachs.restful.rest;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import org.apache.http.HttpHost;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.junit.Before;
import org.junit.Test;

/****
 * 
 * @author zhanchaohan
 *
 */
public class ElasticsearchRestTemplateTest {
//	@Autowired
//	private ElasticsearchRestTemplate restTemplate;
	private RestHighLevelClient elasticsearchClient;

	@Before
	public void init() {
		elasticsearchClient=new RestHighLevelClient(RestClient.builder(
                HttpHost.create("localhost:1365")
                ));
	}
	
	//批量导入数据
	@Test
	public void initScript() throws IOException {
		BufferedReader brs = new BufferedReader(
				new FileReader("C:\\Users\\Administrator.USER-20120726EP\\Desktop\\English\\dict\\book\\BEC_2.json"));
		BulkRequest br = new BulkRequest();
		
		while (brs.ready()) {
			IndexRequest ir=new IndexRequest("bec_2");//索引名称必须小写
			String jsonRead = brs.readLine();
//			System.out.println(jsonRead);
			ir.source(jsonRead, XContentType.JSON);
			
			br.add(ir);
		}
		elasticsearchClient.bulk(br, RequestOptions.DEFAULT);
		brs.close();
	}
	
}
