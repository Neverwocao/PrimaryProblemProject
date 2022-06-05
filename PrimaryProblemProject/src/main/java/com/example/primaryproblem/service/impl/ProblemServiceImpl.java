package com.example.primaryproblem.service.impl;

import com.alibaba.fastjson.JSON;
import com.example.primaryproblem.constant.CalStyleEnum;
import com.example.primaryproblem.exception.RepeatedSubmitAnswerExpection;
import com.example.primaryproblem.service.ProblemService;
import com.example.primaryproblem.service.TokenService;
import com.example.primaryproblem.vo.*;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import com.alibaba.fastjson.TypeReference;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ProblemServiceImpl implements ProblemService {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private TokenService tokenService;


    @Override
    // todo 1.传入的各种参数不能为空 2.位数必须 >=2
    public List<Problem> generateArithmetics(ProblemInfoVo problemInfoVo) {

        // 题目数
        int count = Integer.valueOf(problemInfoVo.getProblemNum());
        List<Integer> calStyle = problemInfoVo.getCalStyle();

        Map<Integer, Integer> styleWeight = calculateStyleWeight(calStyle, count);

        ArrayList<Problem> resultList = new ArrayList<>(count);

        Iterator<Integer> iterator = calStyle.iterator();
        // todo 有趣的问题：我们每次 调用 iterator.next() 后，指针都会后移，越界时会报 NoSuchElementException，因此每次循环里只用一次 iterator.next()
        // todo 抽取
        int id = 1;
        while (iterator.hasNext()) {

            Integer currentVal = iterator.next();
            Integer frequency = styleWeight.get(currentVal);
            if (currentVal.equals(CalStyleEnum.ADD.getCalStyleCode())) {
                for (int i = 0; i < frequency; i++) {
                    Problem problem = new Problem();
                    String addArithmetic = generateAddArithmetic(problemInfoVo.getMaxNum(), problemInfoVo.getStepNum());
                    problem.setProblemBody(addArithmetic);
                    problem.setId(id);
                    resultList.add(problem);
                    id++;
                }
            } else if (currentVal.equals(CalStyleEnum.SUB.getCalStyleCode())) {
                for (int i = 0; i < frequency; i++) {
                    Problem problem = new Problem();
                    String subArithmetic = generateSubArithmetic(problemInfoVo.getMaxNum(), problemInfoVo.getStepNum());
                    problem.setProblemBody(subArithmetic);
                    problem.setId(id);
                    resultList.add(problem);
                    id++;
                }
            } else if (currentVal.equals(CalStyleEnum.MULT.getCalStyleCode())) {
                for (int i = 0; i < frequency; i++) {
                    Problem problem = new Problem();
                    String multArithmetic = generateMultArithmetic(problemInfoVo.getMaxNum(), problemInfoVo.getStepNum());
                    problem.setProblemBody(multArithmetic);
                    problem.setId(id);
                    resultList.add(problem);
                    id++;
                }
            } else if (currentVal.equals(CalStyleEnum.DIVI.getCalStyleCode())) {
                for (int i = 0; i < frequency; i++) {
                    Problem problem = new Problem();
                    String diviArithmetic = generateDiviArithmetic(problemInfoVo.getMaxNum(), problemInfoVo.getStepNum());
                    problem.setProblemBody(diviArithmetic);
                    problem.setId(id);
                    resultList.add(problem);
                    id++;
                }
            }
        }


        return resultList;
    }

    /**
     * 获取答案
     *
     * @param problems
     * @return
     */
    @Override
    public List<Answer> getAnswer(List<Problem> problems) {


        List<Answer> answers = problems.stream().map(problem -> {
            String problemBody = problem.getProblemBody();
            Answer answer = new Answer();
            String[] split = problemBody.split("=");
            String result = split[1].trim();
            answer.setAnswerBody(result);
            answer.setId(problem.getId());
            return answer;
        }).collect(Collectors.toList());

        return answers;

    }


    /**
     * 批改分数
     *
     * @param problemAnswerVo
     * @return
     */
    @Override
    public ProblemScoreInfoVo correctProblem(ProblemAnswerVo problemAnswerVo) {


        String answerJson = stringRedisTemplate.opsForValue().get(TokenServiceImpl.PROBLEM_MARK_ANSWER_PREFIX + problemAnswerVo.getToken());
        if (answerJson == null)
            //log.error("RepeatedSubmitAnswerExpection",new RepeatedSubmitAnswerExpection());
            throw new RepeatedSubmitAnswerExpection();

        List<Answer> answer = JSON.parseObject(answerJson, new TypeReference<List<Answer>>() {
        });

        List<Answer> submitAnswer = problemAnswerVo.getAnswers();

        ProblemScoreInfoVo scoreInfoVo = new ProblemScoreInfoVo();

        List<Answer> problemAnswers = new ArrayList<>(submitAnswer.size());

        Integer score = 0;


        for (int i = 0; i < submitAnswer.size(); i++) {
            if (submitAnswer.get(i).getAnswerBody().equals(String.valueOf(answer.get(i).getAnswerBody()))) {
                score += 3;
                Answer answer1 = new Answer();
                answer1.setAnswerBody("✔");
                answer1.setId(answer.get(i).getId());
                problemAnswers.add(answer1);
            } else {
                Answer answer1 = new Answer();
                answer1.setId(answer.get(i).getId());
                answer1.setAnswerBody(answer.get(i).getAnswerBody());
                problemAnswers.add(answer1);
            }
        }

        scoreInfoVo.setScore(score);
        scoreInfoVo.setProblemAnswers(problemAnswers);
        //String atomToken = UUID.randomUUID().toString().replace("-", "");
        //stringRedisTemplate.opsForValue().set(atomToken,atomToken,10,TimeUnit.MINUTES);
        final String atomToken = tokenService.generateAtomToken();
        scoreInfoVo.setAtomToken(atomToken);
        // 删除题目答案的缓存
        stringRedisTemplate.delete(TokenServiceImpl.PROBLEM_MARK_ANSWER_PREFIX + problemAnswerVo.getToken());
        return scoreInfoVo;
    }

    @Override
    public List<Problem> getProblemWithoutResult(List<Problem> problems) {

        List<Problem> problemBody = problems.stream().map(prob -> {
            Problem problem = new Problem();
            String[] split = prob.getProblemBody().split("=");
            problem.setProblemBody(split[0]);
            problem.setId(prob.getId());
            return problem;
        }).collect(Collectors.toList());

        return problemBody;
    }

    @Override
    public ProblemVo getProblemVo(List<Problem> arithmetics) {

        // 将答案分离开来并保存到redis中
        List<Answer> result = this.getAnswer(arithmetics);
        String json = JSON.toJSONString(result);

        final String answerToken = tokenService.generateMarkAnswerToken(json);

        //// 每一份题目生成一个UUID
        //String token = UUID.randomUUID().toString().replace("-", "");
        //// 10分钟后自动过期
        //stringRedisTemplate.opsForValue().set(token, json, 10, TimeUnit.MINUTES);
        // 纯题目
        List<Problem> problemBody = this.getProblemWithoutResult(arithmetics);
        // 生成原子性token
        //String atomToken = UUID.randomUUID().toString().replace("-", "");

        return new ProblemVo(answerToken, problemBody);
    }


    private Map<Integer, Integer> calculateStyleWeight(List<Integer> calStyle, Integer problemNum) {

        HashMap<Integer, Integer> weightMap = new HashMap<>(calStyle.size());

        Integer avgWeight = problemNum / calStyle.size();

        // 整除，不做特殊处理
        if (avgWeight * calStyle.size() == Integer.valueOf(problemNum)) {

            for (int i = 0; i < calStyle.size(); i++) {
                weightMap.put(calStyle.get(i), avgWeight);
            }

            return weightMap;
        }

        for (int i = 0; i < calStyle.size() - 1; i++)
            weightMap.put(calStyle.get(i), avgWeight);

        // 将多出来的计算式给最后一种运算类型
        int lastWeight = problemNum - avgWeight * (calStyle.size() - 1);

        weightMap.put(calStyle.get(calStyle.size() - 1), lastWeight);

        return weightMap;
    }

    // 计算加法算式
    private String generateAddArithmetic(Integer maxNum, Integer stepNum) {
        List<Integer> numbers = new ArrayList<>(stepNum);
        Random random = new Random();

        for (int i = 0; i < stepNum; i++) {
            numbers.add(random.nextInt(maxNum));
        }

        // 保存计算的结果
        Integer sum = Integer.valueOf(0);
        // 保存计算式
        StringBuffer arithmeticString = new StringBuffer("");


        // 保证必须产生出正确的结果
        while (true) {
            sum = numbers.stream().reduce(0, (subtotal, element) -> subtotal + element);
            if (sum <= maxNum) {
                Iterator<Integer> iterator = numbers.iterator();
                while (iterator.hasNext()) {
                    arithmeticString.append(iterator.next());
                    arithmeticString.append(" + ");
                }
                arithmeticString.replace(arithmeticString.lastIndexOf("+"), arithmeticString.lastIndexOf("+") + 1, "=");
                arithmeticString.append(sum);
                return String.valueOf(arithmeticString);
            }
            // 第一次生成的元素不满足要求，因此必须从新生成随机数
            numbers.clear();
            for (int i = 0; i < stepNum; i++) {
                numbers.add(random.nextInt(maxNum));
            }

        }


    }

    // 计算减法算式
    // todo 减法这里必须做特殊处理，让被减数很大，而减数相对较小，否则会出现全为 0 的 情况
    private String generateSubArithmetic(Integer maxNum, Integer stepNum) {
        List<Integer> numbers = new ArrayList<>(stepNum);
        Random random = new Random();
        numbers.add(random.nextInt(maxNum));
        for (int i = 1; i < stepNum; i++) {
            numbers.add(random.nextInt(maxNum / stepNum));
        }

        // 保存计算的结果
        Integer sum = numbers.get(0);
        // 保存计算式
        StringBuffer arithmeticString = new StringBuffer("");


        while (true) {
            // todo stream.reduce()如何做减法
            for (int i = 1; i < numbers.size(); i++) {
                sum -= numbers.get(i);
            }

//            sum = numbers.stream().reduce(numbers.get(0), (subtotal, element) -> subtotal - element);
            if (sum <= maxNum && sum >= 0) {
                Iterator<Integer> iterator = numbers.iterator();
                while (iterator.hasNext()) {
                    arithmeticString.append(iterator.next());
                    arithmeticString.append(" - ");
                }
                arithmeticString.replace(arithmeticString.lastIndexOf("-"), arithmeticString.lastIndexOf("-") + 1, "=");
                arithmeticString.append(sum);
                return String.valueOf(arithmeticString);
            }
            numbers.clear();
            numbers.add(random.nextInt(maxNum));
            for (int i = 1; i < stepNum; i++) {
                numbers.add(random.nextInt(maxNum / stepNum));
            }
            // sum 必须获得 第一个值(最大值)
            sum = numbers.get(0);
        }

    }

    // 计算乘法算式
    // todo 乘法必须做特殊处理，乘法更容易越界，导致长时间循环
    private String generateMultArithmetic(Integer maxNum, Integer stepNum) {

        List<Integer> numbers = new ArrayList<>(stepNum);
        Random random = new Random();
        for (int i = 0; i < stepNum; i++) {
            // 出现 0 的情况会导致很多算式结果为 0 ，这里让其最小为 1
            numbers.add(random.nextInt(maxNum / stepNum) + 1);
        }

        // 保存计算的结果
        Integer sum = Integer.valueOf(0);
        // 保存计算式
        StringBuffer arithmeticString = new StringBuffer("");


        while (true) {
            sum = numbers.stream().reduce(1, (subtotal, element) -> subtotal * element);
            // 乘法很容易超出限定
            if (sum <= maxNum) {
                Iterator<Integer> iterator = numbers.iterator();
                while (iterator.hasNext()) {
                    arithmeticString.append(iterator.next());
                    arithmeticString.append(" * ");
                }
                arithmeticString.replace(arithmeticString.lastIndexOf("*"), arithmeticString.lastIndexOf("*") + 1, "=");
                arithmeticString.append(sum);
                return String.valueOf(arithmeticString);
            }
            numbers.clear();
            for (int i = 0; i < stepNum; i++) {
                numbers.add(random.nextInt(maxNum / stepNum) + 1);
            }
        }

    }

    // 计算除法算式
    // todo 乘法要避免 0 的情况，此外 被除数要 大于 除数，避免 结果为 0
    private String generateDiviArithmetic(Integer maxNum, Integer stepNum) {

        List<Integer> numbers = new ArrayList<>(stepNum);
        Random random = new Random();
        numbers.add(random.nextInt(maxNum));
        for (int i = 1; i < stepNum; i++) {
            numbers.add(random.nextInt(maxNum / (stepNum * 4)) + 1);
        }

        // 保存计算的结果
        Integer sum = numbers.get(0);
        // 保存计算式
        StringBuffer arithmeticString = new StringBuffer("");


        while (true) {
            // 防止 出现 a / 0 的情况导致程序出问题
            // todo stream().reduce() 如何处理 除法
//                sum = numbers.stream().reduce(0, (subtotal, element) -> subtotal / element);
            for (int i = 1; i < numbers.size(); i++) {
                sum /= numbers.get(i);
            }
            if (sum <= maxNum) {
                Iterator<Integer> iterator = numbers.iterator();
                while (iterator.hasNext()) {
                    arithmeticString.append(iterator.next());
                    arithmeticString.append(" ÷ ");
                }
                arithmeticString.replace(arithmeticString.lastIndexOf("÷"), arithmeticString.lastIndexOf("÷") + 1, "=");
                arithmeticString.append(sum);
                return String.valueOf(arithmeticString);
            }

            numbers.clear();
            numbers.add(random.nextInt(maxNum));
            for (int i = 1; i < stepNum; i++) {
                numbers.add(random.nextInt(maxNum / (stepNum * 4)) + 1);
            }
            sum = numbers.get(0);

        }
    }


    /**
     * 判断运算符
     *
     * @param calStyle
     * @return
     */
    @Deprecated
    private String getOperatorFromCalStyle(Integer calStyle) {


        switch (calStyle.intValue()) {
            case 1:
                return "+";
            case 2:
                return "-";
            case 3:
                return "*";
            case 4:
                return "/";
            default:
                return "";
        }

    }
}


