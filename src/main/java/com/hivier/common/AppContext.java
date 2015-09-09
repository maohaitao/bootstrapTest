package com.hivier.common;


import com.sf.common.T;

/**
 * @author maohaitao
 * @ClassName: AppContext
 * @Des: 配置上下文
 */
public final class AppContext {
    // contronler返回数据类型
    public static final String PRODUCES = "application/json;charset=UTF-8";

    private AppContext() {

    }

    public static boolean IS_STARTED = true;

    // 系统短IP，如 1.32
    public static final String SYS_SHORT_IP = T.getShortLocalIpAddress();
}
