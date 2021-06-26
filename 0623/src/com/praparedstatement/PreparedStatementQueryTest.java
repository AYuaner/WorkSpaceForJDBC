package com.praparedstatement;

import com.bean.Customer;
import com.bean.Order;
import com.util.JDBCUtils;
import org.junit.Test;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.List;

/**
 * 实现针对于不同表的通用查询操作
 *
 * @author A_Yuan
 * @create 2021-06-23 20:26
 */
public class PreparedStatementQueryTest {

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

    //对于上述通用查询函数的测试
    @Test
    public void testGetInstance1() {
        String sql = "select id,name,email from customers where id = ?";
        Customer customer = getInstance(Customer.class, sql, 12);
        System.out.println(customer);
    }

    @Test
    public void testGetInstance2() {
        String sql = "select order_id orderId,order_name orderName,order_date orderDate from `order` where order_id = ?";
        Order order = getInstance(Order.class, sql, 1);
        System.out.println(order);
    }

    /**
     * 针对于不同的表的通用的查询操作
     *
     * @param clazz 对应的类
     * @param sql   查询的SQL语句
     * @param args  填充占位符的不定参数列表
     * @param <T>   对应类泛型
     * @return 返回表中的多条记录的对象列表
     */
    public <T> List<T> getForList(Class<T> clazz, String sql, Object... args) {
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
            ArrayList<T> list = new ArrayList<>();
            while (rs.next()) {
                T t = clazz.getDeclaredConstructor().newInstance();
                for (int i = 0; i < columnCount; i++) {
                    Object columnValue = rs.getObject(i + 1);
                    String columnLabel = rsmd.getColumnLabel(i + 1);
                    Field field = clazz.getDeclaredField(columnLabel);
                    field.setAccessible(true);
                    field.set(t, columnValue);
                }
                list.add(t);
            }
            return list;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.closeResource(conn, ps, rs);
        }
        return null;
    }

    //对于上述通用查询函数的测试
    @Test
    public void testGetForList1() {
        String sql = "select id,name,email from customers where id < ?";
        List<Customer> list = getForList(Customer.class, sql, 12);
        list.forEach(System.out::println);
    }

    @Test
    public void testGetForList2() {
        String sql = "select order_id orderId,order_name orderName from `order` where order_id < ?";
        List<Order> list = getForList(Order.class, sql, 5);
        list.forEach(System.out::println);
    }
}
