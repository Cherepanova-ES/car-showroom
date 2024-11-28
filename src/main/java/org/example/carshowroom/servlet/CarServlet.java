package org.example.carshowroom.servlet;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.carshowroom.config.TemplateConfig;
import org.example.carshowroom.model.Car;
import org.example.carshowroom.service.DataService;
import org.example.carshowroom.service.DataServiceImpl;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.util.StringUtils;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Optional;

import static java.lang.String.format;

@WebServlet({"", "/car/*"})
public class CarServlet extends HttpServlet {

    private final DataService service;

    public CarServlet() {
        this.service = DataServiceImpl.getInstance();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        var path = req.getPathInfo();
        switch (path) {
            case "/new":
                showNewForm(req, resp);
                break;
            case "/edit":
                showEditForm(req, resp);
                break;
            case "/insert":
                insertCar(req, resp);
                break;
            case "/update":
                updateCar(req, resp);
                break;
            case "/delete":
                deleteCar(req, resp);
                break;
            default:
                showCars(req, resp);
        }
    }

    private void showNewForm(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        var ctx = getContext(req, resp);
        TemplateConfig.getEngine().process("car.html", ctx, resp.getWriter());
    }

    private void showEditForm(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        var ctx = getContext(req, resp);

        try {
            var car = service.findCarById(getId(req));
            ctx.setVariable("car", car);
        } catch (Exception e) {
            ctx.setVariable("error", e.getMessage());
        }

        TemplateConfig.getEngine().process("car.html", ctx, resp.getWriter());
    }

    private void insertCar(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            var car = getCarFromRequest(req);
            service.createCar(car);
            req.setAttribute("info", format("Запись успешно добавлена: %s", car));
        } catch (Exception e) {
            req.setAttribute("error", e.getMessage());
        } finally {
            showCars(req, resp);
        }
    }

    private void updateCar(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            var car = getCarFromRequest(req);
            service.updateCar(car);
            req.setAttribute("info", format("Запись успешно обновлена: %s", car));
        } catch (Exception e) {
            req.setAttribute("error", e.getMessage());
        } finally {
            showCars(req, resp);
        }

    }

    private void deleteCar(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            var id = getId(req);
            var customer = service.findCustomerByCarId(id);

            if (customer == null) {
                service.deleteCar(id);
                req.setAttribute("info", format("Запись успешно удалена (id = %s)", id));
            } else {
                var message = format("Удалить автомобиль невозможно т.к. он связан с покупателем: %s", customer);
                throw new RuntimeException(message);
            }
        } catch (Exception e) {
            req.setAttribute("error", e.getMessage());
        } finally {
            showCars(req, resp);
        }
    }

    private void showCars(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        var ctx = getContext(req, resp);

        try {
            ctx.setVariable("cars", service.findAllCars());
        } catch (Exception e) {
            ctx.setVariable("error", e.getMessage());
        }

        TemplateConfig.getEngine().process("cars.html", ctx, resp.getWriter());
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

    private static Car getCarFromRequest(HttpServletRequest req) {
        var id = getId(req);
        var type = req.getParameter("type");
        var brand = req.getParameter("brand");
        var model = req.getParameter("model");
        var year = Short.parseShort(req.getParameter("year"));
        var engineVolume = Optional.of(req.getParameter("engineVolume"))
                .filter(s -> !StringUtils.isEmptyOrWhitespace(s))
                .map(BigDecimal::new)
                .orElse(null);
        var enginePower = Short.parseShort(req.getParameter("enginePower"));
        return new Car(id, type, brand, model, year, engineVolume, enginePower);
    }
}
