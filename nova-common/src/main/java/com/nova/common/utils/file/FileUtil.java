package com.nova.common.utils.file;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.util.function.Consumer;

/**
 * 2019/9/11
 *
 * @author wzh
 */
@Slf4j(topic = "FileUtil")
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class FileUtil {

    public static String getExtension(String fileName) {
        return fileName.substring(fileName.lastIndexOf("."));
    }

    public static void readFileByLines(String filePath, Consumer<? super String> action) {
        File file = new File(filePath);
        readFileByLines(file, action);
    }

    public static void readFileByLines(File file, Consumer<? super String> action) {
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String tempString;
            while ((tempString = reader.readLine()) != null) {
                action.accept(tempString);
            }
        } catch (IOException e) {
            log.error("异常信息:", e);
        }
    }

    public static void doWriterFile(ByteArrayOutputStream byteArrayOutputStream, String filePath) throws IOException {
        FileOutputStream fileOutputStream = new FileOutputStream(filePath);
        byteArrayOutputStream.writeTo(fileOutputStream);
    }

    public static void doWriterFile(String content, String filePath) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byteArrayOutputStream.write(content.getBytes());
        FileOutputStream fileOutputStream = new FileOutputStream(filePath);
        byteArrayOutputStream.writeTo(fileOutputStream);
    }

    public static void doWriterFile(byte[] bytes, String filePath) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byteArrayOutputStream.write(bytes);
        FileOutputStream fileOutputStream = new FileOutputStream(filePath);
        byteArrayOutputStream.writeTo(fileOutputStream);
    }

    public static void doWriterFile(byte[] bytes, File file) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byteArrayOutputStream.write(bytes);
        FileOutputStream fileOutputStream = new FileOutputStream(file);
        byteArrayOutputStream.writeTo(fileOutputStream);
    }
}
