package com.example.primaryproblem.Utils;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author pzw
 * @data 2022/6/1 10:20
 * @apiNote
 */
public class HttpUtils {


    public static HttpServletRequest getHttpRequest() {
        ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();

        return servletRequestAttributes.getRequest();
    }


    public static HttpServletResponse getHttpResponse() {

        ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();

        return servletRequestAttributes.getResponse();
    }

}
