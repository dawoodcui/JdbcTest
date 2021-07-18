package com.atguigu.dbutils;

import com.atguigu.been.Customer;
import com.atguigu.jdbcutils.JdbcUtils;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.MapHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;
import org.junit.Test;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public class QueryRunnerTest {

    @Test
    public void testInsert() throws Exception {

        Connection conn = null;
        try {
            QueryRunner runner = new QueryRunner();
            conn = JdbcUtils.getConnection();
            String sql = "insert into customers (name,email,birth) values (?,?,?)";
            int insertCount = runner.update(conn, sql,  "测试名1", "tes1t@163.com", "2011-05-05");
            System.out.println("添加："+insertCount+" 条记录");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JdbcUtils.closeResoure(conn,null);
        }


    }
    @Test
    public void testSelect() throws Exception {
        Connection conn = null;
        try {
            QueryRunner runner = new QueryRunner();
            conn = JdbcUtils.getConnection();
            String sql = "select id,name,email,birth from customers where id = ?";
            Customer customer = runner.query(conn, sql, new BeanHandler<Customer>(Customer.class), 1);
            System.out.println(customer);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JdbcUtils.closeResoure(conn,null);
        }

    }
    @Test
    public void testSelectList() throws Exception {

        Connection conn = null;
        try {
            QueryRunner runner = new QueryRunner();
            conn = JdbcUtils.getConnection();

            String sql = "select id,name,email,birth from customers where id = ?";
            BeanListHandler<Customer> customerBeanListHandler = new BeanListHandler<>(Customer.class);
            List<Customer> customer = runner.query(conn, sql, customerBeanListHandler, 1);
            System.out.println(customer);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JdbcUtils.closeResoure(conn,null);
        }

    }



    @Test
    public void testSelectMap() throws Exception {

        Connection conn = null;
        try {
            QueryRunner runner = new QueryRunner();
            conn = JdbcUtils.getConnection();
            String sql = "select id,name,email,birth from customers where id = ?";
            MapHandler MapHandler = new MapHandler();
            Map<String, Object> map = runner.query(conn, sql, MapHandler, 1);
            System.out.println(map);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JdbcUtils.closeResoure(conn,null);
        }
    }



    @Test
    public void testScalar() throws Exception {

        Connection conn = null;
        try {
            QueryRunner runner = new QueryRunner();
            conn = JdbcUtils.getConnection();
            String sql = "select count(*) from customers";
            ScalarHandler scalarHandler = new ScalarHandler();
            Object scalar = runner.query(conn, sql, scalarHandler);
            System.out.println(scalar);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JdbcUtils.closeResoure(conn,null);
        }

    }
    @Test


    public void testScalar2() throws Exception {

        Connection conn = null;
        try {
            QueryRunner runner = new QueryRunner();
            conn = JdbcUtils.getConnection();
            String sql = "select count(*) from customers";
            ResultSetHandler handler = new ResultSetHandler<Object>(){
                @Override
                public Object handle(ResultSet rs) throws SQLException {
                    if(rs.next()){
                        System.out.println(rs.getObject(1));
                    }
                    return rs;
                }
            };
            Object results = runner.query(conn, sql, handler);
            System.out.println(results);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JdbcUtils.closeResoure(conn,null);
        }

    }
}
