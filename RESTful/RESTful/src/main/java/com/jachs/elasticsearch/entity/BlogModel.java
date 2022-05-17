package com.jachs.elasticsearch.entity;

import java.io.Serializable;
import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * @author zhanchaohan
 * 
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class BlogModel implements Serializable {
    private static final long serialVersionUID = 6320548148250372657L;

    private String id;

    private String title;
    private Integer age;
    private Double money;

    private Date time;
}
