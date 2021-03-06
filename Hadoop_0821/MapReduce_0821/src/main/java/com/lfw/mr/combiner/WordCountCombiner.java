package com.lfw.mr.combiner;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;

/**
 * combiner要继承Hadooop提供的Reducer类
 */
public class WordCountCombiner extends Reducer<Text, IntWritable, Text, IntWritable> {

    private final IntWritable outV = new IntWritable();

    @Override
    protected void reduce(Text key, Iterable<IntWritable> values, Context context) throws IOException, InterruptedException {
        int sum = 0;
        for (IntWritable value : values) {
            sum += value.get();
        }

        outV.set(sum);
        context.write(key, outV);
    }
}
