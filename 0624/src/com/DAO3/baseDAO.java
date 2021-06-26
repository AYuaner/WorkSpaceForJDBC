package com.DAO3;

import com.util.JDBCUtils;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author A_Yuan
 * @create 2021-06-25 17:14
 */
/*
封装了针对于数据表的通用的操作
DAO:data(base) access object
 */
public abstract class baseDAO<T> {

    private QueryRunner runner = new QueryRunner();

    private Class<T> clazz = null;

    {
        Type genericSuperclass = this.getClass().getGenericSuperclass();
        ParameterizedType paramType = (ParameterizedType) genericSuperclass;
        Type[] typeArguments = paramType.getActualTypeArguments();
        clazz = (Class<T>) typeArguments[0];
    }

    //增删改
    public int update(Connection conn, String sql, Object... args) {
        int updateCount = 0;
        try {
            updateCount = runner.update(conn, sql, args);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return updateCount;
    }

    //查-单
    public T getInstance(Connection conn, String sql, Object... args) {
        T t = null;
        try {
            t = runner.query(conn, sql, new BeanHandler<T>(clazz), args);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return t;
    }

    //查-多
    public List<T> getForList(Connection conn, String sql, Object... args) {
        List<T> list = null;
        try {
            list = runner.query(conn, sql, new BeanListHandler<T>(clazz), args);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    //用于查询特殊值的通用方法
    public <E> E getValue(Connection conn, String sql, Object... args) {
        E e = null;
        try {
            e = runner.query(conn, sql, new ScalarHandler<E>(), args);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return e;
    }
}
