package com.nova.common.core.controller;

import cn.hutool.core.util.StrUtil;
import com.nova.common.constant.Constants;
import com.nova.common.exception.ServiceException;
import com.nova.common.utils.security.SecurityUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * @ClassName: DispatcherController.java
 * @Description: 内部跳转controller
 * @author: wangzehui
 * @Date: 2022/11/21 10:56
 */
@RestController
@RequestMapping("/api")
@Slf4j
public class DispatcherController extends BaseController {

    /**
     * 请求内部转发机制
     *
     * @param request  入参
     *                 apiUrl: 转发地址 function=/api/gameBBS/gameBBSTest&username=123 AES加密结果
     * @param response
     */
    @PostMapping(value = {"/forward", "/dispatcher"})
    public void dispatcher(HttpServletRequest request, HttpServletResponse response) {
        try {
            String apiUrl = request.getParameter("accessSecretData");
            String sid = request.getHeader("sid");
            String source = request.getHeader("source");
            String version = request.getHeader("version");
            String newVersion = request.getHeader("newVersion");
            String clientType = request.getHeader("clientType");
            StringBuilder param = new StringBuilder();
            String function = "";
            if (StrUtil.isNotBlank(apiUrl)) {
                log.info("dispatcher====>before:" + apiUrl);
                apiUrl = SecurityUtil.DecryptAllPara(apiUrl, clientType);
                log.info("dispatcher====>after:" + apiUrl);
                String[] arrUrl = apiUrl.split("&");
                Map<String, String> urlParam = new HashMap<>(16);
                for (String str : arrUrl) {
                    urlParam.put(str.substring(0, str.indexOf("=")), str.substring(str.indexOf("=") + 1));
                }
                Iterator<Map.Entry<String, String>> it = urlParam.entrySet().iterator();
                while (it.hasNext()) {
                    Map.Entry<String, String> entry = it.next();
                    if ("function".equals(entry.getKey())) {
                        function = URLDecoder.decode(entry.getValue(), Constants.UTF8) + "?";
                    } else {
                        //处理特殊参数带有http的 url进行编码
                        if (entry.getValue().contains("+") || (!entry.getKey().contains("avatar") && entry.getValue().contains("http"))) {
                            param.append(entry.getKey() + "=" + URLEncoder.encode(entry.getValue(), Constants.UTF8) + "&");
                        } else {
                            param.append(entry.getKey() + "=" + URLDecoder.decode(entry.getValue(), Constants.UTF8) + "&");
                        }
                    }
                }
            } else {
                function = request.getParameter("function") + "?";
            }
            param.append("sid=" + sid + "&source=" + source + "&version=" + version + "&newVersion=" + newVersion + "&clientType=" + clientType);
            String url = function + param;
            request.getRequestDispatcher(url).forward(request, response);
        } catch (Exception e) {
            throw new ServiceException("请求参数解析异常");
        }
    }

}
