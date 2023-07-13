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
* SolrJ和Spring Data Solr

