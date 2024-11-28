package org.example.carshowroom.service;

import org.example.carshowroom.model.Car;
import org.example.carshowroom.model.Customer;

import java.sql.SQLException;
import java.util.List;

public interface DataService {
    List<Car> findAllCars() throws SQLException;
    List<Car> findVacantCars() throws SQLException;
    Car findCarById(int id) throws SQLException;
    void createCar(Car car) throws SQLException;
    void updateCar(Car car) throws SQLException;
    void deleteCar(int id) throws SQLException;

    List<Customer> findAllCustomers() throws SQLException;
    Customer findCustomerById(int id) throws SQLException;;
    Customer findCustomerByCarId(int id) throws SQLException;;
    void createCustomer(Customer customer) throws SQLException;
    void updateCustomer(Customer customer) throws SQLException;
    void deleteCustomer(int id) throws SQLException;
}
