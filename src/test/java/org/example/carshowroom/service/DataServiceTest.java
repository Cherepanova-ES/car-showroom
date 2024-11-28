package org.example.carshowroom.service;

import org.example.carshowroom.TestUtils;
import org.example.carshowroom.dao.CarDao;
import org.example.carshowroom.dao.CustomerDao;
import org.example.carshowroom.model.Car;
import org.example.carshowroom.model.Customer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.SQLException;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DataServiceTest {

    private static final int ID = 1;
    private static final Car CAR = TestUtils.createCar();
    private static final Customer CUSTOMER = TestUtils.createCustomer();

    @Mock
    private CarDao carDao;
    @Mock
    private CustomerDao customerDao;
    @InjectMocks
    private DataServiceImpl service;

    @Test
    void findAllCars() throws SQLException {
        List<Car> result = Collections.emptyList();
        when(carDao.findAll()).thenReturn(result);
        assertEquals(result, service.findAllCars());
        verify(carDao).findAll();
    }

    @Test
    void findVacantCars() throws SQLException {
        List<Car> result = Collections.emptyList();
        when(carDao.findVacant()).thenReturn(result);
        assertEquals(result, service.findVacantCars());
        verify(carDao).findVacant();
    }

    @Test
    void findCarById() throws SQLException {
        when(carDao.findById(anyInt())).thenReturn(CAR);
        var id = ID;
        assertEquals(CAR, service.findCarById(id));
        verify(carDao).findById(id);
    }

    @Test
    void createCar() throws SQLException {
        service.createCar(CAR);
        verify(carDao).create(CAR);
    }

    @Test
    void updateCar() throws SQLException {
        service.updateCar(CAR);
        verify(carDao).save(CAR);
    }

    @Test
    void deleteCar() throws SQLException {
        service.deleteCar(ID);
        verify(carDao).delete(ID);
    }

    @Test
    void findAllCustomers() throws SQLException {
        List<Customer> result = Collections.emptyList();
        when(customerDao.findAll()).thenReturn(result);
        assertEquals(result, service.findAllCustomers());
        verify(customerDao).findAll();
    }

    @Test
    void findCustomerById() throws SQLException {
        when(customerDao.findById(anyInt())).thenReturn(CUSTOMER);
        assertEquals(CUSTOMER, service.findCustomerById(ID));
        verify(customerDao).findById(ID);
    }

    @Test
    void findCustomerByCarId() throws SQLException {
        when(customerDao.findByCarId(anyInt())).thenReturn(CUSTOMER);
        assertEquals(CUSTOMER, service.findCustomerByCarId(ID));
        verify(customerDao).findByCarId(ID);
    }

    @Test
    void createCustomer() throws SQLException {
        service.createCustomer(CUSTOMER);
        verify(customerDao).create(CUSTOMER);
    }

    @Test
    void updateCustomer() throws SQLException {
        service.updateCustomer(CUSTOMER);
        verify(customerDao).save(CUSTOMER);
    }

    @Test
    void deleteCustomer() throws SQLException {
        service.deleteCustomer(ID);
        verify(customerDao).delete(ID);
    }
}