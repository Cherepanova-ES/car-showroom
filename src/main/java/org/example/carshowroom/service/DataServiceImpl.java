package org.example.carshowroom.service;

import org.example.carshowroom.dao.CarDao;
import org.example.carshowroom.dao.CarDaoImpl;
import org.example.carshowroom.dao.CustomerDao;
import org.example.carshowroom.dao.CustomerDaoImpl;
import org.example.carshowroom.dao.DbConnector;
import org.example.carshowroom.model.Car;
import org.example.carshowroom.model.Customer;

import java.sql.SQLException;
import java.util.List;

public class DataServiceImpl implements DataService {
    private static DataService service;

    private final CarDao carDao;
    private final CustomerDao customerDao;

    public DataServiceImpl(CarDao carDao, CustomerDao customerDao) {
        this.carDao = carDao;
        this.customerDao = customerDao;
    }

    public static DataService getInstance() {
        if (service == null) {
            var connection = new DbConnector().getConnection();
            var carDao = new CarDaoImpl(connection);
            var customerDao = new CustomerDaoImpl(connection);
            service = new DataServiceImpl(carDao, customerDao);
        }

        return service;
    }

    @Override
    public List<Car> findAllCars() throws SQLException {
        return carDao.findAll();
    }

    @Override
    public List<Car> findVacantCars() throws SQLException {
        return carDao.findVacant();
    }

    @Override
    public Car findCarById(int id) throws SQLException {
        return carDao.findById(id);
    }

    @Override
    public void createCar(Car car) throws SQLException {
        carDao.create(car);
    }

    @Override
    public void updateCar(Car car) throws SQLException {
        carDao.save(car);
    }

    @Override
    public void deleteCar(int id) throws SQLException {
        carDao.delete(id);
    }

    @Override
    public List<Customer> findAllCustomers() throws SQLException {
        return customerDao.findAll();
    }

    @Override
    public Customer findCustomerById(int id) throws SQLException {
        return customerDao.findById(id);
    }

    @Override
    public Customer findCustomerByCarId(int id) throws SQLException {
        return customerDao.findByCarId(id);
    }

    @Override
    public void createCustomer(Customer customer) throws SQLException {
        customerDao.create(customer);
    }

    @Override
    public void updateCustomer(Customer customer) throws SQLException {
        customerDao.save(customer);
    }

    @Override
    public void deleteCustomer(int id) throws SQLException {
        customerDao.delete(id);
    }
}
