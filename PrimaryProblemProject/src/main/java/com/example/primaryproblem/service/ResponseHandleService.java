package com.example.primaryproblem.service;

import com.example.primaryproblem.constant.HandleStatus;

import javax.servlet.http.HttpServletResponse;

/**
 * @author pzw
 * @data 2022/6/1 10:13
 * @apiNote
 */
public interface ResponseHandleService {

    void setResponseHeaderMessage(HttpServletResponse response, HandleStatus handleStatus);

}
