package com.lfw.mapreduce.writable;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;

public class FlowReducer extends Reducer<Text, FlowBean, Text, FlowBean> {

    private FlowBean outV = new FlowBean();

    @Override
    protected void reduce(Text key, Iterable<FlowBean> values, Context context) throws IOException, InterruptedException {
        //迭代每个key的所有value,进行流量的汇总累加
        int sumUpFlow = 0;
        int sumDownFlow = 0;
        for (FlowBean value : values) {
            sumUpFlow += value.getUpFlow();
            sumDownFlow += value.getDownFlow();
        }

        //封装value
        outV.setUpFlow(sumUpFlow);
        outV.setDownFlow(sumDownFlow);
        outV.setSumFlow();

        //写出
        context.write(key, outV);
    }
}
