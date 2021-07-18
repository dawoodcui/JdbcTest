package com.atguigu.dao2;

import com.atguigu.been.Customer;
import com.atguigu.jdbcutils.JdbcUtils;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.sql.*;
import java.util.ArrayList;

/*
    此类中的方法，均考虑了事务处理，参数都待Connection conn，由调用方法传入，并且不关闭。
 */
public abstract class BaseDAO<T> {
    //此方法放入父类中，获取“当前执行类”的父类的类型，供反射使用。
    private Class<T> clazz = null;
    {
        //获取当前BaseDao子类，所继承的父类中的泛型。
                //public class CustomerDaoImpl extends BaseDAO<Customer> implements CustomerDao ，取到“<Customer>”；
        Type genericSuperclass = this.getClass().getGenericSuperclass();
        ParameterizedType paramType = (ParameterizedType)genericSuperclass;

        Type[] typeArguments = paramType.getActualTypeArguments();//获取父类的泛型参数
        clazz = (Class<T>) typeArguments[0];


    }


    /*
    通用的增删改查操作+事务
 */
    public int update(Connection conn, String sql, Object... args) {

        //Connection conn = null;
        PreparedStatement ps = null;


        try {
            //1、获取链接
            //不获取链接，事务会话由调用方法管理，不让方法自动管理。
            //conn = JdbcUtils.getConnection();
            //2、创建preparesStatement实例
            ps = conn.prepareStatement(sql);
            //3、填充占位符
            for (int i = 0; i < args.length; i++) {
                ps.setObject(i + 1, args[i]);
            }
            //4、执行int
            return ps.executeUpdate();
        } catch (Exception e) {
            System.out.println("error");
            e.printStackTrace();
        } finally {
            //5、关闭资源
            //链接不自动关闭，使用null
            JdbcUtils.closeResoure(null, ps);
        }
        return 0;
    }


    public  T getInstance(Connection conn,String sql,Object ...args) {

        PreparedStatement ps = null;
        ResultSet resultSet = null;
        try {
            ps = conn.prepareStatement(sql);

            for (int i = 0; i < args.length; i++) {
                ps.setObject(i + 1, args[i]);
            }

            resultSet = ps.executeQuery();

            //获取元数据
            ResultSetMetaData rsmd = resultSet.getMetaData();
            //获取元数据中的列(字段)数
            int columnCount = rsmd.getColumnCount();


            /*
                此方法只适合结果为单条的(如ID = X ,limit1)的查询，所以使用if，另外的方法会使用while循环遍历每条数据。
             */
            if (resultSet.next()) {

                /*
                    实例化对象，好让数据放进去。
                    Customer customer = new Customer();
                */
                //上一条语句限制死了 表和对象，所以改用泛型来适配任意表和对象。
                T t = clazz.newInstance();

                //遍历每条数据的每个字段。
                for (int i = 0; i < columnCount; i++) {
                    //获每一行，每个字段的值，并复制给value
                    Object value = resultSet.getObject(i + 1);

                    //实际开发中，使用Label，获取sql语句中字段的别名，好跟实际的对象属性对应上;如果不写别名，就用默认的字段名。
                    String columnLabel = rsmd.getColumnLabel(i + 1);

                    //使用反射，根据获取到的字段别名(或字段名)，反射出与其相同的属性名。
                    Field field = clazz.getDeclaredField(columnLabel);
                    //设置权限，防止有些属性为私，而有无法操作；
                    field.setAccessible(true);
                    //将value值赋给 字段相对应的实例中的属性。
                    field.set(t, value);
                }
                //将赋值后的对象返回输出。
                return t;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JdbcUtils.closeResoure(conn, ps, resultSet);
            //finally为必须执行，不要将returnll放入进来。
        }
        //如果没查询到内容，那么返回null，因为执行return既结束方法，所以不会有1个以上的return执行。
        return null;
    }


    public ArrayList<T> getForList(Connection conn,String sql, Object... args) {

        PreparedStatement ps = null;
        ResultSet resultSet = null;
        try {
            ps = conn.prepareStatement(sql);

            for (int i = 0; i < args.length; i++) {
                ps.setObject(i + 1, args[i]);
            }

            resultSet = ps.executeQuery();

            ResultSetMetaData rsmd = resultSet.getMetaData();
            int columnCount = rsmd.getColumnCount();

            ArrayList<T> lists = new ArrayList<T>();

            while (resultSet.next()) {

                T t = clazz.newInstance();
                for (int i = 0; i < columnCount; i++) {
                    Object value = resultSet.getObject(i + 1);

                    String columnLabel = rsmd.getColumnLabel(i + 1);

                    Field field = Customer.class.getDeclaredField(columnLabel);
                    field.setAccessible(true);
                    field.set(t, value);
                }
                lists.add(t);
            }
            return lists;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JdbcUtils.closeResoure(null, ps, resultSet);
        }
        return null;
    }


    /*
        适用于特殊的查询 (单条数据)
     */
    public <T> T getValue(Connection conn,String sql, Object ...args){

        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            ps = conn.prepareStatement(sql);
            for (int i = 0; i < args.length;i++){
                ps.setObject(i+1,args[i]);
            }
            rs = ps.executeQuery();
            if(rs.next()){
                return (T) rs.getObject(1);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JdbcUtils.closeResoure(null,ps,rs);
        }

        return null;


    }

}
