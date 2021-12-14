package com.beyond.interceptor;

import com.beyond.utils.NormalException;
import com.beyond.utils.ResponseBean;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@ControllerAdvice
@EnableWebMvc
public class GlobalControllerAdvice {


    // // 捕捉shiro的异常
    // @ResponseStatus(HttpStatus.UNAUTHORIZED)
    // @ExceptionHandler(ShiroException.class)
    // public ResponseBean handle401(ShiroException e) {
    // return new ResponseBean(401, e.getMessage(), null);
    // }
    //
    // // 捕捉UnauthorizedException
    // @ResponseStatus(HttpStatus.UNAUTHORIZED)
    // @ExceptionHandler(UnauthorizedException.class)
    // public ResponseBean handle401() {
    // return new ResponseBean(401, "Unauthorized", null);
    // }

    // 捕捉自定义异常
    @ExceptionHandler(value = NormalException.class)
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody
    ResponseBean globalException(NormalException ex) {

        return new ResponseBean(44444, ex.getMessage(), null);
    }

    // 捕捉其他所有异常
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public @ResponseBody
    ResponseBean globalException(HttpServletRequest request, Throwable ex) {
        log.warn(ex.getMessage());
        ex.printStackTrace();
        return new ResponseBean(getStatus(request).value(), ex.getMessage(), null);

    }

    private HttpStatus getStatus(HttpServletRequest request) {
        Integer statusCode = (Integer) request.getAttribute("javax.servlet.error.status_code");
        if (statusCode == null) {
            return HttpStatus.INTERNAL_SERVER_ERROR;
        }
        return HttpStatus.valueOf(statusCode);
    }
}
