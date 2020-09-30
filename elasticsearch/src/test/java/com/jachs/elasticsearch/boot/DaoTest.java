package com.jachs.elasticsearch.boot;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

import com.jachs.elasticsearch.dao.TestRepository;
import com.jachs.elasticsearch.entity.IMDBProgram;

/**
 * @author zhanchaohan
 * 
 */
@SpringBootTest
@ContextConfiguration( locations = { "/applicationContext-elasticsearch.xml" } )
public class DaoTest {
    @Autowired
    private TestRepository testRepository;
    
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
}
