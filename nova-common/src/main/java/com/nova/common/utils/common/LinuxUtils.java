package com.nova.common.utils.common;

import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * @author: wzh
 * @description linux命令工具类
 * @date: 2023/10/06 10:08
 */
@Slf4j
public class LinuxUtils {

    private LinuxUtils() {

    }

    /**
     * 执行Linux命令
     *
     * @param command
     */
    public static String executeLinux(String command) {
        StringBuilder sb = new StringBuilder();
        try {
            ProcessBuilder processBuilder = new ProcessBuilder("/bin/bash", "-c", command);
            Process process = processBuilder.start();
            // 获取命令输出
            InputStream inputStream = process.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
                sb.append(line);
            }
            // 获取命令错误输出
            InputStream errorStream = process.getErrorStream();
            BufferedReader errorReader = new BufferedReader(new InputStreamReader(errorStream));
            while ((line = errorReader.readLine()) != null) {
                System.err.println(line);
            }
            // 等待命令执行完成
            int exitCode = process.waitFor();
            System.out.println("命令执行完成，退出码：" + exitCode);
        } catch (IOException | InterruptedException e) {
            log.error("executeLinux异常：", e);
        }
        return sb.toString();
    }
}