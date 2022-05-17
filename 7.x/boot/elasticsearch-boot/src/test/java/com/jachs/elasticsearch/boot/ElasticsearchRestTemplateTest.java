package com.jachs.elasticsearch.boot;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;

import com.jachs.elasticsearch.ElasticsearchApplication;

/***
 * 
 * @author zhanchaohan
 *
 */
@SpringBootTest( classes = ElasticsearchApplication.class )
public class ElasticsearchRestTemplateTest {
	@Autowired
	@Qualifier(value = "erTemp")
	private ElasticsearchRestTemplate elasticsearchRestTemplate;
	
	
	private static final String INDEX="aaa";
	
	
	//创建索引
	@Test
	public void test1() {
		elasticsearchRestTemplate.createIndex(INDEX);
	}
	//判断索引是否存在
	@Test
	public void test2() {
		boolean exists=elasticsearchRestTemplate.indexExists(INDEX);
		System.out.println(exists);
	}
	//删除索引
	@Test
	public void test3() {
		System.out.println(elasticsearchRestTemplate.deleteIndex(INDEX));
	}
}
