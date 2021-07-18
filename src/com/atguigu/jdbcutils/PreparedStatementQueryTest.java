package com.atguigu.jdbcutils;

import com.atguigu.been.Customer;
import org.junit.Test;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;


/*
    通用查询方法。
    使用了泛型类，可以带入任何sql语句和对应的java对象。
 */
public class PreparedStatementQueryTest {

    @Test
    public void test1(){
        String sql = "select birth,id,name,email from customers where id > ?";
        ArrayList<Customer> list = getForList(Customer.class, sql,0);
        list.forEach(System.out::println);
    }


    //public Object getInstance(String sql,Object ...args){
    public <T> ArrayList<T> getForList(Class<T> clazz, String sql, Object ...args){

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

                //创建arraylist对象数组
            ArrayList<T> lists = new ArrayList<T>();

            //遍历每条数
            //次方法只适合结果为单条的(如ID = X ,limit1)的查询，下面使用while循环遍历每条数据。
            //if(resultSet.next()){
            //遍历每条数
            while(resultSet.next()){

                //实例化对象，好让数据放进去。
                //Customer customer = new Customer();
                //上一条语句限制死了 表和对象，所以改用泛型来适配任意表和对象。
                T t = clazz.newInstance();

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
                    field.set(t,value);
                }
                //将处理好的对象，放入list当中
                lists.add(t);
            }
            //将赋值后的对象返回输出。
            return lists;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JdbcUtils.closeResoure(conn,ps,resultSet);
            //finally为必须执行，不要将returnll放入进来。
        }
        //如果没查询到内容，那么返回null，因为执行return既结束方法，所以不会有1个以上的return执行。
        return null;
    }
}
