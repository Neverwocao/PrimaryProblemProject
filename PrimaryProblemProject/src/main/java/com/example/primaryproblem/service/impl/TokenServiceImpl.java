package com.example.primaryproblem.service.impl;

import com.example.primaryproblem.service.TokenService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * @author pzw
 * @data 2022/5/22
 * @apiNote
 */
@Slf4j
@Service
public class TokenServiceImpl implements TokenService {

    public static final String USER_TEMP_TOKEN_PREFIX = "user:temp:token:";

    public static final String PROBLEM_MARK_ID_PREFIX = "problem:mark:id:";

    public static final String PROBLEM_MARK_ANSWER_PREFIX = "problem:mark:answer:";

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Override
    public String generateAtomToken() {

        String atomToken = UUID.randomUUID().toString().replace("-", "");

        //String atomTokenKey = CommonService.ATOM_TOKEN_PREFIX + atomToken;
        // 由于我们后来只需要判断该值是否存在即可，并不需要查它的value，因此value随意
        redisTemplate.opsForValue().setIfAbsent(PROBLEM_MARK_ID_PREFIX + atomToken, atomToken, 10, TimeUnit.MINUTES);

        return atomToken;
    }

    @Override
    public boolean checkAtomToken(HttpServletRequest request) {

        String atomToken = request.getHeader("atomToken");

        if (atomToken == null)
            return false;

        // 通过使用lua脚本进行原子性删除
        String script = "if redis.call('get',KEYS[1]) == ARGV[1] then return redis.call('del',KEYS[1]) else return 0 end";
        //删除锁
        Long lock1 = redisTemplate.execute(new DefaultRedisScript<Long>(script, Long.class),
                Arrays.asList(PROBLEM_MARK_ID_PREFIX + atomToken), atomToken);

        if (lock1 == 0L)
            return false;
        else {
            return true;
        }
    }

    @Override
    public String generateTempUserToken(HttpServletRequest request) {
        // todo 本机访问时，会出现ip为：0:0:0:0:0:0:0:1 的情况.
        String remoteAddr = request.getRemoteAddr();

        // 本机
        if (remoteAddr.equals("0:0:0:0:0:0:0:1")) {
            remoteAddr = USER_TEMP_TOKEN_PREFIX + "127.0.0.1";

            return getTempUserTokenByRemoteAddr(remoteAddr);

        }else {
            // 非本机
            remoteAddr = USER_TEMP_TOKEN_PREFIX + remoteAddr;

            return getTempUserTokenByRemoteAddr(remoteAddr);
        }
    }

    private String getTempUserTokenByRemoteAddr(String remoteAddr) {

        // 查到了直接返回
        String tokenRedis = redisTemplate.opsForValue().get(remoteAddr);

        if (tokenRedis != null)
            return tokenRedis;

        // 没查到创建
        String token = UUID.randomUUID().toString().replace("-", "");
        redisTemplate.opsForValue().set(remoteAddr, token, 15, TimeUnit.MINUTES);

        return token;
    }

    public String generateMarkAnswerToken(String answerJson){

        String atomToken = UUID.randomUUID().toString().replace("-", "");

        //String atomTokenKey = CommonService.ATOM_TOKEN_PREFIX + atomToken;
        // 由于我们后来只需要判断该值是否存在即可，并不需要查它的value，因此value随意
        redisTemplate.opsForValue().setIfAbsent(PROBLEM_MARK_ANSWER_PREFIX + atomToken, answerJson, 10, TimeUnit.MINUTES);

        return atomToken;
    }

}
