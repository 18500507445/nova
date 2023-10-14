package com.nova.common.utils.image;

import cn.hutool.core.lang.UUID;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.commons.io.FilenameUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * @description: 阿里云图片上传
 * @author: wzh
 * @date: 2022/12/5 20:39
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AliUploadImage {

    /**
     * 阿里域名
     */
    public static final String ALI_DOMAIN = "https://czh0123-text.oss-cn-guangzhou.aliyuncs.com/";

    public static String uploadImage(MultipartFile file) throws IOException {
        //生成文件名 原来的图片名
        String originalFilename = file.getOriginalFilename();
        String ext = "." + FilenameUtils.getExtension(originalFilename);
        UUID uuid = UUID.randomUUID();
        String fileName = uuid.toString().replace("-", "") + ext;
        //地域节点
        String endpoint = "";
        String accessKeyId = "";
        String accessKeySecret = "";
        //OSS客户端对象
        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
        //仓库名 文件名
        ossClient.putObject("czh0123-text", fileName, file.getInputStream());
        ossClient.shutdown();
        return ALI_DOMAIN + fileName;
    }

}
