package com.C3P0.C3P0util;

import com.mchange.v2.c3p0.ComboPooledDataSource;

import java.sql.Connection;

/**
 * @author A_Yuan
 * @create 2021-06-25 22:56
 */
public class JDBCUtilsC3P0 {

    private static ComboPooledDataSource cpds = new ComboPooledDataSource("hellc3p0");

    /**
     * 使用C3P0的数据库连接池技术 获取连接
     * @return
     * @throws Exception
     */
    public static Connection getConnection() throws Exception {
        Connection conn = cpds.getConnection();
        return conn;
    }
}
