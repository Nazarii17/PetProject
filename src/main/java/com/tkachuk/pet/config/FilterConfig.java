package com.tkachuk.pet.config;

import com.tkachuk.pet.filters.RequestResponseFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FilterConfig {
    ////TODO How to use it?
    // uncomment this and comment the @Component in the filter class definition to register only for a url pattern
//     @Bean
    public FilterRegistrationBean<RequestResponseFilter> loggingFilter() {
        FilterRegistrationBean<RequestResponseFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new RequestResponseFilter());
        registrationBean.addUrlPatterns("/users/*");

        return registrationBean;
    }
}
