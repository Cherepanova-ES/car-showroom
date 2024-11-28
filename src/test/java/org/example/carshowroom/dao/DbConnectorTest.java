package org.example.carshowroom.dao;

import lombok.Cleanup;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.platform.commons.util.ReflectionUtils;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mockStatic;

@ExtendWith(MockitoExtension.class)
class DbConnectorTest {

    @Mock
    private Connection connection;
    @Captor
    private ArgumentCaptor<String> captor;

    @Test
    void getConnection() throws Exception {
        @Cleanup
        var mockedStatic = mockStatic(DriverManager.class);
        mockedStatic.when(() -> DriverManager.getConnection(captor.capture(), captor.capture(), captor.capture()))
                .thenReturn(connection);

        var connector = new DbConnector();
        var actualConnection = (Connection) ReflectionUtils.tryToReadFieldValue(DbConnector.class, "connection", connector).get();
        assertEquals(connection, actualConnection);
        var props = List.of(
                "jdbc:mysql://localhost:3306/showroom",
                "root",
                "password"
        );
        assertEquals(props, captor.getAllValues());
    }
}