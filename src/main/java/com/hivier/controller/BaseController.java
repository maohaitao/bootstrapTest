package com.hivier.controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.sf.common.AppContext;
import com.sf.common.T;
import com.sf.common.constant.ResultErrorCode;
import com.sf.common.exception.AppException;
import com.sf.common.log.FlumeService;
import com.sf.common.log.RsyncLog;
import com.sf.common.model.CommonRequest;
import com.sf.common.model.DynamicResult;
import com.sf.common.model.FlumeFailRequest;
import com.sf.common.util.CommonUtil;
import com.sf.common.util.JsonUtil;
import com.sf.common.util.ServerUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;

/**
 * @author hesin
 * @Created with： com.sf.mall.controller
 * @Des: 基类
 * @date 2015/8/13
 */

public abstract class BaseController {
    protected static final FlumeService flumeService = new FlumeService();
    protected static final Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
    public void dealService(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String recvlog = "";
        String responsedata = "";
        String shandle = "";
        CommonRequest commonRequest = null;
        try {
            // 设置编码 request.setCharacterEncoding("UTF-8");    // 日志信息
            recvlog = request.getServletPath() + ":request:" + ServerUtil.getRequestParameterString(request) + ", ip=" + T.getClientIpAddr(request) + ";";
            RsyncLog.debug("begin " + recvlog);
            // 返回结果是否进行处理
            shandle = request.getParameter("shandle");
            if (CommonUtil.isNull(shandle)) {
                shandle = "0";// 默认不压缩处理
            }
            if (!AppContext.IS_STARTED) {
                throw new AppException(ResultErrorCode.STATUS_CODE_SERVER_ERROR.getCode(), "Server is starting");
            }
            // 下发数据流 防止数据被网关缓存
            response.setContentType("application/octet-stream; charset=UTF-8");

            // 是否需要对数据进行处理
            String handle = request.getParameter("handle");
            String data = request.getParameter("data"); //可进行访问权限控制

            // 解压处理
            if ("1".equals(handle) && !T.isEmpty(data) && !data.startsWith("{")) {
                data = new String(CommonUtil.ungzip(data.getBytes("ISO-8859-1")), "UTF-8");
            } else if (T.isEmpty(data)) {
                data = "{}";
                RsyncLog.debug(recvlog + ">>>>> data is empty!");
            }

            JsonObject jsonData = JsonUtil.parse(data);

			/* 解析requestdata为request对象 */
            commonRequest = new CommonRequest(request, jsonData);

            DynamicResult customResponse = null;
            // 进行业务分发处理
            customResponse = handleBusiness(commonRequest);

            // ===================返回值处理=========================================
            if (customResponse == null) {
                customResponse = getErrorResult("-1", "response=null");
            } else {
                String servertime = customResponse.popValue("servertime");
                Integer hasnew = customResponse.popInt("hasnew");
                String mark = customResponse.popValue("mark");
                customResponse.put("result", getSeccessResult(servertime, hasnew, mark));
            }
            responsedata = customResponse.toString();
            if (!T.isBlank(responsedata) && responsedata.indexOf("error_type") > 0) {
                RsyncLog.info("biz error:   " + recvlog + "==>response:" + responsedata);
                flumeService.flumeLog(new FlumeFailRequest(commonRequest));
            }
            /* 日志记录返回的信息 */
            RsyncLog.debug("end   " + recvlog + "==>response:" + responsedata);
        } catch (Exception e) {
            RsyncLog.error("RuntimeException recvlog：" + recvlog, e);
            DynamicResult errorResult = getErrorResult(ResultErrorCode.STATUS_CODE_SERVER_ERROR.getCode(), e.getMessage());
            responsedata = errorResult.toString();
            flumeService.flumeLog(new FlumeFailRequest(commonRequest, request));
        }

		/* 下发返回值 */
        OutputStream out = response.getOutputStream();
        try {
            byte[] bytes = null;
            if ("1".equals(shandle)) {// 压缩
                bytes = CommonUtil.gzip(responsedata.getBytes("UTF-8"));
            } else {
                bytes = responsedata.getBytes("UTF-8");
            }
            out.write(bytes);
            out.flush();
        } catch (Exception e) {
            RsyncLog.error("下发返回值出错", e);
            flumeService.flumeLog(new FlumeFailRequest(commonRequest, request));
        } finally {
            out.close();
        }
    }

    protected DynamicResult getErrorResult(String errorType, String errorInfo) {
        DynamicResult result = new DynamicResult();
        JsonObject res = new JsonObject();
        res.addProperty("status", errorType);
        res.addProperty("errorcode", errorType);
        res.addProperty("msg", errorInfo);
        result.put("result", res);
        return result;
    }

    /**
     * 获取错误返回信息
     *
     * @param errorType
     * @param errorInfo
     * @return
     */
    protected DynamicResult getErrorResult(Integer errorType, String errorInfo) {
        DynamicResult result = new DynamicResult();
        JsonObject res = new JsonObject();
        res.addProperty("status", errorType);
        res.addProperty("errorcode", errorType);
        res.addProperty("msg", errorInfo);
        result.put("result", res);
        return result;
    }

    protected JsonObject getSeccessResult(String servicetime, Integer hasnew, String mark) {
        JsonObject res = new JsonObject();
        res.addProperty("status", ResultErrorCode.STATUS_CODE_SUCCESS.getCode());
        res.addProperty("errorcode", 0);
        if (!CommonUtil.isNull(servicetime)) {
            res.addProperty("servicetime", servicetime);
        }

        if (!CommonUtil.isEmpty(hasnew)) {
            res.addProperty("hasnew", hasnew);
        }

        if (!CommonUtil.isNull(mark)) {
            res.addProperty("mark", mark);
        }
        return res;
    }

    public abstract DynamicResult handleBusiness(CommonRequest r) throws Exception;
}

