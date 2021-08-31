package com.lfw.mapreduce.combinetextinputformat;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.CombineTextInputFormat;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import java.io.IOException;

public class WordCountDriver {
    public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException {
       //1. 创建一个Job对象
        Configuration conf = new Configuration();
        Job job = Job.getInstance(conf);

        //2. 关联jar
        job.setJarByClass(WordCountDriver.class);

       //3. 关联mapper 和reducer
        job.setMapperClass(WordCountMapper.class);
        job.setReducerClass(WordCountReducer.class);
       //4. 设置mapper输出的kv类型
        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(IntWritable.class);
       //5. 设置最终输出的kv类型
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(IntWritable.class);

       //6. 设置输入和输出路径(本地)
        FileInputFormat.setInputPaths(job,new Path("D:\\IdeaProjects\\bigdata0821\\MapReduce0821\\src\\main\\resources\\4"));
        FileOutputFormat.setOutputPath(job,new Path("D:\\IdeaProjects\\bigdata0821\\MapReduce0821\\src\\main\\resources\\output0"));

        //设置inputformat
        job.setInputFormatClass(CombineTextInputFormat.class);
        CombineTextInputFormat.setMaxInputSplitSize(job, 4194304); //4M


       //7. 提交job
        job.waitForCompletion(true);
    }
}
