package com.atguigu.jdbcutils;

import org.junit.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Random;
/*
    批量插入练习
 */
public class InsertTest {
    @Test
    public void test2() {
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            long start = System.currentTimeMillis();


            conn = JdbcUtils.getConnection();
            String sql = "INSERT INTO stuinfo (name) VALUES (?)";
            ps = conn.prepareStatement(sql);
            for (int i=0;i<1000000;i++) {
                ps.setObject(1, "aaac"+i);
                //1、积攒sql
                    //如果要使用批处理，需要在链接url中加入参数，否则不支持。
                        //rewriteBatchedStatements=true
                ps.addBatch();
                if (i % 100000 == 0){

                    //2、执行bath
                    ps.executeBatch();
                    //3、清空bath
                    ps.clearBatch();
                }
            }
            //最后多执行一次，避免遗漏。

            ps.executeBatch();
            ps.clearBatch();
            //ps.execute();

            System.out.println("使用的时间为："+ (System.currentTimeMillis()-start));
            //100条顺序执行，                5679；
            //1000条一次性执行，              612
            //10000条一次性执行，             1044
            //100000条一次性执行，            1820
            //1000000条一次性执行，           8128
            //10000000条一次性执行，          74768
            //10000000条,10次性执行，         65027
            //
        } catch (Exception e) {
            e.printStackTrace();
            try {
                conn.rollback();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
        } finally {
            JdbcUtils.closeResoure(conn,ps);
        }


    }

    @Test
    public void test1() {
        Random r = new Random(1);
        for (int i = 0; i < 5; i++) {
            int ran1 = r.nextInt(100);
            System.out.println(ran1);
        }
    }
}