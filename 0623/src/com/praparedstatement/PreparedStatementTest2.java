package com.praparedstatement;

import com.util.JDBCUtils;
import org.junit.Test;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * @author A_Yuan
 * @create 2021-06-23 21:28
 */
public class PreparedStatementTest2 {

    //PreparedStatement来解决SQL注入

    /*
    PreparedStatement的优点
        1.可以操作Blob的数据
        2.可以实现更高效的批量操作
     */

    @Test
    public void testLogin() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("请输入用户名：");
        String user = "1' or";
        System.out.println("请输入密码：");
        String password = "=1 or '1' = '1";

        String sql = "select user,password from user_table where user = ? and password = ?";
        User returnUser = getInstance(User.class, sql, user, password);
        if (returnUser != null) {
            System.out.println("登录成功");
        } else {
            System.out.println("用户名不存在或密码错误");
        }
    }

    /**
     * 针对于不同的表的通用的查询操作
     *
     * @param clazz 对应的类
     * @param sql   查询的SQL语句
     * @param args  填充占位符的不定参数列表
     * @param <T>   对应类泛型
     * @return 返回表中的一条记录
     */
    public <T> T getInstance(Class<T> clazz, String sql, Object... args) {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = JDBCUtils.getConnection();
            ps = conn.prepareStatement(sql);
            for (int i = 0; i < args.length; i++) {
                ps.setObject(i + 1, args[i]);
            }
            rs = ps.executeQuery();
            ResultSetMetaData rsmd = rs.getMetaData();
            int columnCount = rsmd.getColumnCount();
            if (rs.next()) {
                T t = clazz.getDeclaredConstructor().newInstance();
                for (int i = 0; i < columnCount; i++) {
                    Object columnValue = rs.getObject(i + 1);
                    String columnLabel = rsmd.getColumnLabel(i + 1);
                    Field field = t.getClass().getDeclaredField(columnLabel);
                    field.setAccessible(true);
                    field.set(t, columnValue);
                }
                return t;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.closeResource(conn, ps, rs);
        }
        return null;
    }
}
