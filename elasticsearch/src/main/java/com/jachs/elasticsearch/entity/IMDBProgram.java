package com.jachs.elasticsearch.entity;

/**
 * @author zhanchaohan
 * 
 */
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;

@Document(indexName = "subprogram_data_test_3",type = "docs")
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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getSubprogramId() {
        return subprogramId;
    }

    public void setSubprogramId(Long subprogramId) {
        this.subprogramId = subprogramId;
    }

    public Long getProgramId() {
        return programId;
    }

    public void setProgramId(Long programId) {
        this.programId = programId;
    }

    public Integer getEpisode() {
        return episode;
    }

    public void setEpisode(Integer episode) {
        this.episode = episode;
    }

    public Integer getParagraph() {
        return paragraph;
    }

    public void setParagraph(Integer paragraph) {
        this.paragraph = paragraph;
    }

    public Integer getDirectWeight() {
        return directWeight;
    }

    public void setDirectWeight(Integer directWeight) {
        this.directWeight = directWeight;
    }

    public String getSourceType() {
        return sourceType;
    }

    public void setSourceType(String sourceType) {
        this.sourceType = sourceType;
    }

    public String getDeleteFlag() {
        return deleteFlag;
    }

    public void setDeleteFlag(String deleteFlag) {
        this.deleteFlag = deleteFlag;
    }
}