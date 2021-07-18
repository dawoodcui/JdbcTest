package com.atguigu.jdbcutils;


import com.atguigu.mysqlbase.DruidTest;
import org.apache.commons.dbutils.DbUtils;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/*
        数据库操作的工具类
 */
public class JdbcUtils {

    /*
         获取数据的链接。
     */
    //工具类一般使用静态方法，方便直接调用。（理解：防止重复实例化浪费资源）
    //Connection为返回类型，返回为数据库链接类型。

    public static Connection getConnection() throws Exception {

        /*原始方法：

        FileInputStream fis = new FileInputStream("src/jdbc.properties");
        Properties pros = new Properties();
        pros.load(fis);

        String user = pros.getProperty("username");
        String password = pros.getProperty("password");
        String url = pros.getProperty("url");
        String driverClass = pros.getProperty("driverClass");

        //Driver有静态代码，自动注册驱动“registerDriver”;
        Class.forName(driverClass);

        Connection conn = DriverManager.getConnection(url, user, password);
        return conn;
        */
        /*
            Druid方法：
         */
        DruidTest druidTest = new DruidTest();
        Connection conn = druidTest.getConnection();
        return conn;
    }


    /*
        资源关闭的方法。

     */

    //一般常用的方法为PrepareStatement,但常规设计一般都选择大一些的类，反正也通用，也方便使用。
    public static void  closeResoure(Connection conn, Statement st){
        try {
            if (st != null)
                st.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            if (conn != null)
                conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    //添加 增加result set的关闭。
    public static void  closeResoure(Connection conn, Statement sm, ResultSet rs){

        DbUtils.closeQuietly(conn);
        DbUtils.closeQuietly(sm);
        DbUtils.closeQuietly(rs);
//        try {
//            if (st != null)
//                st.close();
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//        try {
//            if (conn != null)
//                conn.close();
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//        try {
//            if (rs != null)
//                rs.close();
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
    }
}
