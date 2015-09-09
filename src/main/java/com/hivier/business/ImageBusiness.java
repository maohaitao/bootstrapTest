package com.hivier.business;

import com.google.gson.JsonObject;

/**
 * @author hesin
 * @Created with： com.hivier.business
 * @Des: 壁纸图片业务处理
 * @date 2015/9/1
 */
public interface ImageBusiness {
    /**
     * 获取壁纸详情
     * @param id
     * @param diary  是否获取单天最新 0 否 1 获取
     * @return
     */
    public JsonObject getImageInfo(Integer id, Integer diary);

    public boolean initCache();
}
