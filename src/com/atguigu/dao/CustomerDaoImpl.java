package com.atguigu.dao;

import com.atguigu.been.Customer;

import java.sql.Connection;
import java.sql.Date;
import java.util.List;

public class CustomerDaoImpl extends BaseDAO implements CustomerDao {
    @Override
    public void insert(Connection conn, Customer cust) {

        String sql = "insert into customers(name,email,birth) value(?,?,?)";
        update(conn,sql,cust.getName(),cust.getEmail(),cust.getBirht());
    }

    @Override
    public void deleteById(Connection conn, int id) {

        String sql = "delete from customers where id = ?";
        update(conn,sql,id);

    }

    @Override
    public void update(Connection conn, Customer cust) {
        String sql = "update customers set name = ? , email= ? ,birth =? where id =?";
        update(conn,sql,cust.getName(),cust.getEmail(),cust.getBirht(),cust.getId());
    }

    @Override
    public Customer getCustomerById(Connection conn, int id) {
        String sql = "select id,name,email,birth from customers where id = ?";
        return getInstance(conn,Customer.class,sql,id);
    }

    @Override
    public List<Customer> getAll(Connection conn){
        String sql = "select id,name,email,birth from customers";
        List<Customer> list = getForList(conn, Customer.class, sql);
        return list;
    }

    @Override
    public Long getCount(Connection conn) {
        String sql = "select count(*) from customers";
        return getValue(conn,sql);
    }

    @Override
    public Date getMaxBirth(Connection conn) {
        String sql = "select max(birth) from customers";
        return getValue(conn,sql);
    }

}
