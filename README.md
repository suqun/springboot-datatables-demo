## 使用Spring Boot Jquery Datatables实现管理平台的表格


最近在公司做一个运营平台,增删改查的那种,需要一个多功能的表格,网上看到Jquery的DataTables功能很丰富,查询,排序,翻页等等功能完善,
但是[DataTables官网](http://www.datatables.net/)上的例子,表格数据都没有从服务端获取,生产上使用还得自己摸索下.另外,公司使用
Spring boot这个框架,配置简单,下面我们一起做一个整合的例子

### 新建Spring boot的应用
新建个项目springboot-datatables-demo,我使用的是intellij idea,创建个maven项目,在pom里面引用包后,创建main方法即可主要代码如下:
(详细讲解可以参考我的上一篇日志[第一个Spring Boot应用](http://suqun.github.io/2016/02/17/%E7%AC%AC%E4%B8%80%E4%B8%AASpring-Boot%E5%BA%94%E7%94%A8/))

####pom.xml
```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>

    <groupId>com.larry</groupId>
    <artifactId>springboot-datatables-demo</artifactId>
    <version>1.0-SNAPSHOT</version>

    <parent>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-parent</artifactId>
        <version>1.2.5.RELEASE</version>
    </parent>

    <properties>
        <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
        <java.version>1.8</java.version>
    </properties>

    <dependencies>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-thymeleaf</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-data-jpa</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
        </dependency>
        <dependency>
            <groupId>mysql</groupId>
            <artifactId>mysql-connector-java</artifactId>
        </dependency>
        <dependency>
            <groupId>c3p0</groupId>
            <artifactId>c3p0</artifactId>
            <version>0.9.1.2</version>
        </dependency>
        <dependency>
            <groupId>net.sourceforge.nekohtml</groupId>
            <artifactId>nekohtml</artifactId>
            <version>1.9.21</version>
        </dependency>
        <dependency>
            <groupId>org.projectlombok</groupId>
            <artifactId>lombok</artifactId>
            <version>1.16.4</version>
        </dependency>
        <dependency>
            <groupId>junit</groupId>
            <artifactId>junit</artifactId>
            <version>4.12</version>
        </dependency>
        <dependency>
            <groupId>org.mockito</groupId>
            <artifactId>mockito-core</artifactId>
            <version>1.10.19</version>
            <scope>test</scope>
        </dependency>
        <dependency>
            <groupId>net.coobird</groupId>
            <artifactId>thumbnailator</artifactId>
            <version>0.4.8</version>
        </dependency>
        <dependency>
            <groupId>com.alibaba</groupId>
            <artifactId>fastjson</artifactId>
            <version>1.2.8</version>
        </dependency>
    </dependencies>

    <build>
        <plugins>
            <plugin>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-maven-plugin</artifactId>
            </plugin>
        </plugins>
    </build>

    <repositories>
        <repository>
            <id>spring-releases</id>
            <name>Spring Releases</name>
            <url>https://repo.spring.io/libs-release</url>
        </repository>
    </repositories>
    <pluginRepositories>
        <pluginRepository>
            <id>spring-releases</id>
            <name>Spring Releases</name>
            <url>https://repo.spring.io/libs-release</url>
        </pluginRepository>
    </pluginRepositories>
</project>
```

#### SddApplication.java

```java
package com.larry;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SddApplication {
    public static void main(String[] args) {
        SpringApplication.run(SddApplication.class, args);
    }
}
```

ok,添加完上面两个主要的内容maven引入相关包以后,就可以运行啦,在SDDApplication上右击run一下.....额,是不是报错了,貌似把数据库给忘啦.
我们使用mysql数据库,需要添加数据库配置类config.java,添加配置文件application.properties

#### 建表
```bash
SET NAMES utf8;
SET FOREIGN_KEY_CHECKS = 0;
DROP TABLE IF EXISTS `app`;
CREATE TABLE `app` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `create_time` datetime DEFAULT NULL,
  `description` text,
  `hot` int(8) DEFAULT '0',
  `keywords` text,
  `url` varchar(255) NOT NULL,
  `disabled` int(2) NOT NULL DEFAULT '0',
  `name` varchar(50) DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  `is_delete` bit(1) DEFAULT NULL COMMENT '是否删除，0：删除',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;
```

####application.properties添加数据库配置信息

```bash
spring.thymeleaf.mode=LEGACYHTML5
spring.thymeleaf.encoding=UTF-8
spring.thymeleaf.cache=false

spring.datasource.c3p0.driver-class-name=com.mchange.v2.c3p0.ComboPooledDataSource
spring.datasource.c3p0.jdbc-url=jdbc:mysql://127.0.0.1:3306/test?useUnicode=yes&characterEncoding=UTF-8
spring.datasource.c3p0.username=root
spring.datasource.c3p0.password=
spring.datasource.c3p0.min-evictable-idle-time-millis=30000
spring.jpa.hibernate.dialect=org.hibernate.dialect.MySQL5Dialect
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
```
由于我使用的是thymeleaf模板引擎,需要在resource下面添加文件夹templates,再添加个html, 随便建个html叫index.html,在body里面写个 hello world！ 好了。

然后,我们run一下试试
```bash
2016-04-03 15:29:27.850  INFO 1847 --- [           main] s.b.c.e.t.TomcatEmbeddedServletContainer : Tomcat started on port(s): 8080 (http)
2016-04-03 15:29:27.852  INFO 1847 --- [           main] com.larry.SddApplication                 : Started SddApplication in 4.522 seconds ...
```
当我们发现这两句话时,说明我们项目已经启动成功了.不过输入http://localhost:8080/浏览器仍然会报错,我们配置下路由让/路径默认跳转到index模板上.

###MvcConfig

```java
package com.larry.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
public class MvcConfig extends WebMvcConfigurerAdapter {
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("").setViewName("index");
    }
}
```

重启后,浏览器中输入http://localhost:8080 哈哈，是不是看到熟悉的 hello world！ 啦。

### datatables
啰嗦了半天终于进入主题了。。。。我在github上发现有个哥们已经封装了一套[spring data jpa + jquery datatables](https://github.com/darrachequesne/spring-data-jpa-datatables)的项目，直接pom里面引用下，就ok拉，下面看看具体怎么是用

#### Maven 依赖
```xml
        <dependency>
            <groupId>com.github.darrachequesne</groupId>
            <artifactId>spring-data-jpa-datatables</artifactId>
            <version>2.0</version>
        </dependency>
```

注意这哥们使用的hibernate包是4.3.10.Final，这个和spring-boot用的hibernate要一致，否则启动的时候就会报错，所以我选择了1.2.5.RELEASE的版本的spring boot。

#### 启用DataTablesRepository工厂
```java
@SpringBootApplication
@EnableJpaRepositories(repositoryFactoryBeanClass = DataTablesRepositoryFactoryBean.class)
public class SddApplication {
    public static void main(String[] args) {
        SpringApplication.run(SddApplication.class, args);
    }
}
```

#### 扩展DataTablesRepository接口
```java
public interface AppRepository extends DataTablesRepository<App, Long> {
}
```

#### 设置model属性
```java
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
```

#### 包含jquery.spring-friendly.js
It overrides jQuery data serialization to allow Spring MVC to correctly map input parameters (by changing column[0][data] to column[0].data in request payload)

#### index.html 
index.html
```html
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Title</title>
    <!--<link rel="stylesheet" href="//cdn.datatables.net/1.10.11/css/jquery.dataTables.min.css">-->
    <link rel="stylesheet" href="//cdn.bootcss.com/bootstrap/3.3.6/css/bootstrap.css">
    <link rel="stylesheet" href="/dataTables.bootstrap.css">
</head>
<body>
<div class="box">
    <div class="box-header with-border">
        <button type="button" class="btn btn-info" >增加12条数据</button>
    </div><!-- /.box-header -->
    <div class="box-body">
        <table id="appTable" class="table table-bordered table-striped">
            <thead>
            <tr>
                <th></th>
                <th>操作</th>
                <th>名称</th>
                <th>地址</th>
                <th>关键字</th>
                <th>描述</th>
                <th>热度</th>
                <th>添加日期</th>
                <th>更新时间</th> <!--日期格式在application.properties添加：spring.jackson.date-format=yyyy-MM-dd HH:mm:ss-->
                <th>状态</th>
            </tr>
            </thead>
        </table>
    </div><!-- /.box-body -->
</div>
<script src="/jQuery-2.1.4.min.js"></script>
<script src="/jquery.dataTables.js"></script>
<script src="/dataTables.bootstrap.min.js"></script>
<script src="/jquery.spring-friendly.js"></script>
<script>
    $().ready(function () {
        $('#appTable').DataTable({
            ajax: '/all',
            serverSide: true,
            order: [
                [8, 'desc']//更新时间倒序
            ],
            columns: [{
                data: null,
                orderable: false,
                searchable: false,
                render: function (data, type, row) {
                    return "<td><input type='checkbox' name='allocated' value='" + row.id + "'></td>";
                }
            }, {
                data: '',
                orderable: false,
                searchable: false,
                render: function (data, type, row) {
                    return "<td><button type='button' class='btn btn-primary btn-sm' onclick='editApp(" + row.id + ")'>编辑</button> &nbsp;" +
//                            "<button type='button' class='btn btn-info btn-sm' onclick='detail("+row.id+")'>详情</button>" +
                            "&nbsp;<button type='button' class='btn btn-warning btn-sm' onclick='deleteSingle("+row.id+")'>删除</button>" +
                            "</td>";
                }
            }, {
                data: 'name'
            }, {
                data: 'url',
                render: function (data, type, row) {
                    var shortUrl;
                    if(data.length<30){
                        shortUrl = data ;
                    } else {
                        shortUrl = data.substring(0,30)+"...";
                    }
                    return "<a href='" + data + "' target='_blank'>"+shortUrl+"</a>";
                }
            }, {
                data: 'keywords'
            }, {
                data: 'description'
            }, {
                data: 'hot'
            }, {
                data: 'createTime'
            }, {
                data: 'updateTime'
            }, {
                data: 'disabled',
                render: function (data, type, row) {
                    if (row.disabled) {
                        return "<input type='checkbox' name='state-checkbox' value='" + row.id + "'>";
                    } else {
                        return "<input type='checkbox' name='state-checkbox' value='" + row.id + "' checked>";
                    }
                }
            }]
//            initComplete: function () {
//                $("input[name='state-checkbox']").bootstrapSwitch();
//            },
//            drawCallback: function() {//Function that is called every time DataTables performs a draw.
//                $("input[name='state-checkbox']").bootstrapSwitch();
//            }


        });
    });
    
    $("button").on("click", function () {
        $.get("init")
                .success(function (data) {
                    window.location="/";
                });
    });
</script>
</body>
</html>
```

#### AppController
```java
    @ResponseBody
    @RequestMapping(value = "all", method = RequestMethod.GET)
    public DataTablesOutput<App> messages(@Valid DataTablesInput input) {
        return appRepository.findAll(input);
    }
```

启动后，添加数据后，显示效果如下，已经可以分页搜索排序了。
![表格](http://7xpk5e.com1.z0.glb.clouddn.com/datatables.png)
到这里就可以看到，我们的基本目标已经完成了。不过仍然有个问题，现在的这个查询用的是`findAll(input)`,如果我们要添加过滤条件进行查询呢。其实[darrachequesne](https://github.com/darrachequesne)这个哥们已经封装了。'DataTablesOutput<T> findAll(DataTablesInput var1, Specification<T> var2);'调用这个就可以，下面我们来看看具体怎么用

### 使用过滤条件查询
过滤查询使用到了[Spring Data JPA - Specifications](https://spring.io/blog/2011/04/26/advanced-spring-data-jpa-specifications-and-querydsl/),自己要去熟悉了解下这个。

我们创建个Specification条件，然后调用'DataTablesOutput<T> findAll(DataTablesInput var1, Specification<T> var2);'
假设我们要查询所有删除状态为false的记录

#### AppSpec
```java
public class AppSpec {
    public static Specification<App> isNotDelete() throws Exception {
        return (root, criteriaQuery, criteriaBuilder) -> criteriaBuilder.isFalse(root.get("isDelete"));
    }
}
```
注意：使用了lambda表达式，其实就是创建了内部类

#### 修改AppController
```java
@RequestMapping(value = "all", method = RequestMethod.GET)
    public DataTablesOutput<App> messages(@Valid DataTablesInput input) {
        try {
            return appRepository.findAll(input, AppSpec.isNotDelete());
        } catch (Exception e) {
            return null;
        }
    }
```

重启，添加12条数据，设置6条记录删除状态为true，看看效果
![过滤删除记录](http://7xpk5e.com1.z0.glb.clouddn.com/datatables-delete.png)
表格下面显示：
Showing 1 to 6 of 6 entries (filtered from 12 total entries)
说明我们过滤成功了！

打完收工！
详细代码，欢迎从我的github上获取：[spring-data-jpa-jquery-datatables]()

