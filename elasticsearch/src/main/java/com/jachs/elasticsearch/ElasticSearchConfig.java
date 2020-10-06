package com.jachs.elasticsearch;

import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.TransportAddress;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import java.net.InetAddress;

/****
 * 自定义ElasticSearch配置
 * @author Administrator
 *
 */

/*
@Configuration
@PropertySource(value = "classpath:/elasticsearch.properties")
public class ElasticSearchConfig {
    private Logger logger  = LoggerFactory.getLogger(this.getClass());

    @Value("${network.host}")
    private String ip;
    @Value("${http.port}")
    private String port;
    @Value("${cluster.name}")
    private String clusterName;

    @Bean
    public TransportClient getTransportClient() {
        TransportClient transportClient = null;
        try {
            Settings settings = Settings.builder()
                    .put("cluster.name",clusterName)
                    .put("client.transport.sniff",true)
                    .build();
            transportClient = new PreBuiltTransportClient(settings);
            TransportAddress firstAddress = new TransportAddress(InetAddress.getByName(ip),Integer.parseInt(port));
            transportClient.addTransportAddress(firstAddress);
        }catch (Exception e){
            e.printStackTrace();
            logger.error("getTransportClient fail：" +  e.getMessage(),e);
        }
        return transportClient;
    }
}
*/