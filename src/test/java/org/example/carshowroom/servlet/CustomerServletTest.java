package org.example.carshowroom.servlet;

import org.example.carshowroom.model.Car;
import org.example.carshowroom.model.Customer;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.example.carshowroom.TestUtils.createCustomer;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class CustomerServletTest extends AbstractServletTest {

    private final CustomerServlet servlet = new CustomerServlet();

    @Test
    void showNewForm() throws IOException, SQLException {
        when(req.getPathInfo()).thenReturn("/new");
        List<Car> cars = Collections.emptyList();
        when(service.findVacantCars()).thenReturn(cars);

        servlet.doGet(req, resp);

        verify(service).findVacantCars();
        verify(ctx).setVariable("cars", cars);
        verify(engine).process("customer.html", ctx, writer);
    }

    @Test
    void showNewForm_sqlError() throws IOException, SQLException {
        when(req.getPathInfo()).thenReturn("/new");
        when(service.findVacantCars()).thenThrow(new SQLException(MESSAGE));

        servlet.doGet(req, resp);

        verify(service).findVacantCars();
        verify(ctx).setVariable("error", MESSAGE);
        verify(engine).process("customer.html", ctx, writer);
    }

    @Test
    void showEditForm() throws IOException, SQLException {
        when(req.getPathInfo()).thenReturn("/edit");
        var customer = createCustomer();
        when(service.findCustomerById(anyInt())).thenReturn(customer);
        List<Car> cars = new ArrayList<>();
        when(service.findVacantCars()).thenReturn(cars);

        servlet.doGet(req, resp);

        verify(service).findCustomerById(1);
        verify(service).findVacantCars();
        verify(ctx).setVariable("customer", customer);
        verify(ctx).setVariable("cars", cars);
        verify(engine).process("customer.html", ctx, writer);
    }

    @Test
    void showEditForm_sqlError() throws IOException, SQLException {
        when(req.getPathInfo()).thenReturn("/edit");
        when(service.findCustomerById(anyInt())).thenThrow(new SQLException(MESSAGE));

        servlet.doGet(req, resp);

        verify(service).findCustomerById(1);
        verify(service, never()).findVacantCars();
        verify(ctx).setVariable("error", MESSAGE);
        verify(engine).process("customer.html", ctx, writer);
    }

    @Test
    void insertCustomer() throws IOException, SQLException {
        when(req.getPathInfo()).thenReturn("/insert");
        List<Customer> customers = Collections.emptyList();
        when(service.findAllCustomers()).thenReturn(customers);

        servlet.doGet(req, resp);

        verify(service).createCustomer(any(Customer.class));
        verify(service).findAllCustomers();
        verify(req).setAttribute("info", "Запись успешно добавлена: lastName firstName patronymic");
        verify(ctx).setVariable("customers", customers);
        verify(engine).process("customers.html", ctx, writer);
    }

    @Test
    void insertCustomer_sqlError() throws IOException, SQLException {
        when(req.getPathInfo()).thenReturn("/insert");
        List<Customer> customers = Collections.emptyList();
        doThrow(new SQLException(MESSAGE)).when(service).createCustomer(any());

        servlet.doGet(req, resp);

        verify(service).createCustomer(any(Customer.class));
        verify(service).findAllCustomers();
        verify(req).setAttribute("error", MESSAGE);
        verify(ctx).setVariable("customers", customers);
        verify(engine).process("customers.html", ctx, writer);
    }

    @Test
    void updateCustomer() throws IOException, SQLException {
        when(req.getPathInfo()).thenReturn("/update");
        List<Customer> customers = Collections.emptyList();
        when(service.findAllCustomers()).thenReturn(customers);

        servlet.doGet(req, resp);

        verify(service).updateCustomer(any(Customer.class));
        verify(service).findAllCustomers();
        verify(req).setAttribute("info", "Запись успешно обновлена: lastName firstName patronymic");
        verify(ctx).setVariable("customers", customers);
        verify(engine).process("customers.html", ctx, writer);
    }

    @Test
    void updateCustomer_sqlError() throws IOException, SQLException {
        when(req.getPathInfo()).thenReturn("/update");
        List<Customer> customers = Collections.emptyList();
        doThrow(new SQLException(MESSAGE)).when(service).updateCustomer(any());

        servlet.doGet(req, resp);

        verify(service).updateCustomer(any(Customer.class));
        verify(service).findAllCustomers();
        verify(req).setAttribute("error", MESSAGE);
        verify(ctx).setVariable("customers", customers);
        verify(engine).process("customers.html", ctx, writer);
    }

    @Test
    void deleteCustomer() throws IOException, SQLException {
        when(req.getPathInfo()).thenReturn("/delete");
        List<Customer> customers = Collections.emptyList();
        when(service.findAllCustomers()).thenReturn(customers);

        servlet.doGet(req, resp);

        verify(service).deleteCustomer(1);
        verify(service).findAllCustomers();
        verify(req).setAttribute("info", "Запись успешно удалена (id = 1)");
        verify(ctx).setVariable("customers", customers);
        verify(engine).process("customers.html", ctx, writer);
    }

    @Test
    void deleteCustomer_sqlError() throws IOException, SQLException {
        when(req.getPathInfo()).thenReturn("/delete");
        List<Customer> customers = Collections.emptyList();
        doThrow(new SQLException(MESSAGE)).when(service).deleteCustomer(anyInt());

        servlet.doGet(req, resp);

        verify(service).deleteCustomer(1);
        verify(service).findAllCustomers();
        verify(req).setAttribute("error", MESSAGE);
        verify(ctx).setVariable("customers", customers);
        verify(engine).process("customers.html", ctx, writer);
    }

    @Test
    void showCustomers() throws IOException, SQLException {
        when(req.getPathInfo()).thenReturn("/");
        List<Customer> customers = Collections.emptyList();
        when(service.findAllCustomers()).thenReturn(customers);

        servlet.doGet(req, resp);

        verify(service).findAllCustomers();
        verify(ctx).setVariable("customers", customers);
        verify(engine).process("customers.html", ctx, writer);
    }

    @Test
    void showCustomers_sqlError() throws IOException, SQLException {
        when(req.getPathInfo()).thenReturn("/");
        when(service.findAllCustomers()).thenThrow(new SQLException(MESSAGE));

        servlet.doGet(req, resp);

        verify(service).findAllCustomers();
        verify(ctx).setVariable("error", MESSAGE);
        verify(engine).process("customers.html", ctx, writer);
    }
}