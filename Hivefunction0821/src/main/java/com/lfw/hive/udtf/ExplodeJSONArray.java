package com.lfw.hive.udtf;

import org.apache.hadoop.hive.ql.exec.UDFArgumentException;
import org.apache.hadoop.hive.ql.exec.UDFArgumentLengthException;
import org.apache.hadoop.hive.ql.exec.UDFArgumentTypeException;
import org.apache.hadoop.hive.ql.metadata.HiveException;
import org.apache.hadoop.hive.ql.udf.generic.GenericUDTF;
import org.apache.hadoop.hive.serde2.objectinspector.ObjectInspector;
import org.apache.hadoop.hive.serde2.objectinspector.ObjectInspectorFactory;
import org.apache.hadoop.hive.serde2.objectinspector.StructObjectInspector;
import org.apache.hadoop.hive.serde2.objectinspector.primitive.PrimitiveObjectInspectorFactory;
import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

public class ExplodeJSONArray extends GenericUDTF {
    @Override
    public StructObjectInspector initialize(StructObjectInspector argOIs) throws UDFArgumentException {
        //1 约束函数传入参数个数
        if (argOIs.getAllStructFieldRefs().size() != 1) {
            throw new UDFArgumentLengthException("explode_json_array 函数的参数个数只能为1.....");
        }

        //2 约束函数传入参数的类型,第一个必须为 String
        String typeName = argOIs.getAllStructFieldRefs().get(0).getFieldObjectInspector().getTypeName();
        if (!"string".equals(typeName)) {  //判断参数是否为基础数据类型
            throw new UDFArgumentTypeException(0, "explode_json_array 函数的第一个参数的类型只能为 String...");
        }
        //3 约束函数返回值的类型
        List<String> fieldNames = new ArrayList<String>();
        List<ObjectInspector> fieldOIs = new ArrayList<ObjectInspector>();

        fieldNames.add("item");
        fieldOIs.add(PrimitiveObjectInspectorFactory.javaStringObjectInspector);

        return ObjectInspectorFactory.getStandardStructObjectInspector(fieldNames, fieldOIs);

    }

    @Override
    public void process(Object[] args) throws HiveException {
        //1 获取传入的数据
        String jsonArrayStr = args[0].toString();
        //2 将 jsonArrayStr 字符串转换成 json 数组
        JSONArray jsonArray = new JSONArray(jsonArrayStr);
        //3 将jsonArray里面的一个个 json 字符串写出
        for (int i = 0; i < jsonArray.length(); i++) {
            String jsonStr = jsonArray.getString(i);
            //因为初始化方法里面限定了返回值类型是 struct 结构体
            //所以在这个地方不能直接输出 jsonStr，需要用个字符串数组包装下
            String[] result = new String[1];
            result[0] = jsonStr;
            forward(result);
        }
    }

    @Override
    public void close() throws HiveException {

    }
}
