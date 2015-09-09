package com.hivier.cache;

import com.sf.common.log.LogService;
import com.sf.common.util.JsonUtil;
import com.sf.redis.JedisCache;

/**
 * @author hesin
 * @Created with： com.pc.buyer.cache
 * @Des: redis处理
 * @date 2015/8/25
 */
public class RedisService {
    private static int expri = 1 * 24 * 60 * 60; // 默认有效期 1天

    public static JedisCache getJedisCache() {
        JedisCache cache = null;
        try {
            cache = new JedisCache();
        } catch (Exception e) {
            LogService.error("getJedisCache  error:", e);
        }
        return cache;
    }

    public static boolean set(String key, Object o, int expr) {
        if (expr <= 0) {
            expr = expri;
        }
        boolean flag = getJedisCache().setex(key, expr, JsonUtil.toJsonTree(o).toString());
        return flag;
    }

    public static boolean setString(String key, String o, int expr) {
        if (expr <= 0) {
            expr = expri;
        }
        boolean flag = getJedisCache().setex(key, expr, o);
        return flag;
    }

    public static Object get(String key, Class type) {
        Object resultobj = null;
        String re = getJedisCache().get(key);
        if (re != null) {
            if (type == null) {
                resultobj = JsonUtil.parseEle(re);
            } else {
                resultobj = JsonUtil.parse(re, type);
            }
        }
        return resultobj;
    }


    public static boolean hset(String key, String field, Object o) {
        long re = getJedisCache().hset(key, field, JsonUtil.toJsonTree(o).getAsString());
        return re > 0 ? true : false;
    }


    public static void main(String[] args) {


        System.out.println("end!");
    }


}
