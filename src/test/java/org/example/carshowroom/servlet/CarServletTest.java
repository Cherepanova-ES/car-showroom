package org.example.carshowroom.servlet;

import org.example.carshowroom.model.Car;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;

import static org.example.carshowroom.TestUtils.createCar;
import static org.example.carshowroom.TestUtils.createCustomer;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class CarServletTest extends AbstractServletTest {

    private final CarServlet servlet = new CarServlet();

    @Test
    void showNewForm() throws IOException {
        when(req.getPathInfo()).thenReturn("/new");

        servlet.doGet(req, resp);

        verify(engine).process("car.html", ctx, writer);
    }

    @Test
    void showEditForm() throws IOException, SQLException {
        when(req.getPathInfo()).thenReturn("/edit");
        var car = createCar();
        when(service.findCarById(anyInt())).thenReturn(car);

        servlet.doGet(req, resp);

        verify(service).findCarById(1);
        verify(ctx).setVariable("car", car);
        verify(engine).process("car.html", ctx, writer);
    }

    @Test
    void showEditForm_sqlError() throws IOException, SQLException {
        when(req.getPathInfo()).thenReturn("/edit");
        when(service.findCarById(anyInt())).thenThrow(new SQLException(MESSAGE));

        servlet.doGet(req, resp);

        verify(service).findCarById(1);
        verify(ctx).setVariable("error", MESSAGE);
        verify(engine).process("car.html", ctx, writer);
    }

    @Test
    void insertCar() throws IOException, SQLException {
        when(req.getPathInfo()).thenReturn("/insert");
        List<Car> cars = Collections.emptyList();
        when(service.findAllCars()).thenReturn(cars);

        servlet.doGet(req, resp);

        verify(service).createCar(createCar());
        verify(service).findAllCars();
        verify(req).setAttribute("info", "Запись успешно добавлена: brand model");
        verify(ctx).setVariable("cars", cars);
        verify(engine).process("cars.html", ctx, writer);
    }

    @Test
    void insertCar_sqlError() throws IOException, SQLException {
        when(req.getPathInfo()).thenReturn("/insert");
        doThrow(new SQLException(MESSAGE)).when(service).createCar(any());
        List<Car> cars = Collections.emptyList();
        when(service.findAllCars()).thenReturn(cars);

        servlet.doGet(req, resp);

        verify(service).createCar(createCar());
        verify(service).findAllCars();
        verify(req).setAttribute("error", MESSAGE);
        verify(ctx).setVariable("cars", cars);
        verify(engine).process("cars.html", ctx, writer);
    }

    @Test
    void updateCar() throws IOException, SQLException {
        when(req.getPathInfo()).thenReturn("/update");
        List<Car> cars = Collections.emptyList();
        when(service.findAllCars()).thenReturn(cars);

        servlet.doGet(req, resp);

        verify(service).updateCar(createCar());
        verify(service).findAllCars();
        verify(req).setAttribute("info", "Запись успешно обновлена: brand model");
        verify(ctx).setVariable("cars", cars);
        verify(engine).process("cars.html", ctx, writer);
    }

    @Test
    void updateCar_sqlError() throws IOException, SQLException {
        when(req.getPathInfo()).thenReturn("/update");
        doThrow(new SQLException(MESSAGE)).when(service).updateCar(any());
        List<Car> cars = Collections.emptyList();
        when(service.findAllCars()).thenReturn(cars);

        servlet.doGet(req, resp);

        verify(service).updateCar(createCar());
        verify(service).findAllCars();
        verify(req).setAttribute("error", MESSAGE);
        verify(ctx).setVariable("cars", cars);
        verify(engine).process("cars.html", ctx, writer);
    }

    @Test
    void deleteCar() throws IOException, SQLException {
        when(req.getPathInfo()).thenReturn("/delete");
        when(service.findCustomerByCarId(anyInt())).thenReturn(null);
        List<Car> cars = Collections.emptyList();
        when(service.findAllCars()).thenReturn(cars);

        servlet.doGet(req, resp);

        verify(service).deleteCar(1);
        verify(service).findAllCars();
        verify(req).setAttribute("info", "Запись успешно удалена (id = 1)");
        verify(ctx).setVariable("cars", cars);
        verify(engine).process("cars.html", ctx, writer);
    }

    @Test
    void deleteCar_carReference() throws IOException, SQLException {
        when(req.getPathInfo()).thenReturn("/delete");
        var customer = createCustomer();
        when(service.findCustomerByCarId(anyInt())).thenReturn(customer);
        List<Car> cars = Collections.emptyList();
        when(service.findAllCars()).thenReturn(cars);

        servlet.doGet(req, resp);

        verify(service, never()).deleteCar(anyInt());
        verify(service).findAllCars();
        verify(req).setAttribute("error", "Удалить автомобиль невозможно т.к. он связан с покупателем: lastName firstName patronymic");
        verify(ctx).setVariable("cars", cars);
        verify(engine).process("cars.html", ctx, writer);
    }

    @Test
    void deleteCar_sqlError() throws IOException, SQLException {
        when(req.getPathInfo()).thenReturn("/delete");
        when(service.findCustomerByCarId(anyInt())).thenThrow(new SQLException(MESSAGE));
        List<Car> cars = Collections.emptyList();
        when(service.findAllCars()).thenReturn(cars);

        servlet.doGet(req, resp);

        verify(service, never()).deleteCar(anyInt());
        verify(service).findAllCars();
        verify(req).setAttribute("error", MESSAGE);
        verify(ctx).setVariable("cars", cars);
        verify(engine).process("cars.html", ctx, writer);
    }

    @Test
    void showCars() throws IOException, SQLException {
        when(req.getPathInfo()).thenReturn("/");
        List<Car> cars = Collections.emptyList();
        when(service.findAllCars()).thenReturn(cars);

        servlet.doGet(req, resp);

        verify(service).findAllCars();
        verify(ctx).setVariable("cars", cars);
        verify(engine).process("cars.html", ctx, writer);
    }

    @Test
    void showCars_sqlError() throws IOException, SQLException {
        when(req.getPathInfo()).thenReturn("/");
        when(service.findAllCars()).thenThrow(new SQLException(MESSAGE));

        servlet.doGet(req, resp);

        verify(service).findAllCars();
        verify(ctx).setVariable("error", MESSAGE);
        verify(engine).process("cars.html", ctx, writer);
    }
}