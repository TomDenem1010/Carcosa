package com.home.carcosa.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
@Slf4j(topic = "FILTER")
public class RequestResponseLoggingFilter extends OncePerRequestFilter{

    private static final int MAX_PAYLOAD_BYTES = 1024 * 1024;
    private static final int MAX_LOG_BODY_CHARS = 8_192;

    @Override
    protected void doFilterInternal(HttpServletRequest request,HttpServletResponse response,FilterChain filterChain)
            throws ServletException, IOException{

        ContentCachingRequestWrapper requestWrapper = (request instanceof ContentCachingRequestWrapper)
                ? (ContentCachingRequestWrapper) request
                : new ContentCachingRequestWrapper(request, MAX_PAYLOAD_BYTES);

        ContentCachingResponseWrapper responseWrapper = new ContentCachingResponseWrapper(response);

        long startNanos = System.nanoTime();
        try{
            filterChain.doFilter(requestWrapper,responseWrapper);
        } finally{
            long durationMillis = (System.nanoTime() - startNanos) / 1_000_000;

            String method = requestWrapper.getMethod();
            String uri = requestWrapper.getRequestURI();
            String query = requestWrapper.getQueryString();
            String fullPath = (query == null || query.isBlank()) ? uri : (uri + "?" + query);

            int status = responseWrapper.getStatus();

            String requestBody = readRequestBody(requestWrapper);
            String responseBody = readResponseBody(responseWrapper);

            log.info("HTTP {} {} -> {} ({}ms) requestBody={} responseBody={}",method,fullPath,status,durationMillis,
                    requestBody,responseBody);

            responseWrapper.copyBodyToResponse();
        }
    }

    private static String readRequestBody(ContentCachingRequestWrapper requestWrapper){
        byte[] bytes = requestWrapper.getContentAsByteArray();
        if (bytes == null || bytes.length == 0){
            return "<empty>";
        }

        Charset charset = resolveCharset(requestWrapper.getCharacterEncoding());
        return abbreviate(new String(bytes, charset));
    }

    private static String readResponseBody(ContentCachingResponseWrapper responseWrapper){
        byte[] bytes = responseWrapper.getContentAsByteArray();
        if (bytes == null || bytes.length == 0){
            return "<empty>";
        }

        Charset charset = resolveCharset(responseWrapper.getCharacterEncoding());
        return abbreviate(new String(bytes, charset));
    }

    private static Charset resolveCharset(String encoding){
        if (encoding == null || encoding.isBlank()){
            return StandardCharsets.UTF_8;
        }

        try{
            return Charset.forName(encoding);
        } catch (Exception ignored){
            return StandardCharsets.UTF_8;
        }
    }

    private static String abbreviate(String body){
        if (body == null){
            return "<null>";
        }

        String normalized = body.replace("\r","\\r").replace("\n","\\n");
        if (normalized.length() <= MAX_LOG_BODY_CHARS){
            return normalized;
        }

        return normalized.substring(0,MAX_LOG_BODY_CHARS) + "...<truncated>";
    }
}
