package com.hivier.business.impl;

import com.hivier.business.RemdInfoBusiness;
import com.hivier.cache.JVMCacheService;
import com.hivier.model.HivierProductInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author hesin
 * @Created with： com.hivier.business.impl
 * @Des: TODO
 * @date 2015/9/9
 */
@Service
public class RemdInfoBusinessImpl implements RemdInfoBusiness {
    @Autowired
    private JVMCacheService jvmCacheService;

    @Override
    public List<HivierProductInfo> remdInfo() {
        return jvmCacheService.getProductInfoList();
    }
}
