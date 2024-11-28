package org.example.carshowroom.dao;

import org.example.carshowroom.model.Customer;

import java.sql.SQLException;
import java.util.List;

public interface CustomerDao {
    List<Customer> findAll() throws SQLException;
    Customer findById(int id) throws SQLException;
    Customer findByCarId(int id) throws SQLException;
    void create(Customer customer) throws SQLException;
    void save(Customer customer) throws SQLException;
    void delete(int id) throws SQLException;
}
