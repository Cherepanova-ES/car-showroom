package org.example.carshowroom;

import org.example.carshowroom.model.Car;
import org.example.carshowroom.model.Customer;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestUtils {

    public static Car createCar() {
        return new Car(
                1,
                "type",
                "brand",
                "model",
                (short) 2020,
                BigDecimal.ONE,
                (short) 100
        );
    }

    public static Customer createCustomer() {
        return new Customer(1, "lastName", "firstName", "patronymic", createCar());
    }

    public static void verifyCar(Car car) {
        assertEquals(1, car.getId());
        assertEquals("type", car.getType());
        assertEquals("brand", car.getBrand());
        assertEquals("model", car.getModel());
        assertEquals((short) 2020, car.getYear());
        assertEquals(BigDecimal.ONE, car.getEngineVolume());
        assertEquals((short) 100, car.getEnginePower());
    }

    public static void verifyCustomer(Customer customer) {
        assertEquals(1, customer.getId());
        assertEquals("lastName", customer.getLastName());
        assertEquals("firstName", customer.getFirstName());
        assertEquals("patronymic", customer.getPatronymic());
        verifyCar(customer.getCar());
    }
}
