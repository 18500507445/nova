package com.nova.tools.utils.maven;

import java.io.File;

/**
 * @Description: maven 本地仓库清理类工具类
 * 删除本地maven仓库中的空文件夹
 * 删除本地maven仓库中含有后缀为.lastUpdated的文件的文件夹
 * 删除本地maven仓库中后缀为.jar-in-progress的文件的文件夹
 * @Author: wangzehui
 * @Date: 2022/8/6 13:17
 */
public class DeleteMaven {

    /**
     * 注意：MAVEN_REPO_PATH 的值需要替换成你自己的maven本地仓库地址
     */
    private static final String MAVEN_REPO_PATH = "/Library/Apache/repository";

    private static final String[] SUFFIX = {".lastUpdated", "jar-in-progress"};

    /**
     * @param args
     */
    public static void main(String[] args) {
        File mavenRep = new File(MAVEN_REPO_PATH);
        if (!mavenRep.exists()) {
            System.out.println("Maven repos is not exist.");
            return;
        }
        File[] files = mavenRep.listFiles();
        checkAndDeleteFiles(files);
        /*第一次不会删除 删除（lastUpdated，progress）之后变成空的文件夹*/
        checkAndDeleteFiles(files);
        System.out.println("Clean lastUpdated files and in-progess jar finished.");
    }

    private static void checkAndDeleteFiles(File[] files) {
        for (File file : files) {
            if (file.isDirectory() == true) {
                if (file.listFiles().length == 0) {
                    // 删除空文件夹
                    System.out.println("删除文件夹：" + file.getAbsolutePath());
                    file.delete();
                } else {
                    checkAndDeleteFiles(file.listFiles());
                }
            } else {
                for (String suffix : SUFFIX) {
                    if (file.getName().contains(suffix)) {
                        System.out.println("删除文件：" + file.getAbsolutePath());
                        file.delete();
                    }
                }
            }
        }
    }

}
