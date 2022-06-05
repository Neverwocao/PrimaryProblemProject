package com.example.primaryproblem.constant;

import lombok.Getter;

public enum CalStyleEnum {

    ADD("加法运算", 1),
    SUB("减法运算", 2),
    MULT("乘法运算", 3),
    DIVI("除法运算", 4);


    @Getter
    private String description;
    @Getter
    private Integer CalStyleCode;

    CalStyleEnum(String description, Integer calStyleCode) {
        this.description = description;
        CalStyleCode = calStyleCode;
    }
}
