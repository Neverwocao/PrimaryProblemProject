package com.example.primaryproblem.exception;

/**
 * @author pzw
 * @data 2022/5/22
 * @apiNote
 */
public class RepeatedGeneratePaperExpection extends RuntimeException{


    public RepeatedGeneratePaperExpection() {
    }

    public RepeatedGeneratePaperExpection(String message) {
        super(message);
    }

    public RepeatedGeneratePaperExpection(String message, Throwable cause) {
        super(message, cause);
    }

    public RepeatedGeneratePaperExpection(Throwable cause) {
        super(cause);
    }

    public RepeatedGeneratePaperExpection(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
