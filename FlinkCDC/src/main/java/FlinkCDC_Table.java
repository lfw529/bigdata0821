import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.table.api.bridge.java.StreamTableEnvironment;

public class FlinkCDC_Table {
    public static void main(String[] args) throws Exception {
        //1.创建执行环境
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(1);
        StreamTableEnvironment tableEnv = StreamTableEnvironment.create(env);

        //2.创建Flink-MySQL-CDC的Source
        tableEnv.executeSql("CREATE TABLE test01 (" +
                "  id STRING," +
                "  NAME STRING," +
                "  sex STRING," +
                "  primary key(id) not enforced" +
                ") WITH (" +
                "  'connector' = 'mysql-cdc'," +
                "  'hostname' = 'hadoop102'," +
                "  'port' = '3306'," +
                "  'username' = 'root'," +
                "  'password' = '1234'," +
                "  'database-name' = 'finkcdc'," +
                "  'table-name' = 'test01'" +
                ")");

        tableEnv.executeSql("select * from test01").print();

        env.execute();
    }
}
