package com.nova.common.utils.file;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.core.io.ClassPathResource;

import java.io.*;
import java.net.URLDecoder;

/**
 * @description: 文件路径读取文件内容工具类
 * @author: wzh
 * @date: 2022/12/5 21:36
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class FileContentUtils {

    /**
     * 根据文件路径读取文件内容
     *
     * @param fileInPath
     */
    public static void getFileContent(Object fileInPath) throws IOException {
        BufferedReader br = null;
        if (fileInPath == null) {
            return;
        }
        if (fileInPath instanceof String) {
            br = new BufferedReader(new FileReader(new File((String) fileInPath)));
        } else if (fileInPath instanceof InputStream) {
            br = new BufferedReader(new InputStreamReader((InputStream) fileInPath));
        }
        String line;
        if (br != null) {
            while ((line = br.readLine()) != null) {
                System.err.println(line);
            }
            br.close();
        }
    }

    /**
     * 主要核心方法是使用getResource和getPath方法，这里的getResource("")里面是空字符串
     *
     * @param fileName
     */
    public void demoA(String fileName) throws IOException {
        //注意getResource("")里面是空字符串
        String path = this.getClass().getClassLoader().getResource("").getPath();
        System.err.println(path);
        String filePath = path + fileName;
        System.err.println(filePath);
        getFileContent(filePath);
    }

    /**
     * 直接通过文件名getPath来获取路径
     *
     * @param fileName
     */
    public void demoB(String fileName) throws IOException {
        //注意getResource("")里面是空字符串
        String path = this.getClass().getClassLoader().getResource(fileName).getPath();
        System.err.println(path);
        //如果路径中带有中文会被URLEncoder,因此这里需要解码
        String filePath = URLDecoder.decode(path, "UTF-8");
        System.err.println(filePath);
        getFileContent(filePath);
    }

    /**
     * 直接通过文件名+getFile()来获取
     * 如果是文件路径的话getFile和getPath效果是一样的，如果是URL路径的话getPath是带有参数的路径。
     * url.getFile()=/pub/files/foobar.txt?id=123456
     * url.getPath()=/pub/files/foobar.txt
     *
     * @param fileName
     */
    public void demoC(String fileName) throws IOException {
        //注意getResource("")里面是空字符串
        String path = this.getClass().getClassLoader().getResource(fileName).getFile();
        System.err.println(path);
        //如果路径中带有中文会被URLEncoder,因此这里需要解码
        String filePath = URLDecoder.decode(path, "UTF-8");
        System.err.println(filePath);
        getFileContent(filePath);
    }

    /**
     * 直接使用getResourceAsStream方法获取流
     * springboot项目中需要使用此种方法，因为jar包中没有一个实际的路径存放文件
     *
     * @param fileName
     */
    public void demoD(String fileName) throws IOException {
        InputStream in = this.getClass().getClassLoader().getResourceAsStream(fileName);
        getFileContent(in);
    }

    /**
     * 直接使用getResourceAsStream方法获取流
     * 如果不使用getClassLoader，可以使用getResourceAsStream("/配置测试.txt")直接从resources根路径下获取
     *
     * @param fileName
     */
    public void demoE(String fileName) throws IOException {
        InputStream in = this.getClass().getResourceAsStream("/" + fileName);
        getFileContent(in);
    }

    /**
     * 通过ClassPathResource类获取，建议SpringBoot中使用
     * springboot项目中需要使用此种方法，因为jar包中没有一个实际的路径存放文件
     *
     * @param fileName
     */
    public void demoF(String fileName) throws IOException {
        ClassPathResource classPathResource = new ClassPathResource(fileName);
        InputStream inputStream = classPathResource.getInputStream();
        getFileContent(inputStream);
    }

    /**
     * 通过绝对路径获取项目中文件的位置（不能用于服务器）
     *
     * @param fileName
     */
    public void demoG(String fileName) throws IOException {
        //E:\WorkSpace\Git\spring-framework-learning-example
        String rootPath = System.getProperty("user.dir");
        String filePath = rootPath + "\\chapter-2-springmvc-quickstart\\src\\main\\resources\\" + fileName;
        getFileContent(filePath);
    }

    /**
     * 通过绝对路径获取项目中文件的位置（不能用于服务器）
     *
     * @param fileName
     */
    public void demoH(String fileName) throws IOException {
        //参数为空
        File directory = new File("");
        //规范路径：getCanonicalPath() 方法返回绝对路径，会把 ..\ 、.\ 这样的符号解析掉
        String rootCanonicalPath = directory.getCanonicalPath();
        //绝对路径：getAbsolutePath() 方法返回文件的绝对路径，如果构造的时候是全路径就直接返回全路径，如果构造时是相对路径，就返回当前目录的路径 + 构造 File 对象时的路径
        String rootAbsolutePath = directory.getAbsolutePath();
        System.err.println(rootCanonicalPath);
        System.err.println(rootAbsolutePath);
        String filePath = rootCanonicalPath + "\\chapter-2-springmvc-quickstart\\src\\main\\resources\\" + fileName;
        getFileContent(filePath);
    }

    /**
     * 通过绝对路径获取项目中文件的位置(不考虑)
     * 主要是通过设置环境变量，将文件放在环境变量中，原理也是通过绝对路径获取。
     * 示例中我设置了一个环境变量：TEST_ROOT=E:\\WorkSpace\\Git\\spring-framework-learning-example
     * System.getenv("TEST_ROOT");
     * System.getProperty("TEST_ROOT")
     *
     * @param fileName
     */
    public void demoI(String fileName) throws IOException {
        System.setProperty("TEST_ROOT", "E:\\WorkSpace\\Git\\spring-framework-learning-example");
        //参数为空
        String rootPath = System.getProperty("TEST_ROOT");
        System.err.println(rootPath);
        String filePath = rootPath + "\\chapter-2-springmvc-quickstart\\src\\main\\resources\\" + fileName;
        getFileContent(filePath);
    }
}
