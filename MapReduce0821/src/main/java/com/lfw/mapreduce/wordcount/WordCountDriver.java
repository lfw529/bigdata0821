package com.lfw.mapreduce.wordcount;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import java.io.IOException;

public class WordCountDriver {
    public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException {
        //1. 创建一个Job对象
        Configuration conf = new Configuration();
        //往集群提交Job
        Job job = Job.getInstance(conf);

        //2. 关联jar
        job.setJarByClass(WordCountDriver.class); //程序入口

        //3. 关联mapper 和 reducer
        job.setMapperClass(WordCountMapper.class);
        job.setReducerClass(WordCountReducer.class);
        //4. 设置mapper输出的kv类型
        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(IntWritable.class);
        //5. 设置最终输出的kv类型
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(IntWritable.class);

        //6. 设置输入和输出路径(本地)
        FileInputFormat.setInputPaths(job, new Path("D:\\IdeaProjects\\bigdata0821\\MapReduce0821\\src\\main\\resources\\hello.txt"));
        FileOutputFormat.setOutputPath(job, new Path("D:\\IdeaProjects\\bigdata0821\\MapReduce0821\\src\\main\\resources\\output2"));

        //集群
        //FileInputFormat.setInputPaths(job,new Path(args[0]));
        //FileOutputFormat.setOutputPath(job,new Path(args[1]));

        //7. 提交job
        job.waitForCompletion(true);
    }
}
