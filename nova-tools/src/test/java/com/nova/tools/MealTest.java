package com.nova.tools;

import cn.hutool.core.util.StrUtil;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.util.Units;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * @author: wzh
 * @description 扫描PATH路径下jpg图片(按照01.jpg 、 10.jpg格式升序)，按照100宽、220高像素写入表格
 * @date: 2023/10/09 15:37
 */
public class MealTest {

    private static final String PATH = "/Users/wangzehui/Downloads/公司/加班报销/9月/";

    private static final String DOC_PATH = PATH + "外卖订单.docx";

    public static final String PICTURE_TYPE = "jpg";

    public static void main(String[] args) throws IOException, InvalidFormatException {
        File picture = new File(PATH);
        File[] files = picture.listFiles();
        if (null != files) {
            List<File> fileList = Arrays.asList(files);
            //排序
            Collections.sort(fileList);
            //创建一个document对象，相当于新建一个word文档（后缀名为.docx）
            XWPFDocument document = new XWPFDocument();
            //创建一个段落对象
            XWPFParagraph paragraph = document.createParagraph();
            //将run加入到段落
            XWPFRun run = paragraph.createRun();
            //循环插入图片
            for (File file : files) {
                String path = file.getPath();
                if (StrUtil.contains(path, PICTURE_TYPE) && !StrUtil.contains(path, "完成图")) {
                    //文本中图片的名称，切割出xx.jpg
                    String name = StrUtil.subAfter(path, PATH, true);
                    run.addPicture(Files.newInputStream(Paths.get(file.toURI())),
                            XWPFDocument.PICTURE_TYPE_PNG,
                            name,
                            Units.toEMU(100),
                            Units.toEMU(220));
                }
            }
            //创建一个输出流，保存文档
            OutputStream outputStream = Files.newOutputStream(Paths.get(DOC_PATH));
            document.write(outputStream);
            outputStream.close();
        }

    }

}
