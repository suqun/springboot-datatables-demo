package com.larry.model;

import com.fasterxml.jackson.annotation.JsonView;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.validator.constraints.URL;
import org.springframework.data.jpa.datatables.mapping.DataTablesOutput;

import javax.persistence.*;
import java.util.Date;

@Entity
@Data
public class App {
    @Id
    @GeneratedValue
    private long id;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    @URL
    private String url;
    private String description;
    private String keywords;
    private boolean disabled;
    private int hot;

    @CreationTimestamp
    private Date createTime;
    @UpdateTimestamp
    private Date updateTime;
    private boolean isDelete;
}
