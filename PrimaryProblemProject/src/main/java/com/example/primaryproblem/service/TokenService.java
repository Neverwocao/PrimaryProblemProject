package com.example.primaryproblem.service;

import javax.servlet.http.HttpServletRequest;

/**
 * @author pzw
 * @data 2022/5/22
 * @apiNote
 */
public interface TokenService {


    String generateAtomToken();

    boolean checkAtomToken(HttpServletRequest request);

    String generateTempUserToken(HttpServletRequest request);

    String generateMarkAnswerToken(String answerJson);

}
