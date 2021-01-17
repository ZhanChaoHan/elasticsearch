package com.jachs.elasticsearch.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Id;

import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@Document(indexName = "blog", indexStoreType = "java")
public class BlogModel implements Serializable {
    private static final long serialVersionUID = 6320548148250372657L;

    @Id
    private String id;

    private String title;
    private Integer age;
    private Double money;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date time;
}
