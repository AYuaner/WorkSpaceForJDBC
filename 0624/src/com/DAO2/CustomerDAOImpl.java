package com.DAO2;

import com.bean.Customer;

import java.sql.Connection;
import java.sql.Date;
import java.util.List;

/**
 * @author A_Yuan
 * @create 2021-06-25 19:44
 */
public class CustomerDAOImpl extends baseDAO<Customer> implements CustomerDAO {
    @Override
    public void insert(Connection conn, Customer cust) {
        String sql = "insert into customers(name,email,birth)values(?,?,?)";
        update(conn, sql, cust.getName(), cust.getEmail(), cust.getBirth());
    }

    @Override
    public void deleteById(Connection conn, int id) {
        String sql = "delete from customers where id = ?";
        update(conn, sql, id);
    }

    @Override
    public void update(Connection conn, Customer cust) {
        String sql = "update customers set name = ?,email = ?,birth = ? where id = ?";
        update(conn, sql, cust.getName(), cust.getEmail(), cust.getBirth(), cust.getId());
    }

    @Override
    public Customer getCustomerById(Connection conn, int id) {
        String sql = "select id,name,email,birth from customers where id = ?";
        return getInstance(conn, sql, id);
    }

    @Override
    public List<Customer> getAll(Connection conn) {
        String sql = "select id,name,email,birth from customers";
        return getForList(conn, sql);
    }

    @Override
    public Long getCount(Connection conn) {
        String sql = "select count(*) from customers";
        return getValue(conn, sql);
    }

    @Override
    public Date getMaxBirth(Connection conn) {
        String sql = "select max(birth) from customers";
        return getValue(conn, sql);
    }
}
