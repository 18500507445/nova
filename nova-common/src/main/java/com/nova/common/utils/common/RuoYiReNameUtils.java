package com.nova.common.utils.common;

import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.AccessDeniedException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

/**
 * 包结构重构工具，修改若依项目名称，包名
 *
 * @description: 一键修改：支持修改RuoYi、RuoYi-Vue、RuoYi-fast、RuoYi-Cloud四个系列
 */
@Slf4j(topic = "RuoYiReNameUtils")
public class RuoYiReNameUtils {
    // 配置参数
    private static final String OLD_PKG_PREFIX = "ruoyi";    // 原包名前缀
    private static final String NEW_PKG_PREFIX = "stater";   // 新包名前缀
    private static final Path PROJECT_ROOT = Paths.get("xxx"); // 项目根目录

    // 需要处理的文本文件类型
    private static final Set<String> TEXT_EXTENSIONS = new HashSet<>(Arrays.asList(
            "java", "xml", "yml", "yaml", "properties", "html", "js", "vue", "txt", "md", "json"
    ));

    public static void main(String[] args) throws IOException {
        System.out.println("开始重构包结构...");

        // 第一阶段：替换文件内容
        replaceFileContents(PROJECT_ROOT);

        // 第二阶段：重构目录结构
        restructurePackages(PROJECT_ROOT);

        // 第三阶段：重命名文件
        renameFiles(PROJECT_ROOT);

        System.out.println("重构完成！建议执行以下操作：\n1. mvn clean install\n2. npm install && npm run dev");
    }

    // 替换所有文件内容
    private static void replaceFileContents(Path root) throws IOException {
        Files.walk(root)
                .filter(Files::isRegularFile)
                .forEach(file -> {
                    try {
                        // 如果文件路径包含 ruoyi-ui，直接跳过
                        if (file.toString().contains("ruoyi-ui")) {
                            System.out.println("跳过 ruoyi-ui 文件: " + file);
                            return;
                        }

                        if (isTextFile(file)) {
                            String content = new String(Files.readAllBytes(file), StandardCharsets.UTF_8);

                            // 替换包声明
                            String newContent = content.replace(
                                    "package com.ruoyi",
                                    "package com." + NEW_PKG_PREFIX
                            );

                            // 替换其他关键引用
                            newContent = newContent.replaceAll(OLD_PKG_PREFIX, NEW_PKG_PREFIX);

                            // 特殊处理主类名和ServletInitializer
                            if (file.endsWith("RuoYiApplication.java") || file.endsWith("RuoYiServletInitializer.java")) {
                                newContent = newContent.replace(
                                        "RuoYiApplication",
                                        NEW_PKG_PREFIX.substring(0, 1).toUpperCase() + NEW_PKG_PREFIX.substring(1) + "Application"
                                );
                                newContent = newContent.replace(
                                        "RuoYiServletInitializer",
                                        NEW_PKG_PREFIX.substring(0, 1).toUpperCase() + NEW_PKG_PREFIX.substring(1) + "ServletInitializer"
                                );
                            }

                            if (!content.equals(newContent)) {
                                Files.write(file, newContent.getBytes(StandardCharsets.UTF_8));
                                System.out.println("已更新: " + file);
                            }
                        }
                    } catch (Exception e) {
                        log.error("处理失败: ", e);
                    }
                });
    }

    // 重构包目录结构
    private static void restructurePackages(Path root) throws IOException {
        List<Path> dirs = new ArrayList<>();
        Files.walk(root)
                .filter(Files::isDirectory)
                .sorted(Comparator.reverseOrder())
                .forEach(dirs::add);

        for (Path dir : dirs) {
            // 如果目录路径包含 ruoyi-ui，直接跳过
            if (dir.toString().contains("ruoyi-ui")) {
                System.out.println("跳过 ruoyi-ui 目录: " + dir);
                continue;
            }

            String dirPath = dir.toString().replace(File.separatorChar, '/');

            // 处理包含旧包名的目录
            if (dirPath.contains(OLD_PKG_PREFIX)) {
                Path newDir = dir.resolveSibling(
                        dir.getFileName().toString().replace(OLD_PKG_PREFIX, NEW_PKG_PREFIX)
                );

                // 检查目标目录是否存在
                if (Files.exists(newDir)) {
                    System.err.println("目标目录已存在，跳过: " + newDir);
                    continue;
                }

                // 移动目录
                try {
                    Files.createDirectories(newDir.getParent());
                    Files.move(dir, newDir);
                    System.out.println("目录重构: " + dir + " -> " + newDir);
                } catch (AccessDeniedException e) {
                    log.error("权限不足，无法移动目录: {}，url：{}", e, dir);
                } catch (IOException e) {
                    log.error("移动目录失败: {}，url：{}", e, dir);
                }
            }
        }
    }

    // 重命名文件
    private static void renameFiles(Path root) throws IOException {
        Files.walk(root)
                .filter(Files::isRegularFile)
                .forEach(file -> {
                    try {
                        // 如果文件路径包含 ruoyi-ui，直接跳过
                        if (file.toString().contains("ruoyi-ui")) {
                            System.out.println("跳过 ruoyi-ui 文件: " + file);
                            return;
                        }

                        String fileName = file.getFileName().toString();
                        if (fileName.equals("RuoYiApplication.java") || fileName.equals("RuoYiServletInitializer.java")) {
                            // 替换文件名中的 RuoYi 为新包名
                            String newFileName = fileName.replace("RuoYi", NEW_PKG_PREFIX.substring(0, 1).toUpperCase() + NEW_PKG_PREFIX.substring(1));
                            Path newFilePath = file.resolveSibling(newFileName);
                            Files.move(file, newFilePath);
                            System.out.println("文件重命名: " + file + " -> " + newFilePath);
                        }
                    } catch (Exception e) {
                        log.error("重命名文件失败: {}，file：{}", e, file);
                    }
                });
    }

    // 判断是否为文本文件
    private static boolean isTextFile(Path file) {
        String fileName = file.getFileName().toString();
        int dotIndex = fileName.lastIndexOf('.');
        if (dotIndex == -1) return false;
        return TEXT_EXTENSIONS.contains(fileName.substring(dotIndex + 1).toLowerCase());
    }
}
