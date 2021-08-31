package com.lfw.hbase;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.*;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.filter.FilterList;
import org.apache.hadoop.hbase.filter.RowFilter;
import org.apache.hadoop.hbase.filter.SingleColumnValueFilter;
import org.apache.hadoop.hbase.filter.SubstringComparator;
import org.apache.hadoop.hbase.util.Bytes;
import org.junit.Test;

import java.io.IOException;

public class HbaseUtils {
    // Connection 是重量级的实现，但是是线程安全的。因此推荐只实例化一次
    private static Connection connection;

    static {
        try {
            Configuration configuration = HBaseConfiguration.create();
            configuration.set("hbase.zookeeper.quorum", "hadoop102,hadoop103,hadoop104");
            connection = ConnectionFactory.createConnection(configuration);
            System.out.println("connection:" + configuration);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testCreateNamespace() {
        createNamespace("atguigu");
    }

    @Test
    public void testCreateTable() {
        createTable("atguigu", "student", "info", "msg");
    }

    @Test
    public void testDropTable() {
        dropTable("atguigu", "student");
    }

    @Test
    public void testPutDataToTable() {
        putDataToTable(null, "student", "1005", "info", "name", "dawei");
    }

    @Test
    public void testDeleteData() {
        deleteData(null, "student", "1005", "info", "age");
    }

    @Test
    public void testGetData() {
        getData(null, "student", "1005", null, null);
    }

    @Test
    public void testScanData() {
        scanData(null, "student", "1001", "1004");
    }

    @Test
    public void testScanDataWithCondition1() {
        scanDataWithCondition1(null, "student");
    }

    @Test
    public void testScanDataWithCondition2() {
        scanDataWithCondition1(null, "student");
    }

    @Test
    public void testScanDataWithCondition3() {
        scanDataWithCondition1(null, "student");
    }

    /**
     * DDL - namespace - create
     *
     * @param namespace 命名空间
     */
    public void createNamespace(String namespace) {
        if (namespace == null) {
            return;
        }
        Admin admin = null;
        try {
            //获取DDL操作对象
            admin = connection.getAdmin();
            //创建命名空间描述器
            NamespaceDescriptor.Builder builder = NamespaceDescriptor.create(namespace);
            NamespaceDescriptor namespaceDescriptor = builder.build();
            //执行创建命名空间操作
            admin.createNamespace(namespaceDescriptor);
        } catch (IOException e) {
            if (e instanceof NamespaceExistException) {
                System.out.println(namespace + "已经存在");
            } else {
                e.printStackTrace();
            }
        } finally {
            try {
                admin.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * DDL - table - create  创建表
     *
     * @param tablename 表名
     * @param cfs       列族参数列表
     */
    public void createTable(String namespace, String tablename, String... cfs) {
        if (tablename == null) {
            System.err.println("表名不能为空");
            return;
        }
        if (cfs.length < 1) {
            System.err.println("至少指定一个列族");
            return;
        }
        Admin admin = null;
        try {
            admin = connection.getAdmin();
            //判断表是否存在
            if (admin.tableExists(TableName.valueOf(namespace, tablename))) {
                System.err.println(namespace + ":" + tablename + "已经存在");
                return;
            }
            //获取 TableDescriptorBuilder 对象
            TableDescriptorBuilder tableDescriptorBuilder
                    = TableDescriptorBuilder.newBuilder(TableName.valueOf(namespace, tablename));
            //设置列族
            for (String cf : cfs) {
                ColumnFamilyDescriptorBuilder columnFamilyDescriptorBuilder
                        = ColumnFamilyDescriptorBuilder.newBuilder(Bytes.toBytes(cf));
                ColumnFamilyDescriptor columnFamilyDescriptor = columnFamilyDescriptorBuilder.build();
                tableDescriptorBuilder.setColumnFamily(columnFamilyDescriptor);
            }
            //获取 TableDescriptor 对象
            TableDescriptor tableDescriptor = tableDescriptorBuilder.build();
            admin.createTable(tableDescriptor);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                admin.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * DDL - table - drop  删除表
     *
     * @param namespace 命名空间
     * @param tablename 表名
     */
    public void dropTable(String namespace, String tablename) {
        if (tablename == null) {
            System.err.println("表名不能为空");
        }
        Admin admin = null;

        try {
            admin = connection.getAdmin();
            //判断表是否存在
            if (!admin.tableExists(TableName.valueOf(namespace, tablename))) {
                System.err.println(namespace + ":" + tablename + "不存在");
                return;
            }
            //先 disable
            admin.disableTable(TableName.valueOf(namespace, tablename));
            //后 drop
            admin.deleteTable(TableName.valueOf(namespace, tablename));
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                admin.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * DML - table - put  插入数据
     *
     * @param namespace 命名空间
     * @param tablename 表名
     * @param rowkey    行键
     * @param cf        列族
     * @param cl        列名
     * @param value     列值
     */
    public void putDataToTable(String namespace, String tablename, String rowkey, String cf, String cl, String value) {
        if (tablename == null || rowkey == null || cf == null || cl == null || value == null) {
            System.out.println("参数不合法");
            return;
        }
        Table table = null;
        try {
            table = connection.getTable(TableName.valueOf(namespace, tablename));
            Put put = new Put(Bytes.toBytes(rowkey));
            put.addColumn(Bytes.toBytes(cf), Bytes.toBytes(cl), Bytes.toBytes(value));
            table.put(put);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                table.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * DML - table - delete  删除表中某条数据
     *
     * @param namespace 命名空间
     * @param tablename 表名
     * @param rowkey    行键
     * @param cf        列族
     * @param cl        列名
     */
    public void deleteData(String namespace, String tablename, String rowkey, String cf, String cl) {
        Table table = null;
        try {
            table = connection.getTable(TableName.valueOf(namespace, tablename));
            Delete delete = new Delete(Bytes.toBytes(rowkey));
//            delete.addColumn();  //删除最近版本
            delete.addColumns(Bytes.toBytes(cf), Bytes.toBytes(cl));
            table.delete(delete);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                table.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * DML - table - get  查询某条数据
     *
     * @param namespace 命名空间
     * @param tablename 表名
     * @param rowkey    行键
     * @param cf        列族
     * @param cl        列名
     */
    public static void getData(String namespace, String tablename, String rowkey, String cf, String cl) {
        Table table = null;
        try {
            table = connection.getTable(TableName.valueOf(namespace, tablename));

            Get get = new Get(Bytes.toBytes(rowkey));
//            get.addColumn(); //查询某个列族的某个列的数据
//            get.addFamily(); //查询某个列族的数据
            Result result = table.get(get);
            Cell[] cells = result.rawCells();
            for (Cell cell : cells) {
//                System.out.println("CF:" + Bytes.toString(cell.getFamilyArray()) +
//                        ",CN:" + Bytes.toString(cell.getQualifierArray()) +
//                        ",Value:" + Bytes.toString(cell.getValueArray()));
                System.out.println("ROW:" + Bytes.toString(CellUtil.cloneRow(cell)) +
                        " CF:" + Bytes.toString(CellUtil.cloneFamily(cell)) +
                        " CL:" + Bytes.toString(CellUtil.cloneQualifier(cell)) +
                        " VALUE:" + Bytes.toString(CellUtil.cloneValue(cell)));
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                table.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * DML - table - scan 条件查询表内数据
     *
     * @param namespace 命名空间
     * @param tablename 表名
     * @param start     开始位置
     * @param stop      结束位置
     */
    public void scanData(String namespace, String tablename, String start, String stop) {
        Table table = null;
        try {
            table = connection.getTable(TableName.valueOf(namespace, tablename));
            Scan scan = new Scan();
            scan.withStartRow(Bytes.toBytes(start)).withStartRow(Bytes.toBytes(stop));
            ResultScanner scanner = table.getScanner(scan);

            for (Result result : scanner) {
                Cell[] cells = result.rawCells();
                for (Cell cell : cells) {
                    System.out.println("ROW:" + Bytes.toString(CellUtil.cloneRow(cell)) +
                            " CF:" + Bytes.toString(CellUtil.cloneFamily(cell)) +
                            " CL:" + Bytes.toString(CellUtil.cloneQualifier(cell)) +
                            " VALUE:" + Bytes.toString(CellUtil.cloneValue(cell)));
                }
                System.out.println("----------------------------------------");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * DML - table - scan (了解)
     * <p>
     * 带条件的scan
     * 1. 查询rowkey中包含字母"2"
     * <p>
     * 2. 查询性别为female的数据
     * <p>
     * 3.  1 and 2
     */
    public void scanDataWithCondition1(String namespace, String tablename) {
        Table table = null;
        try {
            table = connection.getTable(TableName.valueOf(namespace, tablename));
            Scan scan = new Scan();
            //1.查询 rowkey 中包含字母"5"
            SubstringComparator substringComparator = new SubstringComparator("5");
            RowFilter rowFilter = new RowFilter(CompareOperator.EQUAL, substringComparator);

            //添加过滤
            scan.setFilter(rowFilter);
            ResultScanner scanner = table.getScanner(scan);

            for (Result result : scanner) {
                Cell[] cells = result.rawCells();
                for (Cell cell : cells) {
                    System.out.println("ROW:" + Bytes.toString(CellUtil.cloneRow(cell)) +
                            " CF:" + Bytes.toString(CellUtil.cloneFamily(cell)) +
                            " CL:" + Bytes.toString(CellUtil.cloneQualifier(cell)) +
                            " VALUE:" + Bytes.toString(CellUtil.cloneValue(cell)));
                }
                System.out.println("---------------------------------------------------");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                table.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void scanDataWithCondition2(String namespace, String tablename) {
        Table table = null;
        try {
            table = connection.getTable(TableName.valueOf(namespace, tablename));
            Scan scan = new Scan();
            //2. 查询性别为male的数据
            SingleColumnValueFilter singleColumnValueFilter
                    = new SingleColumnValueFilter(Bytes.toBytes("info"),
                    Bytes.toBytes("sex"),
                    CompareOperator.EQUAL,
                    Bytes.toBytes("male"));
            singleColumnValueFilter.setFilterIfMissing(true);
            //添加过滤
            scan.setFilter(singleColumnValueFilter);
            ResultScanner scanner = table.getScanner(scan);

            for (Result result : scanner) {
                Cell[] cells = result.rawCells();
                for (Cell cell : cells) {
                    System.out.println("ROW:" + Bytes.toString(CellUtil.cloneRow(cell)) +
                            " CF:" + Bytes.toString(CellUtil.cloneFamily(cell)) +
                            " CL:" + Bytes.toString(CellUtil.cloneQualifier(cell)) +
                            " VALUE:" + Bytes.toString(CellUtil.cloneValue(cell)));
                }
                System.out.println("---------------------------------------------------");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                table.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void scanDataWithCondition3(String namespace, String tablename) {
        Table table = null;
        try {
            table = connection.getTable(TableName.valueOf(namespace, tablename));
            Scan scan = new Scan();

            //3. 1 and 2
            FilterList filterList =
                    new FilterList(FilterList.Operator.MUST_PASS_ALL,
                            new RowFilter(CompareOperator.EQUAL, new SubstringComparator("5")),
                            new SingleColumnValueFilter(Bytes.toBytes("info"), Bytes.toBytes("sex"), CompareOperator.EQUAL, Bytes.toBytes("male"))
                    );
            //添加过滤
            scan.setFilter(filterList);
            ResultScanner scanner = table.getScanner(scan);

            for (Result result : scanner) {
                Cell[] cells = result.rawCells();
                for (Cell cell : cells) {
                    System.out.println("ROW:" + Bytes.toString(CellUtil.cloneRow(cell)) +
                            " CF:" + Bytes.toString(CellUtil.cloneFamily(cell)) +
                            " CL:" + Bytes.toString(CellUtil.cloneQualifier(cell)) +
                            " VALUE:" + Bytes.toString(CellUtil.cloneValue(cell)));
                }

                System.out.println("---------------------------------------------------");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                table.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
