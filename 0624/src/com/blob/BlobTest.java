package com.blob;

import com.bean.Customer;
import com.util.JDBCUtils;
import org.junit.Test;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.*;

/**
 * @author A_Yuan
 * @create 2021-06-24 21:40
 */
public class BlobTest {

    //向customers中插入blob类型的字段
    @Test
    public void testInsert() throws Exception {
        Connection conn = JDBCUtils.getConnection();
        String sql = "insert into customers(name,email,birth,photo)values(?,?,?,?)";

        PreparedStatement ps = conn.prepareStatement(sql);

        ps.setObject(1, "blobTest");
        ps.setObject(2, "blobTest@126.com");
        ps.setObject(3, "1970-01-01");
        FileInputStream fis = new FileInputStream("0.png");

        ps.setBlob(4, fis);

        ps.execute();

        JDBCUtils.closeResource(conn, ps);

        try {
            if (fis != null) {
                fis.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //查询数据表Customers中的Blob类型的字段
    @Test
    public void testQuery() throws Exception {
        Connection conn = JDBCUtils.getConnection();
        String sql = "select id,name,email,birth,photo from customers where id = ?";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setInt(1, 16);

        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            int id = rs.getInt("id");
            String name = rs.getString("name");
            String email = rs.getString("email");
            Date birth = rs.getDate("birth");

            Customer cust = new Customer(id, name, email, birth);
            System.out.println(cust);

            //将Blob类型的字段下载下来，以文件的形式保存在本地
            Blob photo = rs.getBlob("photo");
            InputStream is = photo.getBinaryStream();
            FileOutputStream fos = new FileOutputStream("ZhuYin.jpg");
            byte[] buffer = new byte[1024];
            int len;
            while ((len = is.read(buffer)) != -1) {
                fos.write(buffer, 0, len);
            }
            is.close();
            fos.close();
        }
        JDBCUtils.closeResource(conn, ps, rs);
    }
}
