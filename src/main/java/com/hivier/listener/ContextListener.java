package com.hivier.listener;

import com.sf.common.AppContext;
import com.sf.common.log.RsyncLog;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

/**
 * @author hesin
 * @Created with： com.sf.mall.listener
 * @Des: 监听
 * @date 2015/8/13
 */
@WebListener
public class ContextListener implements ServletContextListener {

    public void contextDestroyed(ServletContextEvent sce) {
        AppContext.IS_STARTED = false;
//        HttpClientUtil.shutdown();// 销毁httpclient的链接池
    }

    public void contextInitialized(ServletContextEvent se) {
        AppContext.IS_STARTED = true;
        RsyncLog.info("webapp server is started!");
    }
}
