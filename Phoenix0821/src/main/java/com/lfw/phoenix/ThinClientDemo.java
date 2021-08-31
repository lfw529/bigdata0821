package com.lfw.phoenix;

import org.apache.phoenix.queryserver.client.ThinClientUtil;
import java.sql.*;

/**
 * Thin client
 * <p>
 * jdbc的步骤: 加载驱动、获取连接、编写SQL、预编译、设置参数、 执行SQL、 结果封装 、 关闭连接
 */
public class ThinClientDemo {
    public static void main(String[] args) throws SQLException {
        // 加载驱动
        //Class.forName("com.mysql.jdbc.Driver");
        //获取连接
        String url = ThinClientUtil.getConnectionUrl("hadoop102", 8765);
        System.out.println(url);
        //String url = "jdbc:phoenix:thin:url=http://hadoop102:8765" ;
        Connection connection = DriverManager.getConnection(url);

        //编写SQL
        String sql = "select * from STUDENT1";

        //预编译
        PreparedStatement ps = connection.prepareStatement(sql);

        // 执行sql
        ResultSet rs = ps.executeQuery();

        //封装结果
        while (rs.next()) {
            System.out.println( //注意：jdbc 的下标从1开始
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
