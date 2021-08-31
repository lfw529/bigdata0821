package com.lfw.hive;

import org.apache.hadoop.hive.ql.exec.UDFArgumentException;
import org.apache.hadoop.hive.ql.metadata.HiveException;
import org.apache.hadoop.hive.ql.udf.generic.GenericUDTF;
import org.apache.hadoop.hive.serde2.objectinspector.ObjectInspector;
import org.apache.hadoop.hive.serde2.objectinspector.ObjectInspectorFactory;
import org.apache.hadoop.hive.serde2.objectinspector.StructObjectInspector;
import org.apache.hadoop.hive.serde2.objectinspector.primitive.PrimitiveObjectInspectorFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * 输入数据：hello,atguigu:hello,hive
 * 输出数据：
 * hello atguigu
 * hello hive
 */
public class MyUDTF2 extends GenericUDTF {

    private ArrayList<String> outList = new ArrayList<>();

    @Override
    public StructObjectInspector initialize(StructObjectInspector argOIs) throws UDFArgumentException {
        //1.定义输出数据的列名和类型
        List<String> fieldNames = new ArrayList<>();
        List<ObjectInspector> fieldOIs = new ArrayList<>();

        //2.添加输出数据的列名和类型(可以别名覆盖)
        fieldNames.add("word1");
        fieldNames.add("word2");

        //输出数据的类型
        fieldOIs.add(PrimitiveObjectInspectorFactory.javaStringObjectInspector);
        fieldOIs.add(PrimitiveObjectInspectorFactory.javaStringObjectInspector);

        return ObjectInspectorFactory.getStandardStructObjectInspector(fieldNames,fieldOIs);
    }

    //hello,atguigu:hello,hive
    @Override
    public void process(Object[] args) throws HiveException {
        //1.获取输入数据
        String input = args[0].toString();
        //2.按照“,”分割字符串
        String[] fields = input.split(":");      //hello,atguigu
                                                        // hello,hive
        //3.遍历数据写出
        for(String field : fields){
            //清空集合
            outList.clear();
            //将field按照“，”分割
            String[] words = field.split(",");

            //将数据放入集合
            outList.add(words[0]);
            outList.add(words[1]);
            //输出数据
            forward(outList);
        }
    }

    //收尾方法
    @Override
    public void close() throws HiveException {

    }
}
