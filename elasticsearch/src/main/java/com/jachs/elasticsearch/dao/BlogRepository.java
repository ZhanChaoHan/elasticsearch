package com.jachs.elasticsearch.dao;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Component;

import com.jachs.elasticsearch.entity.BlogModel;


@Component
public interface BlogRepository extends ElasticsearchRepository<BlogModel,String> {
}
