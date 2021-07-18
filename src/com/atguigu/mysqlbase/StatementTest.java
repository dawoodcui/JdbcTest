package com.atguigu.mysqlbase;


public class StatementTest {
    public static void main(String[] args) {

//        Scanner scanner = new Scanner(System.in);
//        System.out.print("请输入用户名：");
//        String user = scanner.next();
//        System.out.print("请输入密码：");
//        String password = scanner.next();

        String sql = "SELECT `user`,`password` from user_table where user = 'AA' and `password` = '123456'";
        System.out.println(sql);

    }
}
