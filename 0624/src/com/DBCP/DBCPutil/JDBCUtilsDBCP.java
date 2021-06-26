package com.DBCP.DBCPutil;

import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.commons.dbcp2.BasicDataSourceFactory;
import org.junit.Test;

import java.io.InputStream;
import java.sql.Connection;
import java.util.Properties;

/**
 * @author A_Yuan
 * @create 2021-06-25 23:48
 */
public class JDBCUtilsDBCP {

    private static BasicDataSource source;

    static {
        try {
            Properties pros = new Properties();
            InputStream is = ClassLoader.getSystemClassLoader().getResourceAsStream("dbcp.properties");
            pros.load(is);
            source = BasicDataSourceFactory.createDataSource(pros);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 使用DBCP数据库连接池技术 获取连接
     * @return
     * @throws Exception
     */
    @Test
    public static Connection getConnection() throws Exception {
        Connection conn = source.getConnection();
        return conn;
    }
}
