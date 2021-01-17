package com.jachs.elasticsearch.boot.rest;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.Date;

import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexRequestBuilder;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.io.stream.InputStreamStreamInput;
import org.elasticsearch.common.xcontent.XContentType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;

import com.google.gson.Gson;
import com.jachs.elasticsearch.ElasticsearchApplication;
import com.jachs.elasticsearch.entity.BlogModel;

/****
 * 
 * @author zhanchaohan
 *
 */
@SpringBootTest(classes = ElasticsearchApplication.class)
public class ElasticsearchRestTemplateTest {
//	@Autowired
//	private ElasticsearchRestTemplate restTemplate;
	@Autowired
	private RestHighLevelClient elasticsearchClient;

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
