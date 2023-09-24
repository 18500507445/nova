## nova-search-elasticsearch


### 参考文档
> 参考文档 <https://docs.spring.io/spring-data/elasticsearch/docs/current/reference/html>  
> es 和 spring-boot-starter-data-elasticsearch 版本对应关系参考 <https://docs.spring.io/spring-data/elasticsearch/docs/current/reference/html/#new-features>  
> 版本对应关系参考 <https://github.com/spring-projects/spring-data-elasticsearch/blob/main/src/main/asciidoc/preface.adoc>  


### 名词解释
1. 索引（Index）：索引是 Elasticsearch 中存储、搜索和分析数据的地方，类似于关系型数据库中的表。一个索引可以包含多个文档，并且可以分布在一个或多个分片上。
2. 类型（Type）：在 Elasticsearch 6.0 版本之前，一个索引可以包含多个类型。类型允许你对索引中的文档进行更细粒度的分类。然而，从 Elasticsearch 6.0 开始，类型被废弃，一个索引只能有一个默认类型 "_doc"。
3. 文档（Document）：文档是 Elasticsearch 中的最小数据单元。它是以 JSON 格式表示的一条记录或一份数据。每个文档都属于一个索引，并具有一个唯一的标识（_id）。
4. 分片（Shard）：索引可以被分为多个分片。每个分片是一个独立的索引单元，它包含了完整的索引数据。分片允许数据分布在多个节点上，以提高性能和可扩展性。
5. 副本（Replica）：副本是分片的复制。每个分片可以有零个或多个副本。副本用于提供冗余和故障恢复。副本还可以提高搜索性能，因为搜索请求可以并行在主分片和副本上执行。
6. 节点（Node）：节点是 Elasticsearch 集群中的一个服务器，它存储数据并参与集群的索引和搜索操作。一个节点可以是主节点（负责协调集群操作）或数据节点（存储和处理数据）。
7. 集群（Cluster）：一个集群由一个或多个节点组成，它们共同协作来存储数据和执行操作。集群提供了高可用性、扩展性和容错能力。
8. 查询（Query）：查询是用于从 Elasticsearch 中检索数据的操作。Elasticsearch 提供了丰富的查询语言和API，包括全文搜索、精确匹配、范围查询、聚合等功能。
9. 聚合（Aggregation）：聚合是在 Elasticsearch 中进行数据分析的一种功能。它允许你对数据进行分组、计算统计信息、生成报告等，以获取有关数据的洞察力。