package net.proselyte.jwtappdemo.exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;

@Component
public class SecurityExceptionResolver implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException e)
            throws IOException, ServletException {
        HashMap<String, String> map = new HashMap<>(2);
        map.put("state: ", "401");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        if(e instanceof BadCredentialsException){
            map.put("state: ", "400");
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }
        if(e instanceof UsernameNotFoundException){
            map.put("state: ", "404");
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
        }
        map.put("message", e.getMessage());
        response.setCharacterEncoding("utf-8");
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        ObjectMapper objectMapper = new ObjectMapper();
        String resBody = objectMapper.writeValueAsString(map);
        PrintWriter printWriter = response.getWriter();
        printWriter.print(resBody);
        printWriter.flush();
        printWriter.close();
    }

//    public void getException(String s) throws JsonProcessingException {
//        HashMap<String, String> map = new HashMap<>(2);
//        map.put("uri", "ewqeqweqwqweeqw");
//        map.put("msg", "Ошибка аутентификации");
//        ObjectMapper objectMapper = new ObjectMapper();
//        String resBody = objectMapper.writeValueAsString(map);
//        PrintWriter printWriter = response.getWriter();
//        printWriter.print(resBody);
//        printWriter.flush();
//        printWriter.close();
//    }
}
