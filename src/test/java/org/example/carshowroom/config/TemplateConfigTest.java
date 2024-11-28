package org.example.carshowroom.config;

import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.WebApplicationTemplateResolver;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TemplateConfigTest {
    @Mock
    private HttpServletRequest req;
    @Mock
    private HttpServletResponse resp;
    @Mock
    private ServletContext context;
    @Mock
    private ServletContextEvent sce;
    @InjectMocks
    private TemplateConfig config;

    @Test
    void contextInitialized() {
        when(sce.getServletContext()).thenReturn(context);
        assertNull(TemplateConfig.getApplication());
        assertNull(TemplateConfig.getEngine());

        config.contextInitialized(sce);

        assertNotNull(TemplateConfig.getApplication());
        assertNotNull(TemplateConfig.getEngine());
        assertEquals(1, TemplateConfig.getEngine().getTemplateResolvers().size());
        var resolver = (WebApplicationTemplateResolver) TemplateConfig.getEngine().getTemplateResolvers().iterator().next();
        assertEquals(TemplateMode.HTML, resolver.getTemplateMode());
        assertEquals("/templates/", resolver.getPrefix());
        assertEquals(".html", resolver.getSuffix());
        assertEquals(3600000L, resolver.getCacheTTLMs());
        assertTrue(resolver.isCacheable());
    }

    @Test
    void getWebContext() {
        when(req.getServletContext()).thenReturn(context);
        when(sce.getServletContext()).thenReturn(context);
        config.contextInitialized(sce);
        assertNotNull(TemplateConfig.getWebContext(req, resp));
    }
}