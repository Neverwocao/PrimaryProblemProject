package com.example.primaryproblem.constant;

import lombok.Getter;

/**
 * @author pzw
 * @data 2022/6/1 10:07
 * @apiNote
 */
public enum HandleStatus {

    STATUS_MARK("atomTokenMark", "-1"),
    DELTOKEN_WITHOUT_HANDLE("atomTokenMark", "1"),
    DELTOKEN_ADN_HANDLE("atomTokenMark", "2");

    @Getter
    private String handleKey;
    @Getter
    private String status;

    HandleStatus(String handleKey, String status) {
        this.handleKey = handleKey;
        this.status = status;
    }
}
