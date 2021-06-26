package com.praparedstatement;

import com.util.JDBCUtils;
import org.junit.Test;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

/**
 * @author A_Yuan
 * @create 2021-06-23 14:31
 */
public class PreparedStatementTest {

    //Statement有拼串的不便和SQL注入的风险，所以使用PreparedStatement来代替Statement

    // 向customers表中添加一条记录
    @Test
    public void testInsert() throws Exception {

        InputStream is = PreparedStatementTest.class.getClassLoader().getResourceAsStream("jdbc.properties");
        Properties pro = new Properties();
        pro.load(is);

        String user = pro.getProperty("user");
        String password = pro.getProperty("password");
        String url = pro.getProperty("url");
        String driverClass = pro.getProperty("driverClass");

        Class.forName(driverClass);

        Connection connection = DriverManager.getConnection(url, user, password);

        //预编译sql语句，返回PreparedStatement实例
        String sql = "insert into customers(name,email,birth)value(?,?,?)";
        PreparedStatement ps = connection.prepareStatement(sql);


        //填充占位符
        ps.setString(1, "哪吒");
        ps.setString(2, "nezha@gmail.com");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        java.util.Date date = sdf.parse("1000-01-01");
        ps.setDate(3, new java.sql.Date(date.getTime()));

        //执行
        ps.execute();

        //关闭资源
        is.close();
        ps.close();

    }

    //修改Customers表中的一条记录
    @Test
    public void testUpdate() throws Exception {

        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = JDBCUtils.getConnection();

            String sql = "update customers set name = ? where id = ?";
            ps = conn.prepareStatement(sql);

            ps.setObject(1, "莫扎特");
            ps.setObject(2, "18");

            ps.execute();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.closeResource(conn, ps);
        }

    }

    //通用的增删改操作
    public void update(String sql, Object... args) {

        Connection conn = null;
        PreparedStatement ps = null;

        try {
            //获取连接
            conn = JDBCUtils.getConnection();

            //预编译sql语句，返回PreparedStatement实例
            ps = conn.prepareStatement(sql);

            //填充占位符
            for (int i = 0; i < args.length; i++) {
                ps.setObject(i + 1, args[i]);
            }

            //执行
            ps.execute();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            //资源关闭
            JDBCUtils.closeResource(conn, ps);
        }

    }

    //对于上述通用操作函数的测试
    @Test
    public void testCommonUpdate1() {

        String sql = "delete from customers where id = ?";
        update(sql, 3);

    }

    @Test
    public void testCommonUpdate2() {

        String sql = "update from `order` set order_name = ? where order_id = ?";
        update(sql,"DD","2");
    }
}
