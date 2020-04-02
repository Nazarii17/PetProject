package com.tkachuk.pet.filters;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class RequestResponseFilter implements Filter {

    private final static Logger LOG = LoggerFactory.getLogger(RequestResponseFilter.class);

    @Override
    public void init(final FilterConfig filterConfig) throws ServletException {
        LOG.info("Initializing filter :{}", this);
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse myResponse= (HttpServletResponse) response;

        if (httpRequest.getRequestURL().toString().toLowerCase().contains("/filter-test")){
            myResponse.addHeader("Test", "FILTERED");
            myResponse.sendRedirect("/");
            LOG.info("Logging Request  {} : {}", httpRequest.getMethod(), httpRequest.getRequestURI());
            chain.doFilter(httpRequest, myResponse);
            LOG.info("Logging Response :{}", myResponse.getContentType());
            return;
        }
        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
        LOG.warn("Destructing filter :{}", this);
    }
}