package com.hivier.controller;

import com.hivier.business.ImageBusiness;
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
 * @Des: 壁纸图片详情/每日更新
 * @date 2015/8/18
 */
@Controller
@RequestMapping("/imageinfo_query.shtml")
public class ImageInfoController extends BaseController {

    @Autowired
    private ImageBusiness imageBusiness;

    @Override
    public DynamicResult handleBusiness(CommonRequest r) throws Exception {
        DynamicResult result = new DynamicResult();
        JsonObject dataJo = r.getJsonData();
        Integer id = JsonUtil.getValue(dataJo, "id", 0);
        int diary = JsonUtil.getValue(dataJo, "hivier", 0); // hivier  是否获取单天最新 0 否 1 获取

        JsonObject jsonObject = imageBusiness.getImageInfo(id,diary);
        if (jsonObject!=null){
            result.put(jsonObject);
        }
        return result;
    }

    @RequestMapping
    public void imageInfoQuery(HttpServletRequest request, HttpServletResponse response) {
        try {
            dealService(request, response);
        } catch (IOException e) {
            LogService.error("imageInfoQuery,查询详情失败：", e);
        }
    }

}
