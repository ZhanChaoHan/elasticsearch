package com.jachs.elasticsearch.boot;

import org.springframework.context.annotation.Bean;
import org.springframework.data.elasticsearch.client.ClientConfiguration;
import org.springframework.data.elasticsearch.client.reactive.ReactiveElasticsearchClient;
import org.springframework.data.elasticsearch.client.reactive.ReactiveRestClients;

/**
 * Reactive REST Client
 * @author zhanchaohan
 * @see 
 * 
 */
static class Config {

    @Bean
    ReactiveElasticsearchClient client() {

      ClientConfiguration clientConfiguration = ClientConfiguration.builder()   
        .connectedTo("localhost:9200", "localhost:9291")
        .withWebClientConfigurer(webClient -> {                                 
          ExchangeStrategies exchangeStrategies = ExchangeStrategies.builder()
              .codecs(configurer -> configurer.defaultCodecs()
                  .maxInMemorySize(-1))
              .build();
          return webClient.mutate().exchangeStrategies(exchangeStrategies).build();
         })
        .build();

      return ReactiveRestClients.create(clientConfiguration);
    }
  }

//  // ...
//
//  Mono<IndexResponse> response = client.index(request ->
//
//    request.index("spring-data")
//      .type("elasticsearch")
//      .id(randomID())
//      .source(singletonMap("feature", "reactive-client"))
//      .setRefreshPolicy(IMMEDIATE);
//  );