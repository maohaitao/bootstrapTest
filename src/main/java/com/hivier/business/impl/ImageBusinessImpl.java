package com.hivier.business.impl;

import com.hivier.business.ImageBusiness;
import com.hivier.cache.RedisCacheService;
import com.hivier.cache.RedisService;
import com.hivier.model.HivierProductInfo;
import com.hivier.service.impl.HivierProductInfoServiceImpl;
import com.hivier.service.impl.DiarySrcImageServiceImpl;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.sf.common.util.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * @author hesin
 * @Created with： com.hivier.business.impl
 * @Des: 壁纸详情处理
 * @date 2015/9/1
 */
@Service
public class ImageBusinessImpl implements ImageBusiness {
    @Autowired
    private HivierProductInfoServiceImpl imageInfoService;

    @Autowired
    private DiarySrcImageServiceImpl srcImageService;

    Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
    String diarys = DateUtil.formatDateToStringShort(new Date());

    @Override
    public JsonObject getImageInfo(Integer id, Integer diary) {
        JsonObject jsonObject = null;
        HivierProductInfo imageInfo = null;
        if (diary != null && diary == 1) { // 下发每日更新数据
            jsonObject = srcImageService.getRedisByKey(diarys, null);
            if (jsonObject == null || jsonObject.isJsonNull()) {
                imageInfo = srcImageService.getImageByDiary(diarys);
                if (imageInfo != null) {
                    jsonObject = gson.toJsonTree(imageInfo).getAsJsonObject();
                }
            }
        } else {
            jsonObject = srcImageService.getRedisByKey(id+"", null);
            if (jsonObject == null || jsonObject.isJsonNull()) {
                imageInfo = id != null ? imageInfoService.getImageById(id) : null;
                if (imageInfo != null) {
                    jsonObject = gson.toJsonTree(imageInfo).getAsJsonObject();
                }
            }
        }
        return jsonObject;
    }

    @Override
    public boolean initCache() {
        List<HivierProductInfo> list = imageInfoService.queryAllImage();
        if (list!=null && list.size()>0){
            for (HivierProductInfo imageInfo:list){
                if (imageInfo!=null){
                    RedisService.set(HivierProductInfoServiceImpl.IMAGE_KEY + RedisCacheService.modekey + imageInfo.getId(), imageInfo, HivierProductInfoServiceImpl.expri);
                }
            }
        }
        return true;
    }
}
