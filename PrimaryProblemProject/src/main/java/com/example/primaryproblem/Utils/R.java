package com.example.primaryproblem.Utils;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;

import java.util.HashMap;
import java.util.Map;

public class R extends HashMap<String, Object> {

    private static final long serialVersionUID = 1L;

    /**
     * @param key 获取指定key的名字
     */
    public <T> T getData(String key, TypeReference<T> typeReference) {
        Object data = get(key);
        return JSON.parseObject(JSON.toJSONString(data), typeReference);
    }

    // 利用 fastjson 进行逆转
    public <T> T getData(TypeReference<T> tTypeReference) {
        Object data = get("data"); // 默认是map
        String s = JSON.toJSONString(data);
        T t = JSON.parseObject(s, tTypeReference);
        return t;
    }

    public R setData(Object data) {
        put("data", data);
        return this;

    }

    public R() {
        put("code", 200);
        put("msg", "success");
    }


    public static R error(int code, String msg) {
        R r = new R();
        r.put("code", code);
        r.put("msg", msg);
        return r;
    }

    public static R error() {
        R r = new R();
        r.put("code", 400);
        r.put("msg", "failure");
        return r;
    }

    public static R ok(String msg) {
        R r = new R();
        r.put("msg", msg);
        return r;
    }

    public static R ok(Map<String, Object> map) {
        R r = new R();
        r.putAll(map);
        return r;
    }

    public static R ok() {
        return new R();
    }


    public R put(String key, Object value) {
        super.put(key, value);
        return this;
    }

    public Integer getCode() {
        return (Integer) this.get("code");
    }
}

