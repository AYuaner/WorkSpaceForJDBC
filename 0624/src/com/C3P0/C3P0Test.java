package com.C3P0;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import com.mchange.v2.c3p0.DataSources;
import org.junit.Test;

import java.sql.Connection;

/**
 * @author A_Yuan
 * @create 2021-06-25 21:53
 */
public class C3P0Test {

    //硬编码方式
    @Test
    public void testGetConnection() throws Exception {
        //获取C3P0数据库连接池
        ComboPooledDataSource cpds = new ComboPooledDataSource();
        //设置驱动
        cpds.setDriverClass("com.mysql.cj.jdbc.Driver");
        //设置连接的URL
        cpds.setJdbcUrl("jdbc:mysql://localhost:3306/test");
        //配置用户名
        cpds.setUser("root");
        //配置密码
        cpds.setPassword("ayuan520.");

        //**********通过设置相关参数，对数据库连接池进行管理***********
        //设置初始时数据库连接池中的连接数
        cpds.setInitialPoolSize(5);

        //连接测试
        Connection conn = cpds.getConnection();
        System.out.println(conn);

        //销毁连接池
        DataSources.destroy(cpds);
    }

    //使用配置文件
    @Test
    public void testGetConnection1() throws Exception {
        ComboPooledDataSource cpds = new ComboPooledDataSource("helloc3p0");
        Connection conn = cpds.getConnection();
        System.out.println(conn);
    }

}
