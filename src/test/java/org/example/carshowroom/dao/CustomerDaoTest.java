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

import static org.example.carshowroom.TestUtils.createCustomer;
import static org.example.carshowroom.TestUtils.verifyCustomer;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CustomerDaoTest {
    @Mock
    private Connection connection;
    @Mock
    private PreparedStatement statement;
    @InjectMocks
    private CustomerDaoImpl dao;

    @BeforeEach
    void setUp() throws SQLException {
        when(connection.prepareStatement(anyString())).thenReturn(statement);
    }

    @Test
    void findAll() throws SQLException {
        String[] columnNames = {"id", "last_name", "first_name", "patronymic", "car_id", "type", "brand", "model",
                "year", "engine_volume", "engine_power"};
        Object[][] data = {{1, "lastName", "firstName", "patronymic", 1, "type", "brand", "model", (short) 2020,
                BigDecimal.ONE, (short) 100}};
        var rs = MockResultSet.create(columnNames, data);
        when(statement.executeQuery()).thenReturn(rs);
        var customers = dao.findAll();
        assertEquals(1, customers.size());
        verifyCustomer(customers.get(0));
    }

    @Test
    void findById() throws SQLException {
        String[] columnNames = {"id", "last_name", "first_name", "patronymic", "car_id", "type", "brand", "model",
                "year", "engine_volume", "engine_power"};
        Object[][] data = {{1, "lastName", "firstName", "patronymic", 1, "type", "brand", "model", (short) 2020,
                BigDecimal.ONE, (short) 100}};
        var rs = MockResultSet.create(columnNames, data);
        when(statement.executeQuery()).thenReturn(rs);
        verifyCustomer(dao.findById(1));
    }

    @Test
    void findByCarId() throws SQLException {
        String[] columnNames = {"id", "last_name", "first_name", "patronymic", "car_id", "type", "brand", "model",
                "year", "engine_volume", "engine_power"};
        Object[][] data = {{1, "lastName", "firstName", "patronymic", 1, "type", "brand", "model", (short) 2020,
                BigDecimal.ONE, (short) 100}};
        var rs = MockResultSet.create(columnNames, data);
        when(statement.executeQuery()).thenReturn(rs);
        verifyCustomer(dao.findByCarId(1));
    }

    @Test
    void create() throws SQLException {
        when(statement.executeUpdate()).thenReturn(1);
        dao.create(createCustomer());
        verify(statement).executeUpdate();
    }

    @Test
    void create_sqlFailed() throws SQLException {
        when(statement.executeUpdate()).thenReturn(0);
        var customer = createCustomer();
        var ex = assertThrows(RuntimeException.class, () -> dao.create(customer));
        assertEquals("Ошибка при создании записи", ex.getMessage());
    }

    @Test
    void save() throws SQLException {
        when(statement.executeUpdate()).thenReturn(1);
        dao.save(createCustomer());
        verify(statement).executeUpdate();
    }

    @Test
    void save_sqlFailed() throws SQLException {
        when(statement.executeUpdate()).thenReturn(0);
        var customer = createCustomer();
        var ex = assertThrows(RuntimeException.class, () -> dao.save(customer));
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