package com.example.primaryproblem.exception;

/**
 * @author pzw
 * @data 2022/5/22
 * @apiNote
 */
public class RepeatedSubmitAnswerExpection extends RuntimeException{


    public RepeatedSubmitAnswerExpection() {
    }

    public RepeatedSubmitAnswerExpection(String message) {
        super(message);
    }

    public RepeatedSubmitAnswerExpection(String message, Throwable cause) {
        super(message, cause);
    }

    public RepeatedSubmitAnswerExpection(Throwable cause) {
        super(cause);
    }

    public RepeatedSubmitAnswerExpection(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
