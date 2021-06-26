package com.insert;

import com.util.JDBCUtils;
import org.junit.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;

/**
 * @author A_Yuan
 * @create 2021-06-24 23:49
 */
public class InsertTest {

    //使用Statement
    @Test
    public void testStatement() throws Exception {
        Connection conn = JDBCUtils.getConnection();
        Statement st = conn.createStatement();
        for (int i = 0; i < 2000; i++) {
            String sql = "insert into goods(name)values('name_" + i + "')";
            st.execute(sql);
        }
    }

    //使用PreparedStatement
    @Test
    public void testPreparedStatement() throws Exception {
        Connection conn = JDBCUtils.getConnection();
        String sql = "insert into goods(name)values(?)";
        PreparedStatement ps = conn.prepareStatement(sql);
        for (int i = 0; i < 2000; i++) {
            ps.setObject(1, "name_" + i);
            ps.execute();
        }
        JDBCUtils.closeResource(conn, ps);
    }

    //Statement 编译N条sql语句 执行N次插入操作  PreparedStatement 编译一条SQL语句 执行N次插入操作

    //使用Batch来优化
    @Test
    public void testBatch() throws Exception {
        Connection conn = JDBCUtils.getConnection();
        String sql = "insert into goods(name)values(?)";
        PreparedStatement ps = conn.prepareStatement(sql);
        for (int i = 0; i < 2000; i++) {
            ps.setObject(1, "name_" + i);

            //攒SQL
            ps.addBatch();
            if (i % 500 == 0) {
                //执行Batch
                ps.executeBatch();
                //清空Batch
                ps.clearBatch();
            }
            JDBCUtils.closeResource(conn, ps);
        }
    }

    //设置不允许自动提交数据
    @Test
    public void testAutoCommit() throws Exception {
        Connection conn = JDBCUtils.getConnection();

        conn.setAutoCommit(false);

        String sql = "insert into goods(name)values(?)";
        PreparedStatement ps = conn.prepareStatement(sql);
        for (int i = 0; i < 2000; i++) {
            ps.setObject(1, "name_" + i);
            ps.addBatch();
            if (i % 500 == 0) {
                ps.executeBatch();
                ps.clearBatch();
            }
            JDBCUtils.closeResource(conn, ps);
        }
    }
}
