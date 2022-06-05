package com.example.primaryproblem.vo;

import lombok.Data;

import java.util.List;


/**
 * 提交的试卷的Vo
 */
@Data
public class ProblemAnswerVo {

    // 试卷的 UUID
    private String token;

    // 提交的答案
    // todo 答案必须按顺序提交
    private List<Answer> answers;
}
