package fr.univlyon1.m1if.m1if10.appec;

import org.junit.jupiter.api.Test;
import org.springframework.boot.builder.SpringApplicationBuilder;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class ServletInitializerTest {

    @Test
    void configure() {
        SpringApplicationBuilder applicationBuilder = mock(SpringApplicationBuilder.class);
        ServletInitializer servletInitializer = new ServletInitializer();

        servletInitializer.configure(applicationBuilder);

        verify(applicationBuilder).sources(AppecApplication.class);
    }
}