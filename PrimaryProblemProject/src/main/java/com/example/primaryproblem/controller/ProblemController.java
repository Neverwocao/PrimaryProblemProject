package com.example.primaryproblem.controller;

import com.alibaba.fastjson.JSON;
import com.example.primaryproblem.Utils.HttpUtils;
import com.example.primaryproblem.Utils.R;
import com.example.primaryproblem.annotion.Atomic;
import com.example.primaryproblem.constant.HandleStatus;
import com.example.primaryproblem.service.ProblemService;
import com.example.primaryproblem.service.ResponseHandleService;
import com.example.primaryproblem.service.TokenService;
import com.example.primaryproblem.vo.*;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Slf4j
@Controller
public class ProblemController {

    @Autowired
    private ProblemService problemService;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private ResponseHandleService responseHandleService;

    // todo 为了保证该接口的幂等性，我们还可以对用户访问该接口进行限流——使用redis的信号量机制,保证在一段时间内只能提交指定的次数
    @PostMapping(value = "/generate")
    @ResponseBody
    @Atomic // 实现接口幂等性的注解
    public R generateProblem(@Valid @RequestBody ProblemInfoVo problemInfoVo) {

        log.info("enter......generateProblem,args{}", problemInfoVo);
        // 生成的问题包含答案
        List<Problem> arithmetics = problemService.generateArithmetics(problemInfoVo);

        // 对生成的问题进行处理
        ProblemVo problemVo = problemService.getProblemVo(arithmetics);

        responseHandleService.setResponseHeaderMessage(HttpUtils.getHttpResponse(), HandleStatus.DELTOKEN_ADN_HANDLE);

        log.info("leave.......generateProblem,args{}", problemVo);

        return R.ok().put("problemVo", problemVo);
    }

    // todo 该功能用于日后可能的拓展
    // 用户一进页面，我们给他一个token表示身份,默认存在30分钟
    @GetMapping("/getTempToken")
    @ResponseBody
    public String generateToken(HttpServletRequest request) {
        log.info("enter......generateToken");
        String token = tokenService.generateTempUserToken(request);
        log.info("leave......generateToken");
        return token;
    }

    // todo 处理重复提交
    @GetMapping("/getAtomToken")
    @ResponseBody
    public String generateAtomToken() {
        log.info("enter...generateAtomToken");
        String atomToken = tokenService.generateAtomToken();
        log.info("leave...generateAtomToken");
        return atomToken;
    }

    // todo 重复提交问题
    @PostMapping("/correctProblem")
    @ResponseBody
    public R correctProblem(@RequestBody ProblemAnswerVo problemAnswerVo) {

        log.info("enter...correctProblem,args:{}", problemAnswerVo);
        ProblemScoreInfoVo scoreInfoVo = problemService.correctProblem(problemAnswerVo);
        log.info("leave...correctProblem,args:{}", scoreInfoVo);
        return R.ok().put("scoreInfoVo", scoreInfoVo);
    }
}
