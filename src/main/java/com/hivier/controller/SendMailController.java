package com.hivier.controller;

import com.common.util.AppContext;
import com.hivier.model.HivierEmailInfo;
import com.hivier.service.impl.HivierEmailInfoServiceImpl;
import com.mail.executor.Executor;
import com.mail.executor.MailRunnable;
import com.sf.common.exception.AppException;
import com.sf.common.log.LogService;
import com.sf.common.util.DateUtil;
import org.apache.tomcat.util.http.fileupload.FileItem;
import org.apache.tomcat.util.http.fileupload.disk.DiskFileItemFactory;
import org.apache.tomcat.util.http.fileupload.servlet.ServletFileUpload;
import org.apache.tomcat.util.http.fileupload.servlet.ServletRequestContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.mail.internet.InternetAddress;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * @author hesin
 * @Created with： com.hivier.controller
 * @Des: 发送邮件处理
 * @date 2015/9/10
 */
@Controller
public class SendMailController {

    @Autowired
    private HivierEmailInfoServiceImpl emailInfoService;

    @RequestMapping(value = "/sendMail.shtml")
    public
    @ResponseBody
    Map<String, Object> sendMail(HttpServletRequest req, HttpServletResponse resp) throws IOException {
//        PrintWriter out = resp.getWriter();
        Map<String, Object> result = new HashMap<>();
        String name = req.getParameter("name");
        String email = req.getParameter("email");
        String phone = req.getParameter("phone");
        String content = req.getParameter("message");
        String bcc = req.getParameter("bcc");
//        String to = req.getParameter("to");
        String cc = "hesinmao@163.com";//req.getParameter("cc");
        String host = req.getParameter("host");
        String from = req.getParameter("from");
        String pwd = req.getParameter("pwd");

        HivierEmailInfo emailInfo = new HivierEmailInfo();
        emailInfo.seteName(name);
        emailInfo.setEmail(email);
        emailInfo.setePhone(phone);
        emailInfo.setCreateTime(DateUtil.getCurrentDate());

        try {
            emailInfoService.saveEntity(emailInfo);
        } catch (AppException e) {
            e.printStackTrace();
        }

        String title = email + "#" + name + "#" + phone;
        Object bytes = null;
        String filename = null;
        HashMap attaches = null;
        try {
            if (ServletFileUpload.isMultipartContent(req)) {
                attaches = new HashMap();
                DiskFileItemFactory e = new DiskFileItemFactory();
                e.setSizeThreshold(4096);
                e.setRepository(new File(System.getProperty("java.io.tmpdir")));
                ServletFileUpload upload = new ServletFileUpload(e);
                upload.setSizeMax(125829120L);
                List fileItems = upload.parseRequest(new ServletRequestContext(req));
                Iterator i = fileItems.iterator();

                while (i.hasNext()) {
                    FileItem fi = (FileItem) i.next();
                    if (fi.isFormField()) {
                        String field = fi.getFieldName();
                        String val = fi.getString("utf-8");
                        if (field.equalsIgnoreCase("to")) {
//                            to = val;
                        } else if (fi.getFieldName().equalsIgnoreCase("title")) {
//                            title = val;
                        } else if (fi.getFieldName().equalsIgnoreCase("content")) {
                            content = val;
                        } else if (fi.getFieldName().equalsIgnoreCase("cc")) {
                            cc = val;
                        } else if (fi.getFieldName().equalsIgnoreCase("bcc")) {
                            bcc = val;
                        } else if (fi.getFieldName().equalsIgnoreCase("fromname")) {
//                            fromname = val;
                        } else if (fi.getFieldName().equalsIgnoreCase("host")) {
                            host = val;
                        } else if (fi.getFieldName().equalsIgnoreCase("from")) {
                            from = val;
                        } else if (fi.getFieldName().equalsIgnoreCase("pwd")) {
                            pwd = val;
                        }
                    } else {
                        byte[] bytes1 = fi.get();
                        filename = fi.getName();
                        attaches.put(filename, bytes1);
                    }
                }
            }
        } catch (Exception var23) {
            LogService.error(req.getRemoteAddr() + " send email failed!", var23);
        }

        if (host != null && from != null && pwd != null) {
            LogService.info("host=" + host + ",from=" + from + ",pwd=" + pwd);
        } else {
            host = AppContext.HOST;
            from = AppContext.FROM;
            pwd = AppContext.PWD;
        }

        if (from != null && !"".equals(from) && content != null && !"".equals(content.trim())) {
            Executor.executorService.execute(new MailRunnable(host, new InternetAddress(from, title, "utf-8"), pwd, AppContext.CONTENTTYPE, title, content, attaches, processMailInfo(from), processMailInfo(cc), processMailInfo(bcc), req.getRemoteAddr()));
            result.put("1", "succeed");
        } else {
            result.put("1", "fail");
        }
        return result;
    }

    public static Set<InternetAddress> processMailInfo(String mailinfo) {
        if (mailinfo == null) {
            return null;
        } else {
            HashSet set = new HashSet();
            InternetAddress addr = null;
            String[] mail_infos = mailinfo.split(",");
            String[] var7 = mail_infos;
            int var6 = mail_infos.length;

            for (int var5 = 0; var5 < var6; ++var5) {
                String mail_info = var7[var5];
                if (mail_info != null && mail_info.trim().length() >= 4) {
                    String[] tmp = mail_info.split(":");

                    try {
                        if (tmp.length > 1) {
                            addr = new InternetAddress(tmp[0].replaceAll("\\s+", "").trim(), tmp[1].trim(), "utf-8");
                        } else {
                            addr = new InternetAddress(mail_info.replaceAll("\\s+", "").trim());
                        }
                    } catch (Exception var10) {
                        LogService.error(mailinfo + " processMailInfo failed:", var10);
                    }

                    set.add(addr);
                }
            }
            return set;
        }
    }
}
