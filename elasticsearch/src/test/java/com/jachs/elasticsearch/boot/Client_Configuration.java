package com.jachs.elasticsearch.boot;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.springframework.data.elasticsearch.client.ClientConfiguration;
import org.springframework.data.elasticsearch.client.ClientConfiguration.TerminalClientConfigurationBuilder;
import org.springframework.http.HttpHeaders;


/**
 * @author zhanchaohan
 * @see 
 * 
 */
public class Client_Configuration {
    public void config() {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("some-header", "on every request");              

        TerminalClientConfigurationBuilder clientConfiguration = ClientConfiguration.builder()
          .connectedTo("localhost:9200", "localhost:9291")
          .usingSsl ()    //开启ssl协议    
          .withProxy("localhost:8888")                                          
          .withPathPrefix("ela")                                                
          .withConnectTimeout(10)//默认10秒                        
          .withSocketTimeout(5)//默认5秒                    
          .withDefaultHeaders(null)                                   
          .withBasicAuth("username", "username")//用户名，密码                                    
          .withHeaders(() -> {                                                  
            HttpHeaders headers = new HttpHeaders();
            headers.add("currentTime", LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
            return headers;
          });
        
    }
}
