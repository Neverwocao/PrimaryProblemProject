package com.example.primaryproblem.service.impl;

import com.example.primaryproblem.constant.HandleStatus;
import com.example.primaryproblem.service.ResponseHandleService;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;

/**
 * @author pzw
 * @data 2022/6/1 10:14
 * @apiNote
 */
@Service
public class ResponseHandleServiceImpl implements ResponseHandleService {


    @Override
    public void setResponseHeaderMessage(HttpServletResponse response, HandleStatus handleStatus) {

        response.setHeader(handleStatus.getHandleKey(),handleStatus.getStatus());

    }
}
