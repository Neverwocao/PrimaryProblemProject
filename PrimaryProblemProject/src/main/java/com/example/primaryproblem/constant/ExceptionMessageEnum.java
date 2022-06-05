package com.example.primaryproblem.constant;

import lombok.Getter;
import lombok.Setter;

/**
 * @author pzw
 * @data 2022/5/22
 * @apiNote
 */
public enum ExceptionMessageEnum {

    UNKNOWN_EXCEPTION(5000,"未知异常,请与管理员联系!"),
    METHOD_ARGUMENTS_NOT_VALIED(4000, "提交参数不符合要求!"),
    REPEATED_GENERATE_PAPER(4001, "请先完成或提交当前题目!"),
    PRPEATED_SUBMIT_ANSWER(4002, "请勿重复提交试题!");


    @Getter
    private Integer code;
    @Getter
    private String message;

    ExceptionMessageEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
}
