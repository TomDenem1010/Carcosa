package com.home.carcosa.usermanagement.security;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import com.home.carcosa.usermanagement.Role;
import com.home.carcosa.usermanagement.service.UserService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;

@Component
@AllArgsConstructor
public class RoleAuthorizationInterceptor implements HandlerInterceptor{

    private static final String HEADER_AUTHORIZATION = "Authorization";
    private static final String BASIC_PREFIX = "Basic ";

    private final UserService userService;

    @Override
    public boolean preHandle(HttpServletRequest request,HttpServletResponse response,Object handler) throws Exception{

        if (!(handler instanceof HandlerMethod handlerMethod)){
            return true;
        }

        RequireRole requireRole = resolveRequireRole(handlerMethod);
        if (requireRole == null){
            return true;
        }

        Role requiredRole = requireRole.value();
        Credentials credentials = parseBasicAuth(request.getHeader(HEADER_AUTHORIZATION));
        if (credentials == null){
            response.setHeader("WWW-Authenticate","Basic realm=\"carcosa\"");
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
            return false;
        }

        boolean allowed = userService.checkUserHasRole(credentials.username(),credentials.password(),requiredRole);
        if (!allowed){
            response.sendError(HttpServletResponse.SC_FORBIDDEN);
            return false;
        }

        return true;
    }

    private static RequireRole resolveRequireRole(HandlerMethod handlerMethod){
        RequireRole methodAnnotation = AnnotationUtils.findAnnotation(handlerMethod.getMethod(),RequireRole.class);
        if (methodAnnotation != null){
            return methodAnnotation;
        }

        return AnnotationUtils.findAnnotation(handlerMethod.getBeanType(),RequireRole.class);
    }

    private static Credentials parseBasicAuth(String authHeader){
        if (authHeader == null || !authHeader.startsWith(BASIC_PREFIX)){
            return null;
        }

        String base64Credentials = authHeader.substring(BASIC_PREFIX.length()).trim();
        String decoded;
        try{
            decoded = new String(Base64.getDecoder().decode(base64Credentials), StandardCharsets.UTF_8);
        } catch (Exception ignored){
            return null;
        }

        String[] parts = decoded.split(":",2);
        if (parts.length != 2 || parts[0].isBlank() || parts[1].isBlank()){
            return null;
        }

        return new Credentials(parts[0], parts[1]);
    }

    private record Credentials(String username, String password) {
    }
}
