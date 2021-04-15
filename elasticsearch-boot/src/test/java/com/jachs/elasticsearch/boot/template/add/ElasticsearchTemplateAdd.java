package com.jachs.elasticsearch.boot.template.add;

import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.script.mustache.SearchTemplateRequestBuilder;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.jachs.elasticsearch.ElasticsearchApplication;

/**
 * @author zhanchaohan
 * 
 */
@SpringBootTest( classes = ElasticsearchApplication.class )
public class ElasticsearchTemplateAdd {
    @Autowired
    private TransportClient transportClient;
    
    @Test
    public void test() {
//        transportClient.prepareIndex ().
    }
}
