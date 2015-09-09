package com.hivier.service.impl;

import com.hivier.cache.RedisCacheService;
import com.hivier.cache.RedisService;
import com.hivier.model.HivierProductInfo;
import com.sf.common.exception.AppException;
import com.sf.common.log.LogService;
import com.sf.common.service.BaseService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author hesin
 * @Created with： com.hivier.service.impl
 * @Des: 图片资源下发
 * @date 2015/9/1
 */
@Service
public class HivierProductInfoServiceImpl extends BaseService {

//    public static int expri = 1 * 24 * 60 * 60; // 默认有效期 1天
//    public static String IMAGE_KEY = "image_";//图片的redis key

    public HivierProductInfo getImageById(Integer imageid) {
        if (imageid == null) {
            return null;
        }
        List<Object> params = new ArrayList<>();
        params.add(imageid);
        params.add(3);
        String sql = "select * from hivier_product_info i where i.id=? and i.i_status=?";
        HivierProductInfo imageInfo = findBySQL(sql, HivierProductInfo.class, params);
//        if (imageInfo!=null){
//            RedisService.set(IMAGE_KEY + RedisCacheService.modekey + imageid, imageInfo, expri);
//        }
        return imageInfo;
    }

    public List<HivierProductInfo> queryAllProduct() {
        List<Object> params = new ArrayList<>();
        params.add(3);
        String sql = "select * from hivier_product_info i where i.i_status=? ";
        List<HivierProductInfo> imageInfos = null;
        try {
            imageInfos = queryBySQL(sql, HivierProductInfo.class, params);
        } catch (AppException e) {
            LogService.error("queryAllProduct,查询所有的商品失败", e);
        }
        return imageInfos;
    }

}
