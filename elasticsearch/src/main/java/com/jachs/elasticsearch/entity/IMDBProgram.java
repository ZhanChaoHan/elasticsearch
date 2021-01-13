package com.jachs.elasticsearch.entity;


import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import lombok.NoArgsConstructor;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;

/**
 * @author zhanchaohan
 * 
 */
@Data
@NoArgsConstructor
@Document(indexName = "iprogram",indexStoreType = "fs")
public class IMDBProgram {

    @Id
    private Long id;

    @Field
    @JsonProperty("subprogram_id")
    private Long subprogramId;

    @Field
    @JsonProperty("program_id")
    private Long programId;

    @Field
    private Integer episode;

    @Field
    private Integer paragraph;

    @Field
    @JsonProperty("direct_weight")
    private Integer directWeight;

    @Field
    @JsonProperty("source_type")
    private String sourceType;

    @Field
    @JsonProperty("delete_flag")
    private String deleteFlag;

}