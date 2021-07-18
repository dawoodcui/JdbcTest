package com.atguigu.mysqlbase;

/*
    使用PrepareStatement来实现SQL语句查询操作
 */

import com.atguigu.jdbcutils.JdbcUtils;
import org.junit.Test;

import java.io.FileInputStream;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Properties;


public class PrepareStatementUpdateTest {


    //事务测试：
    @Test
    public void test4() throws Exception {
        Connection conn = null;
        try {

            //事务要求在同一个会话session内，所以不能关闭链接，必须全部操作完后，再关闭。
            conn = JdbcUtils.getConnection();
                //设置隔离级别
                //conn.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
            //设置为不自动提交。
            conn.setAutoCommit(false);


            String sql1 = "update user_table set balance = balance - 1000 where user = ?";
            System.out.println(update1(conn,sql1,"AA"));
            //System.out.println(10/0);
            String sql2 = "update user_table set balance = balance + 1000 where user = ?";
            System.out.println(update1(conn,sql2,"BB"));

            //上述方法已将事务要执行的语句处理好，现在进行提交。
            conn.commit();
            System.out.println("转账成功！");
        } catch (Exception e) {
            e.printStackTrace();
            //如果出现异常，进行回滚。此操作也会有异常，也需要try-catch
            try {
                System.out.println("转账失败！");
                conn.rollback();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
        } finally {
            //关闭链接。
            JdbcUtils.closeResoure(conn,null);
        }


    }
    /*
        通用的增删改查操作+事务
     */
    public int update1(Connection conn,String sql, Object ...args) {

        //Connection conn = null;
        PreparedStatement ps = null;

        try {
            //1、获取链接
                //不获取链接，事务会话由调用方法管理，不让方法自动管理。
            //conn = JdbcUtils.getConnection();
            //2、创建preparesStatement实例
            ps = conn.prepareStatement(sql);
            //3、填充占位符
            for (int i =0; i< args.length; i++){
                ps.setObject(i+1,args[i]);
            }
            //4、执行int
            return ps.executeUpdate();
        } catch (Exception e) {
            System.out.println("error");
            e.printStackTrace();
        } finally {
            //5、关闭资源
                //链接不自动关闭，使用null
            JdbcUtils.closeResoure(null,ps);
        }
        return 0;
    }


    //v3:资源链接 + sql执行方法 封装后的方法。
    @Test
    public void test3(){
//        String sql = "delete from customers where id = ? ";
//        update(sql,3);
        String sql = "update customers set `name` = ? where `id` > ?";
        int num = update(sql, "mk1",0);
        System.out.println(num);
    }

    /*
        通用的增删改查操作
     */
    public int update(String sql, Object ...args) {

        Connection conn = null;
        PreparedStatement ps = null;

        try {
            //1、获取链接
            conn = JdbcUtils.getConnection();
            //2、创建preparesStatement实例
            ps = conn.prepareStatement(sql);
            //3、填充占位符
            for (int i =0; i< args.length; i++){
                ps.setObject(i+1,args[i]);
            }
            //4、执行int
            return ps.executeUpdate();
        } catch (Exception e) {
            System.out.println("error");
            e.printStackTrace();
        } finally {
                //修改为自动提交数据，避免其他非事务的方法，操作无法进行。
            //主要针对数据库连接池使用，因为close方法已经关闭会话，但连接池只是回收，此会话不关闭。
            try {
                conn.setAutoCommit(true);
            } catch (SQLException e) {
                e.printStackTrace();
            }
            //5、关闭资源
            JdbcUtils.closeResoure(conn,ps);
        }
        return 0;
    }
    //v2:资源链接方法封装调用。
    @Test
    public void test2()  {
        Connection conn = null;
        PreparedStatement ps = null;

        try {
            //1，获取数据库链接
            conn = JdbcUtils.getConnection();

            //2、预编译sql语句，返回PreparedStatement的实例。
            String sql="update customers set name = ? where id = ?";
            ps = conn.prepareStatement(sql);

            //3、填充占位符

            ps.setString(1,"金吒");
            ps.setInt(2,3);

            //4、执行
            ps.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            //5、关闭资源
            JdbcUtils.closeResoure(conn,ps);
        }


    }
    //v1:过程未封装，全写入到一个方法中。
    @Test
    public void test1(){
        //防止出现错误之前，资源没被创建实例，但finally仍然执行关闭实例操作，而造成的空指针异常。
        Connection conn = null;
        PreparedStatement ps = null;

        try {
            FileInputStream fis = new FileInputStream("src/jdbc.properties");
            Properties pros = new Properties();
            pros.load(fis);

            String user = pros.getProperty("username");
            String password = pros.getProperty("password");
            String url = pros.getProperty("url");
            String driverClass = pros.getProperty("driverClass");

            //Driver有静态代码，自动注册驱动“registerDriver”;
            Class.forName(driverClass);

            conn = DriverManager.getConnection(url, user, password);


            //预编译SQL语句，返回PrepareStatement的实例
            String sql = "insert into customers (name,email,birth) value(?,?,?)";
            ps = conn.prepareStatement(sql);
            ps.setString(1,"哪吒");
            ps.setString(2,"nezha@163.com");

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            java.util.Date  date = sdf.parse("1999-08-04");


            ps.setDate(3,new Date(date.getTime()));

            //执行操作
            ps.execute();

            //关闭资源
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            try {
                if (ps != null)
                    ps.close();
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



    }
}
