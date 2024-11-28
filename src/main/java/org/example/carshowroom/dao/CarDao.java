package org.example.carshowroom.dao;

import org.example.carshowroom.model.Car;

import java.sql.SQLException;
import java.util.List;

public interface CarDao {
    List<Car> findAll() throws SQLException;
    List<Car> findVacant() throws SQLException;
    Car findById(int id) throws SQLException;
    void create(Car car) throws SQLException;
    void save(Car car) throws SQLException;
    void delete(int id) throws SQLException;
}
