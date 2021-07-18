package com.atguigu.been;

import java.sql.Date;

/*
    orm编程思想（object relational mapping）
    一个数据表对应一个java类。
    表中的一条记录对应Java的一个对象(实例)
    表中的一个字段对应java的一个属性
 */
public class Customer {
    private int id;
    private String name;
    private String email;
    private Date birth;

    public Customer() {
    }

    public Customer(int id, String name, String email, Date birht) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.birth = birht;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Date getBirht() {
        return birth;
    }

    public void setBirth(Date birht) {
        this.birth = birht;
    }

    @Override
    public String toString() {
        return "Customer{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", birth=" + birth +
                '}';
    }
}
