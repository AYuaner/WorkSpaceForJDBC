package com.Druid.Druidutil;

import com.alibaba.druid.pool.DruidDataSourceFactory;
import com.mchange.v2.c3p0.DataSources;
import org.junit.Test;

import javax.sql.DataSource;
import java.io.InputStream;
import java.sql.Connection;
import java.util.Properties;

/**
 * @author A_Yuan
 * @create 2021-06-26 0:14
 */
public class JDBCUtilsDruid {

    private static DataSource source;

    static {
        try {
            Properties pros = new Properties();
            InputStream is = ClassLoader.getSystemClassLoader().getResourceAsStream("druid.properties");
            pros.load(is);
            source = DruidDataSourceFactory.createDataSource(pros);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     *使用Druid数据库连接池技术 获取连接
     * @return
     * @throws Exception
     */
    @Test
    public static Connection testGetConnection() throws Exception {
        Connection conn = source.getConnection();
        return conn;
    }

}
