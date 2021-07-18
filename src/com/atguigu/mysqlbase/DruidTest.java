package com.atguigu.mysqlbase;

import com.alibaba.druid.pool.DruidDataSourceFactory;

import javax.sql.DataSource;
import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;
public class DruidTest {

    //每次调用就会生成新的连接池，为避免浪费，所以连接池放入静态代码块。这样随着类的加载而加载(静态代码块只执行一次)，就不会重复创建。
    static DataSource druidDataSource;
    static{

        try {
            FileInputStream fis = new FileInputStream("src/jdbc.properties");
            Properties pros = new Properties();
            pros.load(fis);

            druidDataSource = DruidDataSourceFactory.createDataSource(pros);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    //创建链接
    public static Connection getConnection() throws SQLException {

        Connection conn = druidDataSource.getConnection();

        return conn;

    }
}
