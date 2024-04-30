## nova-search-elasticsearch

### 参考文档
> 参考文档 <https://docs.spring.io/spring-data/elasticsearch/docs/current/reference/html>  
> es和spring-boot-starter-data-elasticsearch版本对应关系参考 <https://docs.spring.io/spring-data/elasticsearch/docs/current/reference/html/#new-features>  
> 版本对应关系参考 <https://github.com/spring-projects/spring-data-elasticsearch/blob/main/src/main/asciidoc/preface.adoc>  

### 名词解释
1.索引（Index）：索引是Elasticsearch中存储、搜索和分析数据的地方，类似于关系型数据库中的表。一个索引可以包含多个文档，并且可以分布在一个或多个分片上。
2.类型（Type）：在Elasticsearch6.0版本之前，一个索引可以包含多个类型。类型允许你对索引中的文档进行更细粒度的分类。然而，从Elasticsearch6.0开始，类型被废弃，一个索引只能有一个默认类型"_doc"。
3.文档（Document）：文档是Elasticsearch中的最小数据单元。它是以JSON格式表示的一条记录或一份数据。每个文档都属于一个索引，并具有一个唯一的标识（_id）。
4.分片（Shard）：索引可以被分为多个分片。每个分片是一个独立的索引单元，它包含了完整的索引数据。分片允许数据分布在多个节点上，以提高性能和可扩展性。
5.副本（Replica）：副本是分片的复制。每个分片可以有零个或多个副本。副本用于提供冗余和故障恢复。副本还可以提高搜索性能，因为搜索请求可以并行在主分片和副本上执行。
6.节点（Node）：节点是Elasticsearch集群中的一个服务器，它存储数据并参与集群的索引和搜索操作。一个节点可以是主节点（负责协调集群操作）或数据节点（存储和处理数据）。
7.集群（Cluster）：一个集群由一个或多个节点组成，它们共同协作来存储数据和执行操作。集群提供了高可用性、扩展性和容错能力。
8.查询（Query）：查询是用于从Elasticsearch中检索数据的操作。Elasticsearch提供了丰富的查询语言和API，包括全文搜索、精确匹配、范围查询、聚合等功能。
9.聚合（Aggregation）：聚合是在Elasticsearch中进行数据分析的一种功能。它允许你对数据进行分组、计算统计信息、生成报告等，以获取有关数据的洞察力。



### QueryBuilders
QueryBuilders.termsQuery()：精确匹配查询类型，它可以匹配多个精确的值。你可以传递一个或多个值，termsQuery 将匹配包含这些值的文档。
QueryBuilders.matchQuery()：全文检索查询类型，它会将查询字符串分析成词项，并在索引中匹配这些词项。它会考虑词项的相似性、权重和其他相关因素。通常用于对文本进行模糊搜索、匹配短语或对多个字段进行查询。

### [ES查询超过限制，并且NativeSearchQuery设置max数](https://www.cnblogs.com/datangguanjunhou/p/16482242.html)


### API说明
* NativeSearchQuery ：是spring data中的查询条件；
* NativeSearchQueryBuilder ：用于建造一个NativeSearchQuery查询对象；
* QueryBuilders ：设置查询条件，是ES中的类，查询条件构构造器
* SortBuilders ：设置排序条件；
* HighlightBuilder ：设置高亮显示；


### 桶的说明
* Terms Bucket（术语桶）：根据字段值进行分组，类似于SQL中的GROUP BY。可以统计每个术语的文档数量或其他指标。
* Range Bucket（范围桶）：根据字段值的范围进行分组，可以定义多个范围并统计每个范围内的文档数量或其他指标。
* Date Histogram Bucket（日期直方图桶）：根据日期字段的时间间隔进行分组，例如按年、月、周等。可以统计每个时间间隔内的文档数量或其他指标。
* Histogram Bucket（直方图桶）：根据数值字段的间隔进行分组，例如按价格区间、年龄段等。可以统计每个间隔内的文档数量或其他指标。
* Geo Distance Bucket（地理距离桶）：根据地理位置字段的距离进行分组，例如按半径范围内的位置。可以统计每个距离范围内的文档数量或其他指标。
* Filter Bucket（过滤桶）：根据给定的过滤条件对文档进行过滤并创建一个桶。可以在桶内统计过滤后的文档数量或其他指标。
* Nested Bucket（嵌套桶）：用于在嵌套文档结构中进行分组，类似于在嵌套对象上进行递归操作。