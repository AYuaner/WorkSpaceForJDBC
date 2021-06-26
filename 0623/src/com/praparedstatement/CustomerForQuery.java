package com.praparedstatement;

import com.bean.Customer;
import com.util.JDBCUtils;
import org.junit.Test;

import java.lang.reflect.Field;
import java.sql.*;

/**
 * 针对于Customers表的通用查询操作
 *
 * @author A_Yuan
 * @create 2021-06-23 16:03
 */
public class CustomerForQuery {

    @Test
    public void testQuery1() {

        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet resultSet = null;
        try {
            conn = JDBCUtils.getConnection();

            String sql = "select id,name,email,birth from customers where id = ?";

            ps = conn.prepareStatement(sql);

            ps.setObject(1, 1);

            //执行，并返回结果集
            resultSet = ps.executeQuery();

            //处理结果集
            if (resultSet.next()) {

                int id = resultSet.getInt(1);
                String name = resultSet.getString(2);
                String email = resultSet.getString(3);
                Date date = resultSet.getDate(4);

                //将数据封装为一个对象
                Customer customer = new Customer(id, name, email, date);
                System.out.println(customer);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.closeResource(conn, ps, resultSet);
        }

    }

    /**
     * 通用查询操作
     *
     * @param sql
     * @param args
     * @return
     * @throws Exception
     */
    public Customer queryForCustomers(String sql, Object... args) {

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

            //获取结果集的元数据
            ResultSetMetaData rsmd = rs.getMetaData();
            //通过ResultSetMetaData来获取结果集中的列数
            int columnCount = rsmd.getColumnCount();
            if (rs.next()) {

                Customer cust = new Customer();
                //处理结果集一行数据中的每一列
                for (int i = 0; i < columnCount; i++) {
                    //获取列值
                    Object columnValue = rs.getObject(i + 1);

                    //获取每一列的列名
                    String columnName = rsmd.getColumnName(i + 1);

                    //通过反射,给cust对象指定的columnName属性,赋值为columnName
                    Field field = Customer.class.getDeclaredField(columnName);
                    field.setAccessible(true);
                    field.set(cust, columnValue);
                }
                return cust;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.closeResource(conn, ps, rs);
        }
        return null;
    }

    //用于测试上述通用查询语句
    @Test
    public void testQueryForCustomers1() {
        String sql = "select id,name,birth,email from customers where id = ?";
        Customer customer = queryForCustomers(sql, 13);
        System.out.println(customer);
    }

    @Test
    public void testQueryForCustomers2() {
        String sql = "select name,email from customers where name = ?";
        Customer customer = queryForCustomers(sql, "周杰伦");
        System.out.println(customer);
    }

}
