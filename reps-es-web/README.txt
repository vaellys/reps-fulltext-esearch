elasticsearch 是一个基于lucene全文检索引擎

1.ES服务启动(jdk版本1.7以上)
	window启动脚本
	elasticsearch-2.4.2\bin\elasticsearch.bat
	
	linux启动命令
	elasticsearch-2.4.2\bin\elasticsearch -d
	
	tcp监听端口：9300,用于节点间内部通信
	http端口：9200,用于es http rest接口调用

2.ES 索引数据目录位置配置
	elasticsearch-2.4.2\config\elasticsearch.yml中
	启用：path.data: /path/to/data1
	日志目录
	启用： path.logs: /path/to/logs

3.修改相应的映射结构文件 mapping/organize.json名字为要索引的类型名字（相当于表名），以及文件中的字段名称及属性（相当于表结构）

4.elasticconfig.properties中属性设置
	修改reps-es-web模块中elasticconfig.properties文件中主机ip（即ES服务部署主机ip）
		es.cluster.host
           修改索引类型,默认为organize
        es.index.type
           修改索引回调接口地址
        es.rebuild.callback.http.url
           修改重构索引类型，默认为organize
        es.rebuild.index.type
  注意：此文件中设置的类型和3中organize.json文件名字前缀一致，不需要后缀
  
5.使用web容器部署reps-es-web工程
           索引接口调用地址:POST http://localhost:8080/reps-es-web/es/addIndex  参数 index=index,type=organize,documents=[{}] documents为json字符串 [{"id","1","name","张三"},{"id","2","name","李四"}]
           搜索调用接口地址:GET http://localhost:8080/reps-es-web/es/search?keywords=测试&index=index&type=organize


