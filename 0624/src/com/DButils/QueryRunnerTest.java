package com.DButils;

import com.bean.Customer;
import com.util.JDBCUtils;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.apache.commons.dbutils.handlers.*;
import org.junit.Test;

import java.sql.Connection;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * @author A_Yuan
 * @create 2021-06-26 9:48
 */
//
public class QueryRunnerTest {

    //DBUtils的插入测试
    @Test
    public void testInsert() {
        Connection conn = null;
        int insertCount = 0;
        try {
            QueryRunner runner = new QueryRunner();
            conn = JDBCUtils.getConnection();
            String sql = "insert into customers(name,email,birth)values(?,?,?)";
            insertCount = runner.update(conn, sql, "蔡徐坤", "caixukun@126.com", new Date(1111325L));
            System.out.println("添加了" + insertCount + "条记录");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.closeResource(conn, null);
        }
    }

    //DBUtils的查询测试
    //BeanHandler：是ResultSetHandler接口的实现类，用于封装表中的一条记录
    @Test
    public void testQuery1() throws Exception {
        QueryRunner runner = new QueryRunner();
        Connection conn = JDBCUtils.getConnection();
        String sql = "select id,name,email,birth from customers where id = ?";
        BeanHandler<Customer> handler = new BeanHandler<>(Customer.class);
        Customer customer = runner.query(conn, sql, handler, 16);
        System.out.println(customer);
        JDBCUtils.closeResource(conn);
    }

    //BeanListHandler：是ResultSetHandler接口的实现类，用于封装表中的多条记录构成的集合
    @Test
    public void testQuery2() throws Exception {
        QueryRunner runner = new QueryRunner();
        Connection conn = JDBCUtils.getConnection();
        String sql = "select id,name,email,birth from customers where id < ?";
        BeanListHandler<Customer> handler = new BeanListHandler<>(Customer.class);
        List<Customer> list = runner.query(conn, sql, handler, 20);
        list.forEach(System.out::println);
        JDBCUtils.closeResource(conn);
    }

    //MapHandler：是ResultSetHandler接口的实现类，对于表中的一条记录
    //  讲字段及其对应的值作为map中的key和value
    @Test
    public void testQuery3() throws Exception {
        QueryRunner runner = new QueryRunner();
        Connection conn = JDBCUtils.getConnection();
        String sql = "select id,name,email,birth from customers where id = ?";
        MapHandler handler = new MapHandler();
        Map<String, Object> map = runner.query(conn, sql, handler, 16);
        System.out.println(map);
        JDBCUtils.closeResource(conn);
    }

    //MapListHandler：是ResultSetHandler接口的实现类，对于表中的多记录构成的集合
    @Test
    public void testQuery4() throws Exception {
        QueryRunner runner = new QueryRunner();
        Connection conn = JDBCUtils.getConnection();
        String sql = "select id,name,email,birth from customers where id > ?";
        MapListHandler handler = new MapListHandler();
        List<Map<String, Object>> list = runner.query(conn, sql, handler, 5);
        list.forEach(System.out::println);
        JDBCUtils.closeResource(conn);
    }

    //ScalarHandler：是ResultSetHandler接口的实现类，用于查询特殊值
    @Test
    public void testQuery5() throws Exception {
        QueryRunner runner = new QueryRunner();
        Connection conn = JDBCUtils.getConnection();
        String sql = "select count(*) from customers";
        ScalarHandler<Long> handler = new ScalarHandler<> ();
        Long count = runner.query(conn, sql, handler);
        System.out.println(count);
        JDBCUtils.closeResource(conn);
    }

    @Test
    public void testQuery6() throws Exception {
        QueryRunner runner = new QueryRunner();
        Connection conn = JDBCUtils.getConnection();
        String sql = "select max(birth) from customers";
        ScalarHandler<Date> handler = new ScalarHandler<>();
        Date date = runner.query(conn, sql, handler);
        System.out.println(date);
        JDBCUtils.closeResource(conn);
    }

    //自定义的ResultSetHandler
    @Test
    public void testQuery7() throws Exception {
        QueryRunner runner = new QueryRunner();
        Connection conn = JDBCUtils.getConnection();
        String sql = "select id,name,email,birth from customers where id = ?";

        ResultSetHandler<Customer> handler = new ResultSetHandler<>() {
            @Override
            public Customer handle(ResultSet resultSet) throws SQLException {
                Customer customer = new Customer(1, "testName3", "test@126.com", new Date(12314214L));
                return customer;
            }
        };

        Customer customer = runner.query(conn, sql, handler, 1);
        System.out.println(customer);
        JDBCUtils.closeResource(conn);
    }
}
