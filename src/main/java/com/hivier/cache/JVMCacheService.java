package com.hivier.cache;

import com.hivier.model.HivierProductInfo;
import com.hivier.service.impl.HivierProductInfoServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author hesin
 * @Created with： com.hivier.cache
 * @Des: JVM缓存处理
 * @date 2015/9/9
 */
@Service
public class JVMCacheService {
//    private static ConcurrentMap<String, HivierProductInfo> productInfoMap = new ConcurrentHashMap<>();//商品信息

    private static List<HivierProductInfo> productInfoList = new ArrayList<>();//商品信息

    @Autowired
    private HivierProductInfoServiceImpl service ;
    public boolean putProductInfoList(){
        productInfoList = service.queryAllProduct();
        return true;
    }

    public List<HivierProductInfo> getProductInfoList(){
        return productInfoList;
    }

}
