package com.lfw.phoenix;

import java.sql.*;
import java.util.Properties;

public class ThickClientDemo {
    public static void main(String[] args) throws SQLException {
        String url = "jdbc:phoenix:hadoop102";

        Properties props = new Properties();
        props.put("phoenix.schema.isNamespaceMappingEnabled", "true");

        Connection connection = DriverManager.getConnection(url, props);

        //编写SQL
        String sql = "select * from student1";

        //预编译
        PreparedStatement ps = connection.prepareStatement(sql);

        // 执行sql
        ResultSet rs = ps.executeQuery();

        //封装结果
        while (rs.next()) {
            System.out.println(
                    rs.getString(1) + ":" +
                            rs.getString(2) + ":" +
                            rs.getString(3));

            //rs.getString("id")
        }
        rs.close();
        ps.close();
        connection.close();
    }
}
