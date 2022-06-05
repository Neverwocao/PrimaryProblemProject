package com.example.primaryproblem.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

/**
 * 生成试卷【选项】的Vo
 */
// TODO 参数校验！
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProblemInfoVo {

    // 记录运算题计算类型
    @Size(min = 1, max = 4, message = "请选择运算类型")
    private List<Integer> calStyle;

    // 算式最大值
    // todo 做乘法容易出问题
    @Max(value = 100, message = "运算范围过大,建议先从小于100练起!")
    @NotNull(message = "请选择运算范围")
    private Integer maxNum;

    // 运算次数
    @Max(value = 5, message = "运算次数过多,请小于5次")
    @NotNull(message = "请选择运算步数")
    private Integer stepNum;

    // 题目数,最少5题
    @Range(min = 5, max = 30, message = "目前最少生成5题,最多只支持30题")
    @NotNull(message = "请选择题目数量")
    private Integer problemNum = Integer.valueOf(5);

    @NotEmpty(message = "临时用户身份过期,请重新刷新页面或登录!")
    @NotEmpty
    private String token;

    private String atomToken;
}
