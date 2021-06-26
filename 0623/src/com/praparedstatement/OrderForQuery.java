package com.praparedstatement;

import com.bean.Order;
import com.util.JDBCUtils;
import org.junit.Test;

import java.lang.reflect.Field;
import java.sql.*;

/**
 * 对于Order表的通用的查询操作
 *
 * @author A_Yuan
 * @create 2021-06-23 19:24
 */
public class OrderForQuery {
    /*
    针对于表的字段名和类的属性名不相同的情况
        1.必须声明sql时，使用类的属性名来命名字段的别名
        2.使用ResultSetMetaData时，使用getColumnLabel()来代替getColumnName()
            getColumnLabel在有别名的时候获取别名，没有别名的时候获取列名
     */

    @Test
    public void testQuery1() {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = JDBCUtils.getConnection();
            String sql = "select order_id,order_name,order_date from `order` where order_id = ?";
            ps = conn.prepareStatement(sql);
            ps.setObject(1, 1);

            rs = ps.executeQuery();
            if (rs.next()) {
                int id = (int) rs.getObject(1);
                String name = (String) rs.getObject(2);
                Date date = (Date) rs.getObject(3);

                Order order = new Order(id, name, date);
                System.out.println(order);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.closeResource(conn, ps, rs);
        }

    }

    /**
     * 通用的针对于Order表的查询操作
     *
     * @param sql
     * @param args
     * @return
     */
    public Order orderForQuery(String sql, Object... args) {

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
                Order order = new Order();
                for (int i = 0; i < columnCount; i++) {
                    Object columnValue = rs.getObject(i + 1);
                    //String columnName = rsmd.getColumnName(i + 1);
                    //getColumnLabel获取列的别名
                    String columnLabel = rsmd.getColumnLabel(i + 1);
                    //Field field = Order.class.getDeclaredField(columnName);
                    Field field = Order.class.getDeclaredField(columnLabel);
                    field.setAccessible(true);
                    field.set(order, columnValue);
                }
                return order;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.closeResource(conn, ps, rs);
        }
        return null;
    }

    //用于测试上述Order表的通用查询语句
    @Test
    public void testOrderForQuery() {
        //字段的别名要对应类的属性名
        String sql = "select order_id orderId,order_name orderName,order_date orderDate from `order` where order_id = ?";
        Order order = orderForQuery(sql, 1);
        System.out.println(order);
    }
}
