package com.nova.common.context;

import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StreamUtils;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * @Description: body reader请求
 * @Author: wangzehui
 * @Date: 2022/12/20 11:16
 */
@Slf4j
public class ReqGetBody {

    public static String getBody(HttpServletRequest request) {
        try {
            ServletInputStream in = request.getInputStream();
            String body;
            body = StreamUtils.copyToString(in, StandardCharsets.UTF_8);
            return body;
        } catch (IOException e) {
            log.error(e.getMessage(), e);
            return "";
        }
    }
}