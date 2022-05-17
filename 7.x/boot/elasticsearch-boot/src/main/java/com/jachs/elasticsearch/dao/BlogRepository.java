package com.jachs.elasticsearch.dao;

import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Component;

import com.jachs.elasticsearch.entity.BlogModel;

/**
 * @author zhanchaohan
 * 
 */
@Component
public interface BlogRepository extends ElasticsearchRepository<BlogModel,String> {
	public BlogModel findByIdAndTitle(String id,String title);

	@Query("{\"match_phrase\":{\"title\":\"标题\"}}")
	public BlogModel findByQueryName();
	@Query("{\"match_phrase\":{\"title\":\"?0\"}}")
	public BlogModel findByQueryName(String title);
}
