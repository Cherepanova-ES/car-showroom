package org.example.carshowroom.dao;

import org.example.carshowroom.model.Car;
import org.example.carshowroom.model.Customer;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CustomerDaoImpl implements CustomerDao {

    private final Connection connection;

    public CustomerDaoImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public List<Customer> findAll() throws SQLException {
        var sql = "SELECT cu.*, c.* FROM showroom.customers cu JOIN cars c ON cu.car_id = c.id";
        var statement = connection.prepareStatement(sql);
        var resultSet = statement.executeQuery();
        return mapToList(resultSet);
    }

    @Override
    public Customer findById(int id) throws SQLException {
        var sql = "SELECT cu.*, c.* FROM showroom.customers cu JOIN cars c ON cu.car_id = c.id WHERE cu.id = ? LIMIT 1";
        var statement = connection.prepareStatement(sql);
        statement.setInt(1, id);
        var resultSet = statement.executeQuery();
        return mapToCustomer(resultSet);
    }

    @Override
    public Customer findByCarId(int id) throws SQLException {
        var sql = "SELECT cu.*, c.* FROM showroom.customers cu JOIN cars c ON cu.car_id = c.id WHERE cu.car_id = ? LIMIT 1";
        var statement = connection.prepareStatement(sql);
        statement.setInt(1, id);
        var resultSet = statement.executeQuery();
        return mapToCustomer(resultSet);
    }

    @Override
    public void create(Customer customer) throws SQLException {
        var sql = "INSERT INTO showroom.customers (last_name, first_name, patronymic, car_id) VALUES (?, ?, ?, ?)";
        var statement = connection.prepareStatement(sql);
        statement.setString(1, customer.getLastName());
        statement.setString(2, customer.getFirstName());
        statement.setString(3, customer.getPatronymic());
        statement.setInt(4, customer.getCar().getId());

        if (statement.executeUpdate() <= 0) {
            throw new RuntimeException("Ошибка при создании записи");
        }
    }

    @Override
    public void save(Customer customer) throws SQLException {
        var sql = "UPDATE showroom.customers SET last_name = ?, first_name = ?, patronymic = ?, car_id = ?  WHERE id = ?";
        var statement = connection.prepareStatement(sql);
        statement.setString(1, customer.getLastName());
        statement.setString(2, customer.getFirstName());
        statement.setString(3, customer.getPatronymic());
        statement.setInt(4, customer.getCar().getId());
        statement.setInt(5, customer.getId());

        if (statement.executeUpdate() <= 0) {
            throw new RuntimeException("Ошибка при сохранении записи");
        }
    }

    @Override
    public void delete(int id) throws SQLException {
        var sql = "DELETE FROM showroom.customers WHERE id = ? LIMIT 1";
        var statement = connection.prepareStatement(sql);
        statement.setInt(1, id);

        if (statement.executeUpdate() != 1) {
            throw new RuntimeException("Ошибка при удалении записи");
        }
    }

    private static Customer mapToCustomer(ResultSet resultSet) throws SQLException {
        if (resultSet != null && resultSet.next()) {
            return createCustomer(resultSet);
        }

        return null;
    }

    private static List<Customer> mapToList(ResultSet resultSet) throws SQLException {
        var customers = new ArrayList<Customer>();

        while (resultSet != null && resultSet.next()) {
            customers.add(createCustomer(resultSet));
        }

        return customers;
    }

    private static Customer createCustomer(ResultSet resultSet) throws SQLException {
        var car = new Car(
                resultSet.getInt("car_id"),
                resultSet.getString("type"),
                resultSet.getString("brand"),
                resultSet.getString("model"),
                resultSet.getShort("year"),
                resultSet.getBigDecimal("engine_volume"),
                resultSet.getShort("engine_power")
        );

        return new Customer(
                resultSet.getInt("id"),
                resultSet.getString("last_name"),
                resultSet.getString("first_name"),
                resultSet.getString("patronymic"),
                car
        );
    }
}
