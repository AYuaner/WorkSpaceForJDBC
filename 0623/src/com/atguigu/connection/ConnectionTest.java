package com.atguigu.connection;

import org.junit.Test;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * @author A_Yuan
 * @create 2021-06-23 11:20
 */
public class ConnectionTest {

    //方式一
    @Test
    public void testConnection1() throws SQLException {

        //获取Driver实现类对象
        Driver driver = new com.mysql.cj.jdbc.Driver();

        /*
        jdbc:mysql  协议
        localhost   ip地址
        3306        默认mysql端口
        test        test数据库
         */
        String url = "jdbc:mysql://localhost:3306/test";

        //将数据库的用户名和密码封装进Properties
        Properties info = new Properties();
        info.setProperty("user", "root");
        info.setProperty("password", "ayuan520.");

        Connection connect = driver.connect(url, info);

        System.out.println(connect);
    }

    //方式二：对方式一的迭代   相对于方法一，程序中不出现第三方的API，使程序具有更好的移植性
    @Test
    public void ConnectionTest2() throws Exception {

        //使用反射获取Driver实现类的对象
        Class clazz = Class.forName("com.mysql.cj.jdbc.Driver");
        Driver driver = (Driver) clazz.getDeclaredConstructor().newInstance();

        String url = "jdbc:mysql://localhost:3306/test";

        Properties info = new Properties();
        info.setProperty("user", "root");
        info.setProperty("password", "ayuan520.");

        Connection connect = driver.connect(url, info);

        System.out.println(connect);
    }

    //方式三：使用DriverManager来代替Driver
    @Test
    public void ConnectionTest3() throws Exception {

        //获取Driver实现类对象
        Class clazz = Class.forName("com.mysql.cj.jdbc.Driver");
        Driver driver = (Driver) clazz.getDeclaredConstructor().newInstance();

        //注册驱动
        DriverManager.registerDriver(driver);

        String url = "jdbc:mysql://localhost:3306/test";

        Properties info = new Properties();
        info.setProperty("user", "root");
        info.setProperty("password", "ayuan520.");

        //获取连接
        Connection connection = DriverManager.getConnection(url, info);

        System.out.println(connection);

    }

    //方式四：在注册驱动部分进行优化
    @Test
    public void ConnectionTest4() throws Exception {

        String url = "jdbc:mysql://localhost:3306/test";
        Properties info = new Properties();
        info.setProperty("user", "root");
        info.setProperty("password", "ayuan520.");


        //优化过的 注册驱动 ：在mysql的Driver.class中有静态代码块，自动注册了驱动
        Class.forName("com.mysql.cj.jdbc.Driver");


        Connection conn = DriverManager.getConnection(url, info);
        System.out.println(conn);
    }

    //方式五：将连接数据库所需要的4个基本信息声明在配置文件中，通过读取配置文件的方式，获取连接
    /*
    1、实现了数据和代码的分离，实现了解耦
    2、如果需要修改配置文件，可以避免程序重新打包
     */
    @Test
    public void ConnectionTest5() throws Exception {

        //从配置文件获取4个基本信息
        InputStream is = ConnectionTest.class.getClassLoader().getResourceAsStream("jdbc.properties");

        Properties pro = new Properties();
        pro.load(is);

        String user = pro.getProperty("user");
        String password = pro.getProperty("password");
        String url = pro.getProperty("url");
        String driverClass = pro.getProperty("driverClass");

        //加载驱动
        Class.forName(driverClass);

        //获取连接
        Connection connection = DriverManager.getConnection(url, user, password);
        System.out.println(connection);

    }

}
