package com.atguigu.jdbcutils;

import com.atguigu.been.Customer;
import org.junit.Test;

import java.lang.reflect.Field;
import java.sql.*;

public class CustomersQuery {

    @Test
    public void test2(){
        String sql = "select `id`,`name`,`email`,`birth` from customers where id= ?";
        Customer customer = queryForCustomers(sql,1);
        System.out.println(customer);


        String sql1 = "select `id`,`name`,`email` from customers where id= ?";
        Customer customer1 = queryForCustomers(sql1,2);
        System.out.println(customer1);
    }


    public Customer queryForCustomers(String sql ,Object ...args){
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet resultSet = null;
        try {
            conn = JdbcUtils.getConnection();
            ps = conn.prepareStatement(sql);

            for(int i = 0; i<args.length;i++){
                ps.setObject(i+1,args[i]);
            }

            resultSet = ps.executeQuery();

            //获取元数据
            ResultSetMetaData rsmd = resultSet.getMetaData();
            //获取元数据中的列(字段)数
            int columnCount = rsmd.getColumnCount();

            //遍历每条数
            if(resultSet.next()){

                //实例化对象，好让数据放进去
                Customer customer = new Customer();
                //遍历每条数据的每个字段。
                for(int i = 0 ; i < columnCount; i++){
                    //获每一行，每个字段的值，并复制给value
                    Object value = resultSet.getObject(i + 1);

                    //获取列名(字段名)，否则value无法知道复制角标
                    //String columnName = rsmd.getColumnName(i + 1);
                    //实际开发中，使用Label，获取sql语句中字段的别名，好跟实际的对象属性对应上。
                        //如果不写别名，就用默认的字段名。
                    String columnLabel = rsmd.getColumnLabel(i + 1);

                    //反射上面的实例，好按实际需求放进数据：
                        //将指定列名对应的属性反射出来，好方便赋值操作。
                    Field field = Customer.class.getDeclaredField(columnLabel);
                        //设置权限，防止有些属性为私，而有无法操作；
                    field.setAccessible(true);
                        //将value值赋给 字段相对应的实例中的属性。
                    field.set(customer,value);
                }
                //将赋值后的对象返回输出。
                return customer;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JdbcUtils.closeResoure(conn,ps,resultSet);
            //finally为必须执行，不要将returnll放入进来。
        }
        //如果没查询到内容，那么返回null，因为执行return既结束方法，所以不会有1个以上的return执行。
        return null;



    }

    /*
        select 查询方式1
     */
    @Test
    public void test1(){
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet resultSet = null;

        try {
            conn = JdbcUtils.getConnection();
            String sql = "SELECT id,name,email,birth FROM customers where id = ?";

            ps = conn.prepareStatement(sql);
            ps.setObject(1,1);

            //执行，并返回结果集。
            resultSet = ps.executeQuery();

            if(resultSet.next()){
                int id = resultSet.getInt(1);
                String name = resultSet.getString(2);
                String email = resultSet.getString(3);
                Date birth = resultSet.getDate(4);

                //方式1
                //System.out.println("id="+id+"; name="+name+" ;email="+email+"; birth="+birth);

                //方式2
    //            Object[] objects = new Object[]{id, name, email, birth};
    //            System.out.println(objects.toString());

                //方式3，将结果封装到对象内。

                Customer customer = new Customer(id, name, email, birth);
                System.out.println(customer);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JdbcUtils.closeResoure(conn,ps,resultSet);
        }

    }
}
