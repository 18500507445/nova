package com.nova.tools.utils.jax;


import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;

/**
 * 导出excel表格
 *
 * @Date 2019/5/16 13:06
 **/
public class ExportCSVUtils {

    /**
     * 写入csv结束，写出流
     * response
     * request
     * tempFile     导出文件
     * filmName     导出文件名称
     **/
    public static void outCsvStream(HttpServletResponse response, HttpServletRequest request, File tempFile, String filmName) throws IOException {
        // 导出文件名称支持汉字
//        filmName = new String(filmName.getBytes("GB2312"), "8859_1");
        filmName = URLEncoder.encode(filmName, "UTF-8");
        java.io.OutputStream out = response.getOutputStream();
        byte[] b = new byte[10240];
        File fileLoad = new File(tempFile.getCanonicalPath());
        response.reset();
        String origin = request.getHeader("Origin");
        if (StringUtils.isNotBlank(origin)) {
            response.addHeader("Access-Control-Allow-Origin", origin);
        }
        response.addHeader("Access-Control-Expose-Headers", "filename");
        response.setHeader("Access-Control-Allow-Credentials", "true");
        response.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE");
        response.setHeader("Access-Control-Max-Age", "3600");
        response.setHeader("Access-Control-Allow-Headers", "x-requested-with,access-token,content-type");
        response.setContentType("application/csv");
        response.setHeader("content-disposition", "attachment");
        response.setHeader("filename", filmName + ".csv");
        java.io.FileInputStream in = new java.io.FileInputStream(fileLoad);
        int n;
        //为了保证excel打开csv不出现中文乱码
        out.write(new byte[]{(byte) 0xEF, (byte) 0xBB, (byte) 0xBF});
        while ((n = in.read(b)) != -1) {
            //每次写入out1024字节
            out.write(b, 0, n);
        }
        in.close();
        out.close();
    }

    /**
     * 删除单个文件
     *
     * @return 单个文件删除成功返回true，否则返回false
     * @Author AnguangWang
     */
    public static boolean deleteFile(File file) {
        // 如果文件路径所对应的文件存在，并且是一个文件，则直接删除
        if (file.exists() && file.isFile()) {
            return file.delete();
        } else {
            return false;
        }
    }
}
