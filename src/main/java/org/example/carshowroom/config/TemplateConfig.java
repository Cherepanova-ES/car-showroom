package org.example.carshowroom.config;

import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Getter;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ITemplateResolver;
import org.thymeleaf.templateresolver.WebApplicationTemplateResolver;
import org.thymeleaf.web.servlet.JakartaServletWebApplication;

@WebListener
public class TemplateConfig implements ServletContextListener {

    @Getter
    private static JakartaServletWebApplication application;
    @Getter
    private static TemplateEngine engine;

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        var context = sce.getServletContext();
        application = JakartaServletWebApplication.buildApplication(context);
        engine = templateEngine(application);
    }

    public static WebContext getWebContext(HttpServletRequest req, HttpServletResponse resp) {
        return new WebContext(application.buildExchange(req, resp));
    }

    private TemplateEngine templateEngine(JakartaServletWebApplication application) {
        var templateEngine = new TemplateEngine();
        var resolver = templateResolver(application);
        templateEngine.setTemplateResolver(resolver);
        return templateEngine;
    }

    private ITemplateResolver templateResolver(JakartaServletWebApplication application) {
        var resolver = new WebApplicationTemplateResolver(application);
        resolver.setTemplateMode(TemplateMode.HTML);
        resolver.setPrefix("/templates/");
        resolver.setSuffix(".html");
        resolver.setCacheTTLMs(3600000L);
        resolver.setCacheable(true);
        return resolver;
    }
}
