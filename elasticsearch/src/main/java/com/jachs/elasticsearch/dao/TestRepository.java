package com.jachs.elasticsearch.dao;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Component;

import com.jachs.elasticsearch.entity.IMDBProgram;

import java.util.List;

@Component
public interface TestRepository extends ElasticsearchRepository<IMDBProgram,String> {
    List<IMDBProgram> findByEpisode(Long eps);
}
