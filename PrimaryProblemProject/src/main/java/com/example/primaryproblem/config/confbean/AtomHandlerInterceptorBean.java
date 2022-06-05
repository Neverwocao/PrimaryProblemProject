package com.example.primaryproblem.config.confbean;

import java.util.ArrayList;
import java.util.List;

/**
 * @author pzw
 * @data 2022/5/22
 * @apiNote 拦截有幂等性要求的请求,亦可精确配置
 */
public class AtomHandlerInterceptorBean {

    public static List<String> interceptPath = new ArrayList<>();

    static {
        interceptPath.add("/**");
    }

}
