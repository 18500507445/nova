package com.nova.common.utils.data;

import java.io.*;
import java.util.function.Consumer;

/**
 * 2019/9/11
 */
public class FileUtil {

    public static String getExtension(String fileName) {
        return fileName.substring(fileName.lastIndexOf("."));
    }

    public static void readFileByLines(String filePath, Consumer<? super String> action) {
        File file = new File(filePath);
        readFileByLines(file, action);
    }

    public static void readFileByLines(File file, Consumer<? super String> action) {
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(file));
            String tempString;
            while ((tempString = reader.readLine()) != null) {
                action.accept(tempString);
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e1) {

                }
            }
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
