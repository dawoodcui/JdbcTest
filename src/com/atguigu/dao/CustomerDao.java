package com.atguigu.dao;

import com.atguigu.been.Customer;

import java.sql.Connection;
import java.sql.Date;
import java.util.List;

/*
    此接口用于规范 Customers 表的规范。
 */
public interface CustomerDao {

    void insert(Connection conn, Customer cust);

    void deleteById(Connection conn,int id);

    void update(Connection conn,Customer cust);

    Customer getCustomerById(Connection conn,int id);

    List<Customer> getAll(Connection conn);

    Long getCount(Connection conn);

    Date getMaxBirth(Connection conn);



}
