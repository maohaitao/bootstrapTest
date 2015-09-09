package com.hivier.business;

import com.google.gson.JsonObject;

/**
 * @author hesin
 * @Created with： com.hivier.business
 * @Des: 首页下发数据
 * @date 2015/9/1
 */
public interface RemdInfoBusiness {

    public JsonObject remdInfo(String typeid, String mark, Integer must, Integer pageid);

    public boolean initRemdInfo();// 初始化数据
}
