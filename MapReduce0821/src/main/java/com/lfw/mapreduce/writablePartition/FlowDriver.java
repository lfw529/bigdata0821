package com.lfw.mapreduce.writablePartition;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import java.io.IOException;


public class FlowDriver {
    public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException {
        Configuration conf = new Configuration();
        Job job = Job.getInstance(conf);

        job.setJarByClass(FlowDriver.class);
        job.setMapperClass(FlowMapper.class);
        job.setReducerClass(FlowReducer.class);
        job.setMapOutputKeyClass(Text.class);
        job.setOutputValueClass(FlowBean.class);

        //设置分区器类
        job.setPartitionerClass(PhoneNumPartitioner.class);
        //job.setPartitionerClass(Partitioner.class);  //默认分区
        //设置reduce的个数  按照分区器的业务逻辑来进行设置
        job.setNumReduceTasks(5);

        FileInputFormat.setInputPaths(job, new Path("D:\\IdeaProjects\\bigdata0821\\MapReduce0821\\src\\main\\resources\\phone_data .txt"));
        FileOutputFormat.setOutputPath(job, new Path("D:\\IdeaProjects\\bigdata0821\\MapReduce0821\\src\\main\\resources\\output4"));
        job.waitForCompletion(true);
    }
}
