package com.lfw.mapreduce.mapjoin;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import java.io.IOException;
import java.net.URI;

public class MapJoinDriver {

    public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException {
        Configuration conf = new Configuration();
        Job job  = Job.getInstance(conf);

        job.setJarByClass(MapJoinDriver.class);
        job.setMapperClass(MapJoinMapper.class);

        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(NullWritable.class);

        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(NullWritable.class);

        FileInputFormat.setInputPaths(job,new Path("D:\\IdeaProjects\\bigdata0821\\MapReduce0821\\src\\main\\resources\\input11"));
        FileOutputFormat.setOutputPath(job,new Path("D:\\IdeaProjects\\bigdata0821\\MapReduce0821\\src\\main\\resources\\output11"));

        //设置缓存文件
        job.addCacheFile(URI.create("file:///D:/IdeaProjects/bigdata0821/MapReduce0821/src/main/resources/inputcache11/pd.txt"));

        //设置reduce的个数为0
        job.setNumReduceTasks(0);

        job.waitForCompletion(true);
    }
}
