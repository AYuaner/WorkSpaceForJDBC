package com.exer;

import com.util.JDBCUtils;
import org.junit.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;

/**
 * @author A_Yuan
 * @create 2021-06-24 16:14
 */
public class ExerTest1 {

    @Test
    public void exer1() {
        String name = "testName";
        String email = "testEMail";
        String birth = "1970-01-01";

        String sql = "insert into customers(name,email,birth)value(?,?,?)";
        int insertCount = update(sql, name, email, birth);
        if (insertCount > 0) {
            System.out.println("添加成功");
        } else {
            System.out.println("添加失败");
        }

    }

    public int update(String sql, Object... args) {

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
            return ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            //资源关闭
            JDBCUtils.closeResource(conn, ps);
        }
        return 0;
    }
}
