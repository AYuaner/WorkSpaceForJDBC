package com.transaction;

import com.bean.User;
import com.util.JDBCUtils;
import org.junit.Test;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.*;

/**
 * @author A_Yuan
 * @create 2021-06-25 15:57
 */
public class TransactionTest {

    /*
    数据自动提交的情况
        DDL操作一旦执行，都会自动提交
        DML默认情况下，一旦执行就会自动提交
            以上两情况可以通过set autocommit = false来解决
        默认在关闭连接时，会自动的提交数据
     */

    @Test
    public void testUpdate() {
        String sql1 = "update user_table set balance = balance - 100 where name = ?";
        update(sql1, "AA");

        //模拟网络异常
        System.out.println(10 / 0);

        String sql2 = "update user_table set balance = balance + 100 where name = ?";
        update(sql2, "BB");

        System.out.println("转账成功");
    }

    //通用的增删改操作
    public int update(String sql, Object... args) {
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = JDBCUtils.getConnection();
            ps = conn.prepareStatement(sql);
            for (int i = 0; i < args.length; i++) {
                ps.setObject(i + 1, args[i]);
            }
            return ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.closeResource(conn, ps);
        }
        return 0;
    }

    @Test
    public void testUpdateWithTx() {
        Connection conn = null;
        try {
            conn = JDBCUtils.getConnection();

            //1.取消数据的自动提交
            conn.setAutoCommit(false);

            String sql1 = "update user_table set balance = balance - 100 where name = ?";
            update(conn, sql1, "AA");

            //模拟网络异常
            //System.out.println(10 / 0);

            String sql2 = "update user_table set balance = balance + 100 where name = ?";
            update(conn, sql2, "BB");

            System.out.println("转账成功");

            //2.提交数据
            conn.commit();
        } catch (Exception e) {
            e.printStackTrace();

            //3.回滚数据
            try {
                conn.rollback();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        } finally {
            JDBCUtils.closeResource(conn, null);
        }
    }

    //优化过的增删改操作-考虑上事务
    public int update(Connection conn, String sql, Object... args) {
        PreparedStatement ps = null;
        try {
            ps = conn.prepareStatement(sql);
            for (int i = 0; i < args.length; i++) {
                ps.setObject(i + 1, args[i]);
            }
            return ps.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } finally {
            //修改为自动提交数据-主要针对于使用数据库连接池的
            try {
                conn.setAutoCommit(true);
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
            JDBCUtils.closeResource(null, ps);
        }
        return 0;
    }

    @Test
    public void testTransactionSelect() throws Exception {
        Connection conn = JDBCUtils.getConnection();

        System.out.println(conn.getTransactionIsolation());
        conn.setAutoCommit(false);

        String sql = "select user,password,balance from user_table where user = ?";
        User cc = getInstance(conn, User.class, sql, "CC");
        System.out.println(cc);
    }

    @Test
    public void testTransactionUpdate() throws Exception {
        Connection conn = JDBCUtils.getConnection();

        conn.setAutoCommit(false);

        //conn.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);

        String sql = "update user_table set balance = ? where user = ?";
        int updateCount = update(conn, sql, 5000, "CC");

        Thread.sleep(15000);
        System.out.println("修改结束");
    }

    //优化后的 通用的查询操作 -考虑事务
    public <T> T getInstance(Connection conn, Class<T> clazz, String sql, Object... args) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
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

                    Field field = clazz.getDeclaredField(columnLabel);
                    field.setAccessible(true);
                    field.set(t, columnValue);
                }
                return t;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.closeResource(null, ps, rs);
        }
        return null;
    }
}
