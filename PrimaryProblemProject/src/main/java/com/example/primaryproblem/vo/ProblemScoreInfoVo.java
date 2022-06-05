package com.example.primaryproblem.vo;

import lombok.Data;

import java.util.List;

@Data
/**
 * 批改完毕的响应信息
 */
public class ProblemScoreInfoVo {

    // 总分数
    private Integer score;

    // 封装 题目正确与否相关状态信息
    private List<Answer> problemAnswers;

    private String atomToken;
}
