package com.example.primaryproblem;

import com.alibaba.fastjson.JSON;
import com.example.primaryproblem.service.ProblemService;
import com.example.primaryproblem.vo.ProblemInfoVo;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class PrimaryProblemProjectApplicationTests {

    @Autowired
    private ProblemService problemService;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    //@Test
    //public void testRedis(){
    //
    //    List<Integer> calStyle = new ArrayList<>();
    //    calStyle.add(1);
    //    calStyle.add(2);
    //    calStyle.add(3);
    //    calStyle.add(4);
    //    Integer maxNum = 100;
    //    Integer stepNum = 3;
    //    Integer problemNum = 30;
    //    ProblemInfoVo problemInfoVo = new ProblemInfoVo(calStyle, maxNum, stepNum, problemNum);
    //
    //    String json = JSON.toJSONString(problemInfoVo);
    //
    //    stringRedisTemplate.opsForValue().set("k3",json);
    //
    //}

    //@Test
    //public void testProblemService() {
    //
    //    List<Integer> calStyle = new ArrayList<>();
    //    calStyle.add(1);
    //    calStyle.add(2);
    //    calStyle.add(3);
    //    calStyle.add(4);
    //    Integer maxNum = 100;
    //    Integer stepNum = 3;
    //    Integer problemNum = 30;
    //    ProblemInfoVo problemInfoVo = new ProblemInfoVo(calStyle, maxNum, stepNum, problemNum);
    //
    //    List<String> strings = problemService.generateArithmetics(problemInfoVo);
    //
    //    Iterator<String> iterator = strings.iterator();
    //    while (iterator.hasNext()) {
    //        System.out.println(iterator.next());
    //    }
    //
    //    String json = JSON.toJSONString(strings);
    //
    //    stringRedisTemplate.opsForValue().set("k2",json);
    //
    //}

    @Test
    public void contextLoads() {

//        StringBuffer stringBuffer = new StringBuffer("1 + 2 + ");
//
//        StringBuffer replace = stringBuffer.replace(stringBuffer.lastIndexOf("+"), stringBuffer.lastIndexOf("+") + 1, "=");
//
//        replace.replace(0,replace.length() + 1,"");
//        System.out.println(replace);

        Integer a = 1;
        int b = 1;

        System.out.println(a == b);
    }

}
