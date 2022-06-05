package com.example.primaryproblem.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 生成的试卷的Vo
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProblemVo {

    private String token;

    //private String atomToken;

    private List<Problem> problems;
}
