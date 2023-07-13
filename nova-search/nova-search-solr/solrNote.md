## nova-search-solr

### 简介:
* 什么是solr，高性能，采用Java开发，基于Lucene的全文搜索服务器，是一款非常优秀的全文搜索引擎

### 第一章
* core就相当于数据库中的表
* 域就相当于关系型数据库表的字段，域的类型相当于字段类型
* 主键域uniqueKey `<uniqueKye>id</uniqueKye>`
* 普通域field `<field name="id" type="string"></field>`  `<fieldType name="string" class="solr.StrField">`
* 动态域dynamicField 就是模糊匹配域名
* 自定义业务域，相当于在数据库中建立表结构，定义字段名和字段类型

### 第二章
* SolrJ引入依赖，基础的增删改查
~~~xml
<dependency>
    <groupId>org.apache.solr</groupId>
    <artifactId>solr-solrj</artifactId>
</dependency>
~~~

### 第三章
* Solr导入Mysql，注意导入jar包(mysql驱动、导入功能包(2个)、分词包)
~~~
mysql-connector-java-8.0.30.jar

solr-dataimporthandler-8.6.0.jar
solr-dataimporthandler-extras-8.6.0.jar

lucene-analyzers-smartcn-8.6.0.jar
~~~

* 如何修改配置文件，以及配置业务域，网上找吧，懒得写教程了

### 优缺点
缺点：
* 建立索引时会产生IO阻塞，搜索效率下降，实时索引搜索效率不高
* 集群需要引入zookeeper

优点：
* 支持更多格式的数据、官方提供的功能更多
* 不考虑建索引的同时进行搜索，速度更快