package com.jachs.elasticsearch.entity;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * @author zhanchaohan
 * 
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class IMDBProgram {

    private Long id;

    private Long subprogramId;

    private Long programId;

    private Integer episode;

    private Integer paragraph;

    private Integer directWeight;

    private String sourceType;

    private String deleteFlag;

}