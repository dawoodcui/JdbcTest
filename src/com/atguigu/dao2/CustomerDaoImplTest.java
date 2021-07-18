package com.atguigu.dao2;


import com.atguigu.been.Customer;
import com.atguigu.jdbcutils.JdbcUtils;
import org.junit.Test;

import java.sql.Connection;
import java.sql.Date;
import java.util.List;

public class CustomerDaoImplTest {


    private CustomerDaoImpl dao = new CustomerDaoImpl();

    @Test
    public void insert(){

        Connection conn =  null;
        try {
            conn = JdbcUtils.getConnection();
            Customer cust = new Customer(1,"晓飞","xiaofei@126.com",new Date(121233423L));
            dao.insert(conn,cust);
            System.out.println("添加成功！");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JdbcUtils.closeResoure(conn,null);
        }

    }

    @Test
    public void deleteById() {

        Connection conn =  null;
        try {
            conn = JdbcUtils.getConnection();
            dao.deleteById(conn,4);
            System.out.println("删除成功！");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JdbcUtils.closeResoure(conn,null);
        }
    }

    @Test
    public void update() {

        Connection conn =  null;
        try {
            conn = JdbcUtils.getConnection();
            Customer cust = new Customer(1,"晓飞","xiaofei@126.com",new Date(1212133233423L));
            dao.update(conn,cust);
            System.out.println("更新成功！");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JdbcUtils.closeResoure(conn,null);
        }

    }

    @Test
    public void getCustomerById() {

        Connection conn =  null;
        try {
            conn = JdbcUtils.getConnection();
            Customer customer = dao.getCustomerById(conn, 2);
            System.out.println(customer);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JdbcUtils.closeResoure(conn,null);
        }

    }

    @Test
    public void getAll() {

        Connection conn =  null;
        try {
            conn = JdbcUtils.getConnection();
            List<Customer> all = dao.getAll(conn);
            all.forEach(System.out::println);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JdbcUtils.closeResoure(conn,null);
        }
    }

    @Test
    public void getCount() {

        Connection conn =  null;
        try {
            conn = JdbcUtils.getConnection();
            Long count = dao.getCount(conn);
            System.out.println("共计"+count+"条数据.");

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JdbcUtils.closeResoure(conn,null);
        }
    }

    @Test
    public void getMaxBirth() {

        Connection conn =  null;
        try {
            conn = JdbcUtils.getConnection();
            Date maxBirth = dao.getMaxBirth(conn);
            System.out.println("最大的生日为：" + maxBirth);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JdbcUtils.closeResoure(conn,null);
        }
    }
}
