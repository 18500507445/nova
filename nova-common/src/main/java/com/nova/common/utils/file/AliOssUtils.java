package com.nova.common.utils.file;

import cn.hutool.core.lang.UUID;
import cn.hutool.core.util.ObjectUtil;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.model.GetObjectRequest;
import com.aliyun.oss.model.OSSObject;
import com.nova.common.config.AliOssConfig;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.rmi.ServerException;

/**
 * @author: wzh
 * @description: 阿里oss文件工具类
 * @date: 2024/05/08 14:40
 */
@Slf4j
@Component
public class AliOssUtils {

    private final AliOssConfig aliOssConfig;

    private final OSS ossClient;

    //构造注入
    public AliOssUtils(AliOssConfig aliOssConfig) {
        this.aliOssConfig = aliOssConfig;
        // 获取阿里云 OSS 的访问域名
        String endpoint = aliOssConfig.getEndpoint();
        // 获取访问阿里云 OSS 的 Access Key ID
        String accessKeyId = aliOssConfig.getAccessKeyId();
        // 获取访问阿里云 OSS 的 Access Key Secret
        String accessKeySecret = aliOssConfig.getAccessKeySecret();
        // 创建 OSS 客户端实例，判断为空再进行构建，否则会报错，导致服务起不来
        if (ObjectUtil.isAllNotEmpty(endpoint, accessKeyId, accessKeySecret)) {
            ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
        } else {
            ossClient = null;
        }
    }

    /**
     * 上传本地文件到阿里云 OSS
     *
     * @param sourceFilePathName 本地文件路径
     * @param aimFilePathName    目标文件路径
     * @return 上传后文件的访问 URL
     */
    public String upload(String sourceFilePathName, String aimFilePathName) {
        // 去除目标文件名前面的斜杠（如果存在）
        aimFilePathName = trimLeadingSlash(aimFilePathName);
        // 生成唯一的文件名
        aimFilePathName = UUID.fastUUID().toString(true) + aimFilePathName;
        //仓库名 文件名
        ossClient.putObject(aliOssConfig.getBucketName(), aimFilePathName, new File(sourceFilePathName));
        //关闭ossClient
        ossClient.shutdown();
        // 生成访问上传后文件的 URL，并返回
        return "https://" + aliOssConfig.getBucketName() + "." + aliOssConfig.getEndpoint() + "/" + aimFilePathName;
    }

    /**
     * 上传图片
     *
     * @param file file
     */
    public String uploadImage(MultipartFile file) throws IOException {
        //生成文件名 原来的图片名
        String originalFilename = file.getOriginalFilename();
        String ext = "." + FilenameUtils.getExtension(originalFilename);
        String fileName = UUID.fastUUID().toString(true) + ext;
        //仓库名 文件名
        ossClient.putObject(aliOssConfig.getBucketName(), fileName, file.getInputStream());
        ossClient.shutdown();
        // 生成访问上传后文件的 URL，并返回
        return "https://" + aliOssConfig.getBucketName() + "." + aliOssConfig.getEndpoint() + "/" + fileName;
    }

    /**
     * 下载文件
     *
     * @param objectName 文件名
     * @return 文件的输入流
     */
    public InputStream download(String objectName) {
        // 去除文件名前面的斜杠（如果存在）
        objectName = trimLeadingSlash(objectName);
        // 创建下载文件的请求
        GetObjectRequest getObjectRequest = new GetObjectRequest(aliOssConfig.getBucketName(), objectName);
        // 获取文件对象
        OSSObject ossObject = ossClient.getObject(getObjectRequest);
        // 返回文件的输入流
        return ossObject.getObjectContent();
    }

    /**
     * 删除文件
     *
     * @param objectName 文件名
     */
    public void delete(String objectName) {
        // 去除文件名前面的斜杠（如果存在）
        objectName = trimLeadingSlash(objectName);
        // 删除文件
        ossClient.deleteObject(aliOssConfig.getBucketName(), objectName);
    }

    /**
     * 当Bucket不存在时创建Bucket，Bucket命名规则如下
     * 1.只能包含小写字母、数字和短横线，
     * 2.必须以小写字母和数字开头和结尾
     * 3.长度在3-63之间
     */
    private void createBucket() throws ServerException {
        try {
            //判断是否存在该Bucket，不存在时再重新创建
            if (!ossClient.doesBucketExist(aliOssConfig.getBucketName())) {
                ossClient.createBucket(aliOssConfig.getBucketName());
            }
        } catch (Exception e) {
            log.error("{}", "创建Bucket失败,请核对Bucket名称(规则：只能包含小写字母、数字和短横线，必须以小写字母和数字开头和结尾，长度在3-63之间)");
            throw new ServerException("创建Bucket失败,请核对Bucket名称(规则：只能包含小写字母、数字和短横线，必须以小写字母和数字开头和结尾，长度在3-63之间)");
        }
    }


    /**
     * 去除路径前面的斜杠
     *
     * @param path 路径
     * @return 去除斜杠后的路径
     */
    private static String trimLeadingSlash(String path) {
        // 如果路径以斜杠开头，则去除开头的斜杠
        return path.startsWith("/") ? path.substring(1) : path;
    }

    /**
     * 获取文件的 Content-Type
     *
     * @param fileName 文件名
     * @return 文件的 Content-Type
     */
    private static String getContentType(String fileName) {
        // 根据文件名的后缀判断 Content-Type
        if (fileName.endsWith(".png")) {
            return "image/png";
        } else if (fileName.endsWith(".jpg") || fileName.endsWith(".jpeg")) {
            return "image/jpeg";
        } else if (fileName.endsWith(".gif")) {
            return "image/gif";
        } else if (fileName.endsWith(".bmp")) {
            return "image/bmp";
        } else if (fileName.endsWith(".txt")) {
            return "text/plain";
        } else if (fileName.endsWith(".pdf")) {
            return "application/pdf";
        } else if (fileName.endsWith(".doc") || fileName.endsWith(".docx")) {
            return "application/msword";
        } else if (fileName.endsWith(".xls") || fileName.endsWith(".xlsx")) {
            return "application/vnd.ms-excel";
        } else if (fileName.endsWith(".ppt") || fileName.endsWith(".pptx")) {
            return "application/vnd.ms-powerpoint";
        } else if (fileName.endsWith(".zip")) {
            return "application/zip";
        } else if (fileName.endsWith(".rar")) {
            return "application/x-rar-compressed";
        } else {
            return "application/octet-stream";
        }
    }
}
