package com.DBCP;

import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.commons.dbcp2.BasicDataSourceFactory;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.sql.Connection;
import java.util.Properties;

/**
 * @author A_Yuan
 * @create 2021-06-25 23:20
 */
public class DBCPTest {

    //硬编码方式
    @Test
    public void testGetConnection() throws Exception {
        //创建DBCP的数据库连接池
        BasicDataSource source = new BasicDataSource();

        //设置基本信息
        source.setDriverClassName("com.mysql.cj.jdbc.Driver");
        source.setUrl("jdbc:mysql://localhost:3306/test");
        source.setUsername("root");
        source.setPassword("ayuan520.");

        //设置数据库连接池的相关属性
        source.setInitialSize(10);
        source.setMaxTotal(10);

        //获取连接
        Connection conn = source.getConnection();
        System.out.println(conn);
    }

    //使用配置文件
    @Test
    public void testGetConnection1() throws Exception {
        Properties pros = new Properties();
        //方式一
        InputStream is = ClassLoader.getSystemClassLoader().getResourceAsStream("dbcp.properties");
        //方式二
        //FileInputStream is = new FileInputStream(new File("dbcp.properties"));
        pros.load(is);
        BasicDataSource source = BasicDataSourceFactory.createDataSource(pros);

        Connection conn = source.getConnection();
        System.out.println(conn);
    }
}
