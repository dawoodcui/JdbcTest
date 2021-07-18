package com.atguigu.mysqlbase;

import com.mysql.jdbc.Driver;
import org.junit.Test;

import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;

import static java.lang.Class.forName;

/*
*mysql基础链接方法。
*
*
*
 */
public class base {

    //方法1，最原始
    @Test
    public void test1() throws Exception {

        //创建驱动对象实例
        Driver driver = new Driver();//此为Driver为：com.mysql.jdbc.Driver
        //设置链接信息
        String url = "jdbc:mysql://qdm765045670.my3w.com:3306/qdm765045670_db";
        Properties info = new Properties();
        info.setProperty("user","qdm765045670");
        info.setProperty("password","3WYA%Ensk$zobE32EmGummk5Dv875v");

        Connection conn = driver.connect(url, info);
        System.out.println(conn);
    }



    //使用反射，方便移植不同数据库。
    @Test
    public void test2() throws Exception {

        //使用反射，获取Driver实现类对象
        Class clazz = forName("com.mysql.jdbc.Driver");
        Driver driver = (Driver) clazz.newInstance();

        String url  = "jdbc:mysql://qdm765045670.my3w.com:3306/qdm765045670_db";
        Properties info = new Properties();
        info.setProperty("user","qdm765045670");
        info.setProperty("password","3WYA%Ensk$zobE32EmGummk5Dv875v");

        //获取链接
        Connection conn = driver.connect(url, info);
        System.out.println(conn);
    }

    //使用DriverManager链接
    @Test
    public void test3() throws Exception {
        //创建反射对象
        Class clazz = forName("com.mysql.jdbc.Driver");

        //实例化反射对象(driver驱动)
        Driver driver = (Driver)clazz.getDeclaredConstructor().newInstance();

        //注册驱动
        DriverManager.registerDriver(driver);

        //建立并获取链接
        Connection conn = DriverManager.getConnection("jdbc:mysql://qdm765045670.my3w.com:3306/qdm765045670_db", "qdm765045670", "3WYA%Ensk$zobE32EmGummk5Dv875v");

        System.out.println(conn);
    }

    @Test
    public void test4() throws Exception {
        String url  = "jdbc:mysql://qdm765045670.my3w.com:3306/qdm765045670_db";
        String user = "qdm765045670";
        String password = "3WYA%Ensk$zobE32EmGummk5Dv875v";

        //Driver有静态代码，自动注册驱动“registerDriver”;
        //仅mysql可省略 Class.forName("com.mysql.jdbc.Driver");//mysql-connector-java-5.1.37-bin.jar!\META-INF\services\java.sql.Driver 调用mysql驱动jar包时自动加载并注册驱动
        /*
            static {
                    try {
                        java.sql.DriverManager.registerDriver(new Driver());
                    } catch (SQLException E) {
                        throw new RuntimeException("Can't register driver!");
                    }
                }
         */

        //实例化反射对象(driver驱动)
        //Driver driver = (Driver)clazz.getDeclaredConstructor().newInstance();

        //注册驱动
        //DriverManager.registerDriver(driver);

        Connection conn = DriverManager.getConnection(url, user, password);

        System.out.println(conn);

    }


    @Test
    public void test5() throws Exception {
        FileInputStream fis = new FileInputStream("src/jdbc.properties");
        Properties pros = new Properties();
        pros.load(fis);

        String user = pros.getProperty("username");
        String password = pros.getProperty("password");
        String url = pros.getProperty("url");
        String driverClass = pros.getProperty("driverClass");

        //com.mysql.jdbc.Driver
        //Driver有静态代码，自动注册驱动“registerDriver”;
        //java.sql.DriverManager.registerDriver(new Driver());
        Class.forName(driverClass);

        Connection conn = DriverManager.getConnection(url, user, password);
        System.out.println(conn);
    }
}
