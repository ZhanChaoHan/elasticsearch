package com.jachs.elasticsearch.boot;

import java.util.Random;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.jachs.elasticsearch.dao.TestRepository;
import com.jachs.elasticsearch.entity.IMDBProgram;

/**
 * @author zhanchaohan
 * 
 */
@SpringBootTest()
public class DaoTest {
    @Autowired
    private TestRepository testRepository;
    
    /***
     * 单条添加
     */
    @Test
    public void tt() {
        IMDBProgram ip=new IMDBProgram();
        ip.setDeleteFlag ( "deleteFlag" );
        ip.setDirectWeight ( 23 );
        ip.setEpisode ( 47 );
        ip.setId ( 26L );
        ip.setParagraph ( 26 );
        ip.setProgramId ( 175L );
        ip.setSourceType ( "sourceType" );
        ip.setSubprogramId ( 587L );
        testRepository.save ( ip );
    }
    @Test
    public void loopData() {
    	for (int k = 0; k < 50; k++) {
	        IMDBProgram ip=new IMDBProgram();
	        ip.setDeleteFlag ( "deleteFlag"+k );
	        ip.setDirectWeight ( 23 );
	        ip.setEpisode ( 47 );
	        ip.setId ( new Random().nextLong());
	        ip.setParagraph ( 26 );
	        ip.setProgramId ( 175L );
	        ip.setSourceType ( "sourceType" );
	        ip.setSubprogramId ( 587L );
	        testRepository.save ( ip );
    	}
    }
    
}
