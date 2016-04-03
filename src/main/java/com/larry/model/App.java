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
    @JsonView(DataTablesOutput.View.class)
    @Id
    @GeneratedValue
    private long id;
    @Column(nullable = false)
    @JsonView(DataTablesOutput.View.class)
    private String name;
    @Column(nullable = false)
    @URL
    @JsonView(DataTablesOutput.View.class)
    private String url;
    private String description;
    private String keywords;
    private boolean disabled;
    @JsonView(DataTablesOutput.View.class)
    private int hot;

    @JsonView(DataTablesOutput.View.class)
    @CreationTimestamp
    private Date createTime;
    @JsonView(DataTablesOutput.View.class)
    @UpdateTimestamp
    private Date updateTime;
    private boolean isDelete;
}
