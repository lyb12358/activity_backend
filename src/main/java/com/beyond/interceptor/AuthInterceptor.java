package com.beyond.interceptor;

import com.beyond.utils.ResponseBean;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@Slf4j
@Component
public class AuthInterceptor implements HandlerInterceptor {


    private static List<String> uncheckUrls = Lists.newArrayList("/user/login", "imageUpload/product", "product/batchUpload");
    private static List<String> uncheckUrlEnds = Lists.newArrayList(".jpg", ".jpeg", ".png", ".woff", ".ttf");


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {

        response.setHeader("Access-control-Allow-Origin", request.getHeader("Origin"));
        response.setHeader("Access-Control-Allow-Methods", "GET,POST,OPTIONS,PUT,DELETE");
        response.setHeader("Access-Control-Allow-Headers", request.getHeader("Access-Control-Request-Headers"));
        // 跨域时会首先发送一个option请求，这里我们给option请求直接返回正常状态
        if (request.getMethod().equals(RequestMethod.OPTIONS.name())) {
            log.info("this is a option request");
            response.setStatus(HttpStatus.OK.value());
            return false;
        }

        String url = request.getRequestURI();
        url = url.replace(request.getContextPath(), "");

        for (String s : uncheckUrlEnds) {
            if (url.endsWith(s)) {
                log.info("access uncheckUrlEnds：" + url);
                return true;
            }
        }
        return true;
//        String token = request.getHeader("imsToken");
//        if (token != null) {
//            String account = JWTUtil.getAccount(token);
//            if (account != null) {
//                if (JWTUtil.verify(token, account)) {
//                    HandlerMethod method = (HandlerMethod) handler;
//                    AuthCheck auth = method.getMethodAnnotation(AuthCheck.class);
//                    if (auth != null) {
//                        TUserLyb user = userService.getRbac(account);
//                        String permission = auth.permission();
//                        if (user.getPermissions().contains(permission)) {
//                            return true;
//                        } else {
//                            log.info("权限不足");
//                            returnMessage(response, 50002, "权限不足");
//                            return false;
//                        }
//
//                    }
//                    return true;
//                } else {
//                    log.info("token验证失败");
//                    returnMessage(response, 50001, "登录超时/验证失败");
//                    return false;
//                }
//
//            } else {
//                log.info("payload非法");
//                returnMessage(response, 50001, "非法的token");
//                return false;
//            }
//
//        } else {
//            if (uncheckUrls.contains(url)) {// 允许访问登录页面资源
//                log.info("access uncheckUrls：" + url);
//                return true;
//            } else {
//                returnMessage(response, 50000, "非法的访问");
//                return false;
//            }
//        }
    }


    private void returnMessage(HttpServletResponse response, int code, String msg) throws IOException {
        ResponseBean result = new ResponseBean(code, msg, null);
        response.setContentType("application/json; charset=utf-8");
        PrintWriter out = response.getWriter();
        ObjectMapper mapper = new ObjectMapper();
        String jsonOfResult = mapper.writeValueAsString(result);
        out.print(jsonOfResult);
        out.close();
        response.flushBuffer();
    }

    @Override
    public void afterCompletion(HttpServletRequest arg0, HttpServletResponse arg1, Object arg2, Exception arg3)
            throws Exception {

    }

    @Override
    public void postHandle(HttpServletRequest arg0, HttpServletResponse arg1, Object arg2, ModelAndView arg3)
            throws Exception {

    }
}
