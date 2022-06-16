package com.ldnhat.springbootblog.security;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

//
//AuthenticationEntryPoint được sử dụng để gửi phản hồi HTTP yêu cầu thông tin xác thực từ máy khách
@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    //phương thức được gọi bất cứ khi nào một ngoại lệ
    // được ném ra do người dùng xác thực cố gắng truy cập vào một tài nguyên yêu cầu xác thực
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException authException) throws IOException, ServletException {
        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, authException.getMessage());
    }
}
