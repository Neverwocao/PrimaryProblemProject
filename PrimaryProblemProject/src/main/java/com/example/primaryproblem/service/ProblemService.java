package com.example.primaryproblem.service;

import com.example.primaryproblem.vo.*;

import java.util.ArrayList;
import java.util.List;

public interface ProblemService {

    List<Problem> generateArithmetics(ProblemInfoVo problemInfoVo);

    List<Answer> getAnswer(List<Problem> problems);

    ProblemScoreInfoVo correctProblem(ProblemAnswerVo problemAnswerVo);

    List<Problem> getProblemWithoutResult(List<Problem> arithmetics);

    ProblemVo getProblemVo(List<Problem> arithmetics);
}
