package com.hivier.controller;

import com.hivier.business.RemdInfoBusiness;
import com.google.gson.JsonObject;
import com.hivier.model.HivierProductInfo;
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
import java.util.List;

/**
 * @author hesin
 * @Created with： com.pc.buyer.controller
 * @Des: 代购首页接口
 * @date 2015/8/19
 */
@Controller
public class RemdInfoController {
    @Autowired
    private RemdInfoBusiness remdInfoBusiness;
    @RequestMapping(value = "/remdinfo.shtml")
    public String handleBusiness(HttpServletRequest request, HttpServletResponse response) throws Exception {
        List<HivierProductInfo> types = remdInfoBusiness.remdInfo();
        request.setAttribute("list",types);
        return "index";
    }
}