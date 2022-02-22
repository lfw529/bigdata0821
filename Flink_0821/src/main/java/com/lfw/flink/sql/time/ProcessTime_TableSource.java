package com.lfw.flink.sql.time;

import com.lfw.flink.bean.WaterSensor;
import org.apache.flink.api.common.typeinfo.TypeInformation;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.table.api.DataTypes;
import org.apache.flink.table.api.Types;
import org.apache.flink.table.api.bridge.java.StreamTableEnvironment;
import org.apache.flink.table.sources.DefinedProctimeAttribute;
import org.apache.flink.table.sources.StreamTableSource;

//定义一个由处理时间属性的 table source
public class ProcessTime_TableSource {
    public static void main(String[] args) {
        //1.获取执行环境
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(1);
        StreamTableEnvironment tableEnv = StreamTableEnvironment.create(env);

    }

    public class WaterSensorSource implements StreamTableSource<WaterSensor>, DefinedProctimeAttribute {
        @Override
        public String getProctimeAttribute() {
            // 这个名字的列会被追加到最后，作为第三列
            return "water_sensor_time";
        }

        @Override
        public DataStream<WaterSensor> getDataStream(StreamExecutionEnvironment execEnv) {
            // create stream
            DataStream<WaterSensor> waterSensorDS = execEnv.readTextFile("Flink_0821/src/main/resources/sensor.txt")
                    .map(line -> {
                        String[] split = line.split(",");
                        return new WaterSensor(split[0],
                                Long.parseLong(split[1]),
                                Integer.parseInt(split[2]));
                    });
            return waterSensorDS;
        }

        @Override
        public TypeInformation<WaterSensor> getReturnType() {
            String[] names = new String[] {"id" , "ts", "vc"};
            TypeInformation<WaterSensor>[] types = new TypeInformation<WaterSensor>[] {DataTypes.FIELD("id", DataTypes.STRING()), Types.STRING()};
            return Types.ROW(names, types);
        }
    }
}
