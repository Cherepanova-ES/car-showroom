package org.example.carshowroom.dao;

import org.example.carshowroom.model.Car;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CarDaoImpl implements CarDao {

    private final Connection connection;

    public CarDaoImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public List<Car> findAll() throws SQLException {
        var sql = "SELECT * FROM showroom.cars";
        var statement = connection.prepareStatement(sql);
        var resultSet = statement.executeQuery();
        return mapToList(resultSet);
    }

    @Override
    public List<Car> findVacant() throws SQLException {
        var sql = "SELECT * FROM showroom.cars c " +
                "LEFT JOIN showroom.customers cu ON c.id = cu.car_id " +
                "WHERE cu.car_id IS NULL;";
        var statement = connection.prepareStatement(sql);
        var resultSet = statement.executeQuery();
        return mapToList(resultSet);
    }

    @Override
    public Car findById(int id) throws SQLException {
        var sql = "SELECT * FROM showroom.cars WHERE id = ? LIMIT 1";
        var statement = connection.prepareStatement(sql);
        statement.setInt(1, id);
        var resultSet = statement.executeQuery();
        return mapToCar(resultSet);
    }

    @Override
    public void create(Car car) throws SQLException {
        var sql = "INSERT INTO showroom.cars (type, brand, model, year, engine_volume, engine_power) VALUES (?, ?, ?, ?, ?, ?)";
        var statement = connection.prepareStatement(sql);
        statement.setString(1, car.getType());
        statement.setString(2, car.getBrand());
        statement.setString(3, car.getModel());
        statement.setShort(4, car.getYear());
        statement.setBigDecimal(5, car.getEngineVolume());
        statement.setShort(6, car.getEnginePower());

        if (statement.executeUpdate() <= 0) {
            throw new RuntimeException("Ошибка при создании записи");
        }
    }

    @Override
    public void save(Car car) throws SQLException {
        var sql = "UPDATE showroom.cars SET type = ?, brand = ?, model = ?, year = ?, engine_volume = ?, engine_power = ? WHERE id = ?";
        var statement = connection.prepareStatement(sql);
        statement.setString(1, car.getType());
        statement.setString(2, car.getBrand());
        statement.setString(3, car.getModel());
        statement.setShort(4, car.getYear());
        statement.setBigDecimal(5, car.getEngineVolume());
        statement.setShort(6, car.getEnginePower());
        statement.setInt(7, car.getId());

        if (statement.executeUpdate() <= 0) {
            throw new RuntimeException("Ошибка при сохранении записи");
        }
    }

    @Override
    public void delete(int id) throws SQLException {
        var sql = "DELETE FROM showroom.cars WHERE id = ? LIMIT 1";
        var statement = connection.prepareStatement(sql);
        statement.setInt(1, id);

        if (statement.executeUpdate() != 1) {
            throw new RuntimeException("Ошибка при удалении записи");
        }
    }

    private static Car mapToCar(ResultSet resultSet) throws SQLException {
        if (resultSet != null && resultSet.next()) {
            return createCar(resultSet);
        }

        return null;
    }

    private static List<Car> mapToList(ResultSet resultSet) throws SQLException {
        var cars = new ArrayList<Car>();

        while (resultSet != null && resultSet.next()) {
            cars.add(createCar(resultSet));
        }

        return cars;
    }

    private static Car createCar(ResultSet resultSet) throws SQLException {
        return new Car(
                resultSet.getInt("id"),
                resultSet.getString("type"),
                resultSet.getString("brand"),
                resultSet.getString("model"),
                resultSet.getShort("year"),
                resultSet.getBigDecimal("engine_volume"),
                resultSet.getShort("engine_power")
        );
    }
}
