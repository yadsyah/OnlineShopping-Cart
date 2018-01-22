package com.diansetiyadi.myapp.config;

import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.servlet.DispatcherServlet;

import javax.servlet.FilterRegistration;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;
import java.util.Set;

public class SpringWebAppInitializer implements WebApplicationInitializer{

    public void onStartup(ServletContext servletContext) throws ServletException {
        AnnotationConfigWebApplicationContext appContext = new AnnotationConfigWebApplicationContext();
        appContext.register(ApplicationContextConfig.class,WebMvcConfig.class);

        ServletRegistration.Dynamic dispatcher = servletContext.addServlet("dispatcher", new DispatcherServlet(appContext));
        dispatcher.setLoadOnStartup(1);
       // dispatcher.addMapping("/");
        Set<String> mappings = dispatcher.addMapping("/");

        ContextLoaderListener contextLoaderListener = new ContextLoaderListener(appContext);

        servletContext.addListener(contextLoaderListener);

        if (!mappings.isEmpty()) {
            throw new IllegalStateException("Conflicting mappings found! Terminating. " + mappings);
        }

        //Filter
        FilterRegistration.Dynamic filterRegistration = servletContext.addFilter("encodingFilter", CharacterEncodingFilter.class);
        filterRegistration.setInitParameter("encoding","UTF-8");
        filterRegistration.setInitParameter("forceEncoding","true");
        filterRegistration.addMappingForUrlPatterns(null,true,"/*");


    }
}
