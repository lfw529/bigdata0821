package com.lfw.flink.flinksql3;

import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.table.api.bridge.java.StreamTableEnvironment;
import org.apache.flink.table.catalog.hive.HiveCatalog;

public class My_HiveCatalog {
    public static void main(String[] args) {
        //1.获取执行环境
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(1);
        StreamTableEnvironment tableEnv = StreamTableEnvironment.create(env);

        //2.创建HiveCatalog
        HiveCatalog hiveCatalog = new HiveCatalog("myHive", "default", "D:\\IdeaProjects\\bigdata0821\\Flink_0821\\input");

        //3.注册HiveCatalog
        tableEnv.registerCatalog("myHive", hiveCatalog);

        //4.使用HiveCatalog
        tableEnv.useCatalog("myHive");

        //5.执行查询,查询Hive中已经存在的表数据
        tableEnv.executeSql("select * from business2").print();
    }
}
