package com.lfw.flink.cep;

import com.lfw.flink.bean.WaterSensor;
import org.apache.flink.api.common.eventtime.SerializableTimestampAssigner;
import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.cep.CEP;
import org.apache.flink.cep.PatternSelectFunction;
import org.apache.flink.cep.PatternStream;
import org.apache.flink.cep.pattern.Pattern;
import org.apache.flink.cep.pattern.conditions.IterativeCondition;
import org.apache.flink.cep.pattern.conditions.SimpleCondition;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.time.Duration;

public class SingleCep {
    @Test
    public void test_times() throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        String relativePath = null;

        // @Test 相对文件处理
        try {
            relativePath = new File("").getCanonicalPath() + "/target/classes/cep_test.txt";
        } catch (IOException e) {
            e.printStackTrace();
        }

        DataStreamSource<String> source = env.readTextFile(relativePath);
        //读取数据封装成 JavaBean
        SingleOutputStreamOperator<WaterSensor> returns = source.map(data -> {
            String[] split = data.split(",");
            return new WaterSensor(split[0], Long.parseLong(split[1]), Integer.parseInt(split[2]));
        });

        //添加水印
        SingleOutputStreamOperator<WaterSensor> watermark = returns.assignTimestampsAndWatermarks(
                //最大乱序程度
                WatermarkStrategy.<WaterSensor>forBoundedOutOfOrderness(Duration.ofSeconds(3))
                        .withTimestampAssigner((SerializableTimestampAssigner<WaterSensor>) (element, recordTimestamp) -> element.getTs() * 1000)
        );

        //定义模式
        Pattern<WaterSensor, WaterSensor> beginPattern = Pattern.<WaterSensor>begin("begin")
                .where(new SimpleCondition<WaterSensor>() {
                    @Override
                    public boolean filter(WaterSensor value) throws Exception {
                        return "sensor_1".equals(value.getId());
                    }
                }).times(2);

        //将 CEP 应用到流中
        PatternStream<WaterSensor> pattern = CEP.pattern(watermark, beginPattern);
        SingleOutputStreamOperator<String> select = pattern.select((PatternSelectFunction<WaterSensor, String>)
                //从 "begin" 名称中，获取模式里的数据
                pattern1 -> pattern1.get("begin").toString()
        );

        select.print();
        env.execute();
    }

    @Test
    public void test_timesOrMore() throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        String relativePath = null;

        // @Test 相对文件处理
        try {
            relativePath = new File("").getCanonicalPath() + "/target/classes/cep_test.txt";
        } catch (IOException e) {
            e.printStackTrace();
        }

        DataStreamSource<String> source = env.readTextFile(relativePath);
        //读取数据封装成 JavaBean
        SingleOutputStreamOperator<WaterSensor> returns = source.map(data -> {
            String[] split = data.split(",");
            return new WaterSensor(split[0], Long.parseLong(split[1]), Integer.parseInt(split[2]));
        });

        //添加水印
        SingleOutputStreamOperator<WaterSensor> watermark = returns.assignTimestampsAndWatermarks(
                //最大乱序程度
                WatermarkStrategy.<WaterSensor>forBoundedOutOfOrderness(Duration.ofSeconds(3))
                        .withTimestampAssigner((SerializableTimestampAssigner<WaterSensor>) (element, recordTimestamp) -> element.getTs() * 1000)
        );

        //定义模式
        Pattern<WaterSensor, WaterSensor> beginPattern = Pattern.<WaterSensor>begin("begin")
                .where(new SimpleCondition<WaterSensor>() {
                    @Override
                    public boolean filter(WaterSensor value) throws Exception {
                        return "sensor_1".equals(value.getId());
                    }
                }).timesOrMore(2);

        //将 CEP 应用到流中
        PatternStream<WaterSensor> pattern = CEP.pattern(watermark, beginPattern);
        SingleOutputStreamOperator<String> select = pattern.select((PatternSelectFunction<WaterSensor, String>)
                //从 "begin" 名称中，获取模式里的数据
                pattern1 -> pattern1.get("begin").toString()
        );

        select.print();
        env.execute();
    }

    @Test
    public void test_times_nm() throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        String relativePath = null;

        // @Test 相对文件处理
        try {
            relativePath = new File("").getCanonicalPath() + "/target/classes/cep_test.txt";
        } catch (IOException e) {
            e.printStackTrace();
        }

        DataStreamSource<String> source = env.readTextFile(relativePath);
        //读取数据封装成 JavaBean
        SingleOutputStreamOperator<WaterSensor> returns = source.map(data -> {
            String[] split = data.split(",");
            return new WaterSensor(split[0], Long.parseLong(split[1]), Integer.parseInt(split[2]));
        });

        //添加水印
        SingleOutputStreamOperator<WaterSensor> watermark = returns.assignTimestampsAndWatermarks(
                //最大乱序程度
                WatermarkStrategy.<WaterSensor>forBoundedOutOfOrderness(Duration.ofSeconds(3))
                        .withTimestampAssigner((SerializableTimestampAssigner<WaterSensor>) (element, recordTimestamp) -> element.getTs() * 1000)
        );

        //定义模式
        Pattern<WaterSensor, WaterSensor> beginPattern = Pattern.<WaterSensor>begin("begin")
                .where(new SimpleCondition<WaterSensor>() {
                    @Override
                    public boolean filter(WaterSensor value) throws Exception {
                        return "sensor_1".equals(value.getId());
                    }
                }).times(2, 3);

        //将 CEP 应用到流中
        PatternStream<WaterSensor> pattern = CEP.pattern(watermark, beginPattern);
        SingleOutputStreamOperator<String> select = pattern.select((PatternSelectFunction<WaterSensor, String>)
                //从 "begin" 名称中，获取模式里的数据
                pattern1 -> pattern1.get("begin").toString()
        );

        select.print();
        env.execute();
    }

    //迭代条件
    @Test
    public void test_iterate_condition() throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        String relativePath = null;

        // @Test 相对文件处理
        try {
            relativePath = new File("").getCanonicalPath() + "/target/classes/cep_test.txt";
        } catch (IOException e) {
            e.printStackTrace();
        }

        DataStreamSource<String> source = env.readTextFile(relativePath);
        //读取数据封装成 JavaBean
        SingleOutputStreamOperator<WaterSensor> returns = source.map(data -> {
            String[] split = data.split(",");
            return new WaterSensor(split[0], Long.parseLong(split[1]), Integer.parseInt(split[2]));
        });

        //添加水印
        SingleOutputStreamOperator<WaterSensor> watermark = returns.assignTimestampsAndWatermarks(
                //最大乱序程度
                WatermarkStrategy.<WaterSensor>forBoundedOutOfOrderness(Duration.ofSeconds(3))
                        .withTimestampAssigner((SerializableTimestampAssigner<WaterSensor>) (element, recordTimestamp) -> element.getTs() * 1000)
        );

        //定义模式
        Pattern<WaterSensor, WaterSensor> beginPattern = Pattern.<WaterSensor>begin("begin")
                .where(new IterativeCondition<WaterSensor>() {
                    @Override
                    public boolean filter(WaterSensor value, Context<WaterSensor> ctx) throws Exception {
                        return "sensor_1".equals(value.getId());
                    }
                });

        //将 CEP 应用到流中
        PatternStream<WaterSensor> pattern = CEP.pattern(watermark, beginPattern);
        SingleOutputStreamOperator<String> select = pattern.select((PatternSelectFunction<WaterSensor, String>)
                //从 "begin" 名称中，获取模式里的数据
                pattern1 -> pattern1.get("begin").toString()
        );

        select.print();
        env.execute();
    }

    // 组合条件
    @Test
    public void test_combination_condition_or() throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        String relativePath = null;

        // @Test 相对文件处理
        try {
            relativePath = new File("").getCanonicalPath() + "/target/classes/cep_test02.txt";
        } catch (IOException e) {
            e.printStackTrace();
        }

        DataStreamSource<String> source = env.readTextFile(relativePath);
        //读取数据封装成 JavaBean
        SingleOutputStreamOperator<WaterSensor> returns = source.map(data -> {
            String[] split = data.split(",");
            return new WaterSensor(split[0], Long.parseLong(split[1]), Integer.parseInt(split[2]));
        });

        //添加水印
        SingleOutputStreamOperator<WaterSensor> watermark = returns.assignTimestampsAndWatermarks(
                //最大乱序程度
                WatermarkStrategy.<WaterSensor>forBoundedOutOfOrderness(Duration.ofSeconds(3))
                        .withTimestampAssigner((SerializableTimestampAssigner<WaterSensor>) (element, recordTimestamp) -> element.getTs() * 1000)
        );

        //定义模式
        Pattern<WaterSensor, WaterSensor> beginPattern = Pattern.<WaterSensor>begin("begin")
                .where(new SimpleCondition<WaterSensor>() {
                    @Override
                    public boolean filter(WaterSensor value) throws Exception {
                        return "sensor_2".equals(value.getId());
                    }
                }).or(new SimpleCondition<WaterSensor>() {
                    @Override
                    public boolean filter(WaterSensor value) throws Exception {
                        return "sensor_7".equals(value.getId());
                    }
                });

        //将 CEP 应用到流中
        PatternStream<WaterSensor> pattern = CEP.pattern(watermark, beginPattern);
        SingleOutputStreamOperator<String> select = pattern.select((PatternSelectFunction<WaterSensor, String>)
                //从 "begin" 名称中，获取模式里的数据
                pattern1 -> pattern1.get("begin").toString()
        );

        select.print();
        env.execute();
    }

    @Test
    public void test_combination_condition_and() throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        String relativePath = null;

        // @Test 相对文件处理
        try {
            relativePath = new File("").getCanonicalPath() + "/target/classes/cep_test02.txt";
        } catch (IOException e) {
            e.printStackTrace();
        }

        DataStreamSource<String> source = env.readTextFile(relativePath);
        //读取数据封装成 JavaBean
        SingleOutputStreamOperator<WaterSensor> returns = source.map(data -> {
            String[] split = data.split(",");
            return new WaterSensor(split[0], Long.parseLong(split[1]), Integer.parseInt(split[2]));
        });

        //添加水印
        SingleOutputStreamOperator<WaterSensor> watermark = returns.assignTimestampsAndWatermarks(
                //最大乱序程度
                WatermarkStrategy.<WaterSensor>forBoundedOutOfOrderness(Duration.ofSeconds(3))
                        .withTimestampAssigner((SerializableTimestampAssigner<WaterSensor>) (element, recordTimestamp) -> element.getTs() * 1000)
        );

        //定义模式
        Pattern<WaterSensor, WaterSensor> beginPattern = Pattern.<WaterSensor>begin("begin")
                .where(new SimpleCondition<WaterSensor>() {
                    @Override
                    public boolean filter(WaterSensor value) throws Exception {
                        return "sensor_1".equals(value.getId());
                    }
                }).where(new SimpleCondition<WaterSensor>() {
                    @Override
                    public boolean filter(WaterSensor value) throws Exception {
                        return value.getTs() < 4;
                    }
                });

        //将 CEP 应用到流中
        PatternStream<WaterSensor> pattern = CEP.pattern(watermark, beginPattern);
        SingleOutputStreamOperator<String> select = pattern.select((PatternSelectFunction<WaterSensor, String>)
                //从 "begin" 名称中，获取模式里的数据
                pattern1 -> pattern1.get("begin").toString()
        );

        select.print();
        env.execute();
    }

    @Test
    public void test_combination_condition_until() throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        String relativePath = null;

        // @Test 相对文件处理
        try {
            relativePath = new File("").getCanonicalPath() + "/target/classes/cep_test02.txt";
        } catch (IOException e) {
            e.printStackTrace();
        }

        DataStreamSource<String> source = env.readTextFile(relativePath);
        //读取数据封装成 JavaBean
        SingleOutputStreamOperator<WaterSensor> returns = source.map(data -> {
            String[] split = data.split(",");
            return new WaterSensor(split[0], Long.parseLong(split[1]), Integer.parseInt(split[2]));
        });

        //添加水印
        SingleOutputStreamOperator<WaterSensor> watermark = returns.assignTimestampsAndWatermarks(
                //最大乱序程度
                WatermarkStrategy.<WaterSensor>forBoundedOutOfOrderness(Duration.ofSeconds(3))
                        .withTimestampAssigner((SerializableTimestampAssigner<WaterSensor>) (element, recordTimestamp) -> element.getTs() * 1000)
        );

        //定义模式
        Pattern<WaterSensor, WaterSensor> beginPattern = Pattern.<WaterSensor>begin("begin")
                .where(new SimpleCondition<WaterSensor>() {
                    @Override
                    public boolean filter(WaterSensor value) throws Exception {
                        return "sensor_1".equals(value.getId());
                    }
                }).timesOrMore(2).until(new SimpleCondition<WaterSensor>() {
                    @Override
                    public boolean filter(WaterSensor value) throws Exception {
                        return "sensor_2".equals(value.getId());
                    }
                });

        //将 CEP 应用到流中
        PatternStream<WaterSensor> pattern = CEP.pattern(watermark, beginPattern);
        SingleOutputStreamOperator<String> select = pattern.select((PatternSelectFunction<WaterSensor, String>)
                //从 "begin" 名称中，获取模式里的数据
                pattern1 -> pattern1.get("begin").toString()
        );

        select.print();
        env.execute();
    }
}
