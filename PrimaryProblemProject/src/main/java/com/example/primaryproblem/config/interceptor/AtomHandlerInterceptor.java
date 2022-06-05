package com.example.primaryproblem.config.interceptor;

import com.example.primaryproblem.Utils.HttpUtils;
import com.example.primaryproblem.annotion.Atomic;
import com.example.primaryproblem.constant.HandleStatus;
import com.example.primaryproblem.exception.RepeatedGeneratePaperExpection;
import com.example.primaryproblem.service.ResponseHandleService;
import com.example.primaryproblem.service.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.lang.reflect.Method;

/**
 * @author pzw
 * @data 2022/5/22
 * @apiNote
 */
@Configuration
@Order(100)
public class AtomHandlerInterceptor implements HandlerInterceptor {

    @Autowired
    private TokenService tokenService;

    @Autowired
    private ResponseHandleService responseHandleService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {


        if (!(handler instanceof HandlerMethod))
            return true;

        HandlerMethod handlerMethod = (HandlerMethod) handler;
        Method method = handlerMethod.getMethod();
        final Atomic annotation = method.getAnnotation(Atomic.class);

        if (annotation != null) {
            boolean checkAtomToken = tokenService.checkAtomToken(request);

            // todo 给 响应头上标记信息,解决幂等性 token 误删问题
            final HttpServletResponse httpResponse = HttpUtils.getHttpResponse();

            responseHandleService.setResponseHeaderMessage(httpResponse, HandleStatus.DELTOKEN_WITHOUT_HANDLE);


            if (checkAtomToken == true)
                return checkAtomToken;
            else
                throw new RepeatedGeneratePaperExpection();
        }

        return true;
    }
}
