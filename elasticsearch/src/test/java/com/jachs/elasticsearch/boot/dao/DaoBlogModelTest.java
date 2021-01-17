package com.jachs.elasticsearch.boot.dao;

import java.util.Date;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.jachs.elasticsearch.ElasticsearchApplication;
import com.jachs.elasticsearch.dao.BlogRepository;
import com.jachs.elasticsearch.entity.BlogModel;

/**
 * @author zhanchaohan
 * 
 */
@SpringBootTest(classes =ElasticsearchApplication.class )
public class DaoBlogModelTest {
	@Autowired
	private BlogRepository blogRepository;
	
	@Test
	public void add() {
		BlogModel bm=new BlogModel();
		
		bm.setId("as");
		bm.setTitle("标题");
		bm.setTime(new Date());
		blogRepository.save(bm);
	}
}
