package com.util;

import java.io.InputStream;
import java.sql.*;
import java.util.Properties;

/**
 * 操作数据库的工具类
 *
 * @author A_Yuan
 * @create 2021-06-23 15:16
 */
public class JDBCUtils {

    /**
     * 获取数据库的连接
     * @return
     * @throws Exception
     */
    public static Connection getConnection() throws Exception {

        InputStream is = ClassLoader.getSystemClassLoader().getResourceAsStream("jdbc.properties");

        Properties pros = new Properties();
        pros.load(is);

        String user = pros.getProperty("user");
        String password = pros.getProperty("password");
        String url = pros.getProperty("url");
        String driverClass = pros.getProperty("driverClass");

        Class.forName(driverClass);

        Connection conn = DriverManager.getConnection(url, user, password);

        return conn;
    }

    /**
     * 关闭连接和Statement的操作
     * @param conn
     * @param ps
     */
    public static void closeResource(Connection conn, Statement ps) {

        try {
            if (ps!=null) {
                ps.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try {
            if (conn!=null) {
                conn.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * 关闭资源的操作
     * @param conn
     * @param ps
     * @param rs
     */
    public static void closeResource(Connection conn, Statement ps, ResultSet rs) {

        try {
            if (ps!=null) {
                ps.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try {
            if (conn!=null) {
                conn.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try {
            if (rs!=null) {
                rs.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
