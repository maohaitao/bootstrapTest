package com.hivier.controller;

import com.hivier.business.RemdInfoBusiness;
import com.google.gson.JsonObject;
import com.sf.common.log.LogService;
import com.sf.common.model.CommonRequest;
import com.sf.common.model.DynamicResult;
import com.sf.common.util.JsonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author hesin
 * @Created with： com.pc.buyer.controller
 * @Des: 代购首页接口
 * @date 2015/8/19
 */
@Controller
public class RemdInfoController extends BaseController {

    @Autowired
    private RemdInfoBusiness remdInfoBusiness;

    @Override
    public DynamicResult handleBusiness(CommonRequest r) throws Exception {
        return remdInfoDeal(r);
    }

    public DynamicResult remdInfoDeal(CommonRequest r) {
        DynamicResult result = new DynamicResult();
        String typeid = JsonUtil.getValue(r.getJsonData(), "typeid");
        String mark = JsonUtil.getValue(r.getJsonData(), "mark");
        Integer must = JsonUtil.getValue(r.getJsonData(), "must", 0);
        Integer pageid = JsonUtil.getValue(r.getJsonData(), "pageid", 0);
        long servertime = System.currentTimeMillis();

        JsonObject types = remdInfoBusiness.remdInfo(typeid, mark, must,pageid);
        result.put("types", types);
        result.put("servertime", servertime); //活动信息
        return result;
    }

    @RequestMapping("/remdinfo.shtml")
    public void remdInfo(HttpServletRequest request, HttpServletResponse response) {
        try {
            dealService(request, response);
        } catch (IOException e) {
            LogService.error("remdInfo：", e);
        }
    }
}