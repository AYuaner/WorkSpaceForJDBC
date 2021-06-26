package com.exer;

import com.bean.Student;
import com.util.JDBCUtils;
import org.junit.Test;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;

/**
 * @author A_Yuan
 * @create 2021-06-24 20:27
 */
public class ExerTest2 {

    //问题1：向examstudent表中添加一条记录
    @Test
    public void testInsert() {
        int type = 6;
        String IDCard = "312432124234";
        String examCard = "2354324325235";
        String studentName = "张彦宇";
        String location = "沈阳";
        int grade = 100;

        String sql = "insert into examstudent(type,IDCard,examCard,studentName,location,grade)value(?,?,?,?,?,?)";
        int insertCount = update(sql, type, IDCard, examCard, studentName, location, grade);
        if (insertCount > 0) {
            System.out.println("添加成功");
        } else {
            System.out.println("添加失败");
        }
    }

    //问题2：根据身份证号码或者准考证号查询学生成绩信息
    @Test
    public void queryWithIDCardOrExamCard() {
        String selection = "b";
        if ("a".equalsIgnoreCase(selection)) {
            String examCard = "200523164754002";
            String sql = "select FlowID flowID,Type type,IDCard,ExamCard examCard,StudentName name,Location location,Grade grade from examstudent where ExamCard = ?";
            Student student = getInstance(Student.class, sql, examCard);
            System.out.println(student);
        } else if ("b".equalsIgnoreCase(selection)) {
            String IDCard = "454524195263214584";
            String sql = "select FlowID flowID,Type type,IDCard,ExamCard examCard,StudentName name,Location location,Grade grade from examstudent where IDCard = ?";
            Student student = getInstance(Student.class, sql, IDCard);
            System.out.println(student);
        }
    }

    //问题3：删除指定的学生信息
    @Test
    public void deleteWithIDCard() {
        String IDCard = "312432124234";
        String sql = "delete from examstudent where IDCard = ?";
        int deleteCount = update(sql, IDCard);
        if (deleteCount > 0) {
            System.out.println("删除成功");
        } else {
            System.out.println("删除失败，查无此人");
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
