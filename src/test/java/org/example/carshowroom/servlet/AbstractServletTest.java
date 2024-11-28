package org.example.carshowroom.servlet;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.carshowroom.config.TemplateConfig;
import org.example.carshowroom.service.DataService;
import org.example.carshowroom.service.DataServiceImpl;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;

import java.io.IOException;
import java.io.PrintWriter;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.reset;

@ExtendWith(MockitoExtension.class)
abstract class AbstractServletTest {

    static final String MESSAGE = "message";

    static DataService service = mock(DataService.class);
    static MockedStatic<DataServiceImpl> dataServiceMock;
    static TemplateEngine engine = mock(TemplateEngine.class);
    static WebContext ctx = mock(WebContext.class);
    static MockedStatic<TemplateConfig> templateUtilMock;

    @Mock
    HttpServletRequest req;
    @Mock
    HttpServletResponse resp;
    @Mock
    PrintWriter writer;

    @BeforeAll
    static void beforeAll() {
        dataServiceMock = mockStatic(DataServiceImpl.class);
        dataServiceMock.when(DataServiceImpl::getInstance).thenReturn(service);
        templateUtilMock = mockStatic(TemplateConfig.class);
        templateUtilMock.when(TemplateConfig::getEngine).thenReturn(engine);
        templateUtilMock.when(() -> TemplateConfig.getWebContext(any(), any())).thenReturn(ctx);
    }

    @BeforeEach
    void setUp() throws IOException {
        reset(ctx);
        reset(service);
        lenient().when(resp.getWriter()).thenReturn(writer);
        lenient().when(req.getParameter("id")).thenReturn("1");
        lenient().when(req.getParameter("type")).thenReturn("type");
        lenient().when(req.getParameter("brand")).thenReturn("brand");
        lenient().when(req.getParameter("model")).thenReturn("model");
        lenient().when(req.getParameter("year")).thenReturn("2020");
        lenient().when(req.getParameter("engineVolume")).thenReturn("1");
        lenient().when(req.getParameter("enginePower")).thenReturn("100");
        lenient().when(req.getParameter("lastName")).thenReturn("lastName");
        lenient().when(req.getParameter("firstName")).thenReturn("firstName");
        lenient().when(req.getParameter("patronymic")).thenReturn("patronymic");
        lenient().when(req.getParameter("carId")).thenReturn("1");
    }

    @AfterAll
    static void afterAll() {
        dataServiceMock.close();
        templateUtilMock.close();
    }
}