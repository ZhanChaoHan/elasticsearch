package com.jachs.elasticsearch.boot.dao;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

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
	@Test
	public void loopAdd() throws ParseException {
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		List<BlogModel>bmList=new ArrayList<BlogModel>();
		for (int kk = 0; kk < 10; kk++) {
			BlogModel bm=new BlogModel();
			
			bm.setId(kk+"Id");
			bm.setTitle(new Random().nextInt()+"lkc");
			bm.setTime(sdf.parse("2021-01-"+kk+" 00:52:36"));
			bmList.add(bm);
		}
		blogRepository.saveAll(bmList);
	}
	@Test
	public void query1() {
		BlogModel bm=blogRepository.findByIdAndTitle("0Id","-480824872lkc");
		System.out.println(bm.toString());
	}
}
