package com.wandson.ecommerce;

import jakarta.servlet.DispatcherType;
import jakarta.servlet.FilterRegistration;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletRegistration;
import org.springframework.orm.jpa.support.OpenEntityManagerInViewFilter;
import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.request.RequestContextListener;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.servlet.DispatcherServlet;

import java.util.EnumSet;

public class InicializadorSpringWeb implements WebApplicationInitializer {

    private static final String DISPATCHER = "dispatcher";

    @Override
    public void onStartup(ServletContext servletContext) {
        var applicationContext = new AnnotationConfigWebApplicationContext();

        applicationContext.scan(InicializadorSpringWeb.class.getPackage().getName());

        servletContext.addListener(new ContextLoaderListener(applicationContext));
        servletContext.addListener(new RequestContextListener());

        FilterRegistration.Dynamic characterEncodingFilter = servletContext
                .addFilter("characterEncodingFilter", characterEncodingFilter());
        characterEncodingFilter.setAsyncSupported(true);
        characterEncodingFilter.addMappingForServletNames(dispatcherTypes(), false, DISPATCHER);

        FilterRegistration.Dynamic openEntityManagerInViewFilter = servletContext
                .addFilter("openEntityManagerInViewFilter", openEntityManagerInViewFilter());
        openEntityManagerInViewFilter.setAsyncSupported(true);
        openEntityManagerInViewFilter.addMappingForServletNames(dispatcherTypes(), false, DISPATCHER);

        ServletRegistration.Dynamic dispatcher = servletContext
                .addServlet(DISPATCHER, dispatcherServlet(applicationContext));
        dispatcher.setAsyncSupported(true);
        dispatcher.setLoadOnStartup(1);
        dispatcher.addMapping("/");
    }

    private DispatcherServlet dispatcherServlet(WebApplicationContext applicationContext) {
        return new DispatcherServlet(applicationContext);
    }

    private CharacterEncodingFilter characterEncodingFilter() {
        var filter = new CharacterEncodingFilter();

        filter.setEncoding("UTF-8");
        filter.setForceEncoding(true);

        return filter;
    }

    private OpenEntityManagerInViewFilter openEntityManagerInViewFilter() {
        return new OpenEntityManagerInViewFilter();
    }

    private EnumSet<DispatcherType> dispatcherTypes() {
        return EnumSet.of(DispatcherType.REQUEST, DispatcherType.FORWARD,
                DispatcherType.INCLUDE, DispatcherType.ASYNC);
    }
}