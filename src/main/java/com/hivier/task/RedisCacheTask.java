package com.hivier.task;

import com.hivier.business.RemdInfoBusiness;
import com.hivier.cache.JVMCacheService;
import com.hivier.cache.RedisCacheService;
import com.sf.common.log.LogService;
import com.sf.common.util.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * @author hesin
 * @Created with：
 * @Des: redis缓存定时任务
 * @date 2015/8/21
 */

@Component
public class RedisCacheTask {
    private Date time5m = null;
    private Date time1h = null;
    private RedisCacheService redisCacheService = new RedisCacheService();

    @Autowired
    private RemdInfoBusiness remdInfoBusiness;
    @Autowired
    private JVMCacheService jvmCacheService;

    /**
     * * @return 格式: [秒] [分] [小时] [日] [月] [周] [年]
     * 序号 说明 是否必填 允许填写的值 允许的通配符
     * 1   秒    是      0-59 ,         - * /
     * 2    分    是      0-59 ,        - * /
     * 3    小时  是      0-23 ,       - * /
     * 4    日    是      1-31 ,      - * ? / L W
     * 5    月    是    1-12 or JAN-DEC , - * /
     * 6    周     是     1-7 or SUN-SAT , - * ? / L #
     * 7    年     否     empty 或 1970-2099 , - * /
     */
    @Scheduled(fixedDelay = 1000 * 60 * 10)   //每10分钟执行一次全量
    public void initCache() {
//        boolean initRemdInfo = remdInfoBusiness.initRemdInfo();
//        LogService.info("初始化remdinfo数据：initRemdInfo=" + initRemdInfo + "|");
        boolean initproInfo = jvmCacheService.putProductInfoList();
        LogService.info("初始化product数据：initproInfo=" + initproInfo + "|");
        time1h = DateUtil.getCurrentDate();
    }

    @Scheduled(fixedDelay = 1000 * 60 * 5)   //每5分钟执行一次增量
    public void updateCache() {

    }

}
