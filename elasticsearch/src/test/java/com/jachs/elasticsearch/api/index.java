package com.jachs.elasticsearch.api;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.elasticsearch.common.Strings;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.junit.Test;

/**
 * 定义es index格式
 * @author zhanchaohan
 * @see https://www.elastic.co/guide/en/elasticsearch/client/java-api/current/java-docs-index.html
 */
public class index {
    @Test
    public void testIndex() throws IOException {
        //最简单粗暴的方式
        String json = "{" +
                "\"user\":\"kimchy\"," +
                "\"postDate\":\"2013-01-30\"," +
                "\"message\":\"trying out Elasticsearch\"" +
            "}";
        //通过Map
        Map<String, Object> mapJson = new HashMap<String, Object>();
        mapJson.put("user","kimchy");
        mapJson.put("postDate",new Date());
        mapJson.put("message","trying out Elasticsearch");
        //Jackson实现
//        ObjectMapper mapper = new ObjectMapper(); // create once, reuse
//        byte[] json = mapper.writeValueAsBytes(yourbeaninstance);
        
        //官方给出的对象实现
        XContentBuilder builder = jsonBuilder();
        String builderJson = Strings.toString(builder);
        System.out.println (  builderJson);
    }

    private XContentBuilder jsonBuilder () throws IOException {
        XContentBuilder builder= XContentFactory.jsonBuilder()
                .startObject()
                .startObject("properties")
                .startObject("user")
                .field("type","string")
                .field("index","not_analyzed")
                .endObject()
                .startObject("postDate")
                .field("type","date")
                .endObject()
                .startObject("message")
                .field("type","string")
                .field("index","not_analyzed")
                .endObject()
                .startObject("address")
                .field("type","string")
                .endObject()
                .startObject("车牌号")
                .field("type","string")
                .field("index","ik")
                .endObject()
                .endObject()
                .endObject();
        return builder;
    }
}
