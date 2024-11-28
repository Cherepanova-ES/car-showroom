package org.example.carshowroom.dao;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import static org.example.carshowroom.TestUtils.createCar;
import static org.example.carshowroom.TestUtils.verifyCar;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CarDaoTest {
    @Mock
    private Connection connection;
    @Mock
    private PreparedStatement statement;
    @InjectMocks
    private CarDaoImpl dao;

    @BeforeEach
    void setUp() throws SQLException {
        when(connection.prepareStatement(anyString())).thenReturn(statement);
    }

    @Test
    void findAll() throws SQLException {
        String[] columnNames = {"id", "type", "brand", "model", "year", "engine_volume", "engine_power"};
        Object[][] data = {{1, "type", "brand", "model", (short) 2020, BigDecimal.ONE, (short) 100}};
        var rs = MockResultSet.create(columnNames, data);
        when(statement.executeQuery()).thenReturn(rs);
        var cars = dao.findAll();
        assertEquals(1, cars.size());
        verifyCar(cars.get(0));
    }

    @Test
    void findVacant() throws SQLException {
        String[] columnNames = {"id", "type", "brand", "model", "year", "engine_volume", "engine_power"};
        Object[][] data = {{1, "type", "brand", "model", (short) 2020, BigDecimal.ONE, (short) 100}};
        var rs = MockResultSet.create(columnNames, data);
        when(statement.executeQuery()).thenReturn(rs);
        var cars = dao.findVacant();
        assertEquals(1, cars.size());
        verifyCar(cars.get(0));
    }

    @Test
    void findById() throws SQLException {
        String[] columnNames = {"id", "type", "brand", "model", "year", "engine_volume", "engine_power"};
        Object[][] data = {{1, "type", "brand", "model", (short) 2020, BigDecimal.ONE, (short) 100}};
        var rs = MockResultSet.create(columnNames, data);
        when(statement.executeQuery()).thenReturn(rs);
        verifyCar(dao.findById(1));
    }

    @Test
    void create() throws SQLException {
        when(statement.executeUpdate()).thenReturn(1);
        dao.create(createCar());
        verify(statement).executeUpdate();
    }

    @Test
    void create_sqlFailed() throws SQLException {
        when(statement.executeUpdate()).thenReturn(0);
        var car = createCar();
        var ex = assertThrows(RuntimeException.class, () -> dao.create(car));
        assertEquals("Ошибка при создании записи", ex.getMessage());
    }

    @Test
    void save() throws SQLException {
        when(statement.executeUpdate()).thenReturn(1);
        dao.save(createCar());
        verify(statement).executeUpdate();
    }

    @Test
    void save_sqlFailed() throws SQLException {
        when(statement.executeUpdate()).thenReturn(0);
        var car = createCar();
        var ex = assertThrows(RuntimeException.class, () -> dao.save(car));
        assertEquals("Ошибка при сохранении записи", ex.getMessage());
    }

    @Test
    void delete() throws SQLException {
        when(statement.executeUpdate()).thenReturn(1);
        dao.delete(1);
        verify(statement).executeUpdate();
    }

    @Test
    void delete_recordNotExists() throws SQLException {
        when(statement.executeUpdate()).thenReturn(0);
        var ex = assertThrows(RuntimeException.class, () -> dao.delete(1));
        assertEquals("Ошибка при удалении записи", ex.getMessage());
    }
}