package org.example.carshowroom.servlet;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.carshowroom.config.TemplateConfig;
import org.example.carshowroom.model.Car;
import org.example.carshowroom.model.Customer;
import org.example.carshowroom.service.DataService;
import org.example.carshowroom.service.DataServiceImpl;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.util.StringUtils;

import java.io.IOException;
import java.util.Optional;

import static java.lang.String.format;

@WebServlet("/customer/*")
public class CustomerServlet extends HttpServlet {

    private final DataService service;

    public CustomerServlet() {
        this.service = DataServiceImpl.getInstance();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        var path = req.getPathInfo() == null ? req.getServletPath() : req.getPathInfo();

        switch (path) {
            case "/new":
                showNewForm(req, resp);
                break;
            case "/edit":
                showEditForm(req, resp);
                break;
            case "/insert":
                insertCustomer(req, resp);
                break;
            case "/update":
                updateCustomer(req, resp);
                break;
            case "/delete":
                deleteCustomer(req, resp);
                break;
            default:
                showCustomers(req, resp);
        }
    }

    private void showNewForm(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        var ctx = getContext(req, resp);

        try {
            var cars = service.findVacantCars();
            ctx.setVariable("cars", cars);
        } catch (Exception e) {
            ctx.setVariable("error", e.getMessage());
        }

        TemplateConfig.getEngine().process("customer.html", ctx, resp.getWriter());
    }

    private void showEditForm(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        var ctx = getContext(req, resp);

        try {
            var customer = service.findCustomerById(getId(req));
            ctx.setVariable("customer", customer);
            var cars = service.findVacantCars();
            cars.add(customer.getCar());
            ctx.setVariable("cars", cars);
        } catch (Exception e) {
            ctx.setVariable("error", e.getMessage());
        }

        TemplateConfig.getEngine().process("customer.html", ctx, resp.getWriter());
    }

    private void insertCustomer(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            var customer = getCustomerFromRequest(req);
            service.createCustomer(customer);
            req.setAttribute("info", format("Запись успешно добавлена: %s", customer));
        } catch (Exception e) {
            req.setAttribute("error", e.getMessage());
        } finally {
            showCustomers(req, resp);
        }
    }

    private void updateCustomer(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            var customer = getCustomerFromRequest(req);
            service.updateCustomer(customer);
            req.setAttribute("info", format("Запись успешно обновлена: %s", customer));
        } catch (Exception e) {
            req.setAttribute("error", e.getMessage());
        } finally {
            showCustomers(req, resp);
        }

    }

    private void deleteCustomer(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            var id = getId(req);
            service.deleteCustomer(id);
            req.setAttribute("info", format("Запись успешно удалена (id = %s)", id));
        } catch (Exception e) {
            req.setAttribute("error", e.getMessage());
        } finally {
            showCustomers(req, resp);
        }
    }

    private void showCustomers(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        var ctx = getContext(req, resp);

        try {
            ctx.setVariable("customers", service.findAllCustomers());
        } catch (Exception e) {
            ctx.setVariable("error", e.getMessage());
        }

        TemplateConfig.getEngine().process("customers.html", ctx, resp.getWriter());
    }

    private WebContext getContext(HttpServletRequest req, HttpServletResponse resp) {
        var ctx = TemplateConfig.getWebContext(req, resp);
        ctx.setVariable("error", req.getAttribute("error"));
        req.removeAttribute("error");
        ctx.setVariable("info", req.getAttribute("info"));
        req.removeAttribute("info");
        return ctx;
    }

    private static Integer getId(HttpServletRequest req) {
        return Optional.of(req.getParameter("id"))
                .filter(s -> !StringUtils.isEmptyOrWhitespace(s))
                .map(Integer::parseInt)
                .orElse(null);
    }

    private static Customer getCustomerFromRequest(HttpServletRequest req) {
        var id = getId(req);
        var lastName = req.getParameter("lastName");
        var firstName = req.getParameter("firstName");
        var patronymic = req.getParameter("patronymic");
        var carId = Integer.parseInt(req.getParameter("carId"));
        var car = new Car(carId);
        return new Customer(id, lastName, firstName, patronymic, car);
    }
}
