package com.senlime.nexus.common.filter;

import com.senlime.nexus.common.util.ContentCachingRequestWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.util.ContentCachingResponseWrapper;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;

public class RestfulLogFilter implements Filter {

    private static final Logger logger = LoggerFactory.getLogger(RestfulLogFilter.class);

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
        throws IOException, ServletException {

        if (logger.isDebugEnabled()) {
            HttpServletRequest httpRequest = (HttpServletRequest) request;
            HttpServletResponse httpResponse = (HttpServletResponse) response;
            ContentCachingRequestWrapper httpRequestWrapper = new ContentCachingRequestWrapper(httpRequest);
            ContentCachingResponseWrapper httpResponseWrapper = new ContentCachingResponseWrapper(httpResponse);
            StringBuilder buf = new StringBuilder();
            buf.append("\n----------incoming message----------\n");
            buf.append(httpRequestWrapper.getMethod()).append(" ");
            buf.append(httpRequestWrapper.getRequestURI());

            Enumeration<String> names = httpRequestWrapper.getParameterNames();
            if (names.hasMoreElements()) buf.append("?");
            while (names.hasMoreElements()) {
                String name = names.nextElement();
                String value = httpRequest.getParameter(name);
                buf.append(String.format("%s=%s&", name, value));
            }
            buf.append("\n");

            names = httpRequestWrapper.getHeaderNames();
            while (names.hasMoreElements()) {
                String name = names.nextElement();
                String value = httpRequest.getHeader(name);
                buf.append(String.format("%s: %s\n", name, value));
            }
            buf.append("\n\n");

            InputStream in = httpRequestWrapper.getInputStream();
            while (in.read() != -1) ;
            httpRequestWrapper.getInputStream().reset();
            String requestBody = new String(httpRequestWrapper.getContentAsByteArray(), "UTF-8");
            buf.append(requestBody);
            buf.append("\n----------incoming message----------\n");
            logger.debug(buf.toString());

            chain.doFilter(httpRequestWrapper, httpResponseWrapper);


            buf = new StringBuilder();
            buf.append("\n----------outgoing message----------\n");
            for (String name : httpResponseWrapper.getHeaderNames()) {
                String value = httpResponseWrapper.getHeader(name);
                buf.append(String.format("%s : %s\n", name, value));
            }
            buf.append("\n\n");
            String responseBody = new String(httpResponseWrapper.getContentAsByteArray(), "UTF-8");
            buf.append(responseBody);
            buf.append("\n----------outgoing message----------\n");
            logger.debug(buf.toString());
            httpResponseWrapper.copyBodyToResponse();

        } else {
            chain.doFilter(request, response);
        }

    }

    @Override
    public void destroy() {
    }
}

