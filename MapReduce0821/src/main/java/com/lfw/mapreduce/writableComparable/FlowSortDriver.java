package com.lfw.mapreduce.writableComparable;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import java.io.IOException;

public class FlowSortDriver {

    public static void main(String[] args) throws InterruptedException, IOException, ClassNotFoundException {

        Configuration conf = new Configuration();
        Job job = Job.getInstance(conf);

        job.setJarByClass(FlowSortDriver.class);
        job.setMapperClass(FlowSortMapper.class);
        job.setReducerClass(FlowSortReducer.class);

        job.setMapOutputKeyClass(FlowBean.class);
        job.setMapOutputValueClass(Text.class);

        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(FlowBean.class);

        FileInputFormat.setInputPaths(job, new Path("D:\\IdeaProjects\\bigdata0821\\MapReduce0821\\src\\main\\resources\\phone_data .txt"));
        FileOutputFormat.setOutputPath(job, new Path("D:\\IdeaProjects\\bigdata0821\\MapReduce0821\\src\\main\\resources\\output5"));

        //设置分区器
        job.setPartitionerClass(FlowSortPartitioner.class);
        job.setNumReduceTasks(5);

        job.waitForCompletion(true);
    }
}
