package com.nova.excel.utils.watermark;

import cn.hutool.core.img.ImgUtil;
import com.alibaba.excel.write.handler.SheetWriteHandler;
import com.alibaba.excel.write.metadata.holder.WriteSheetHolder;
import com.alibaba.excel.write.metadata.holder.WriteWorkbookHolder;
import com.nova.excel.entity.WaterMark;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFRelation;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * 水印-单个工作表导出
 *
 * @author:wzh
 */
@RequiredArgsConstructor
public class WaterMarkHandler implements SheetWriteHandler {
    private final WaterMark watermark;

    private BufferedImage createWatermarkImage(WaterMark watermark) {
        final Font font = this.watermark.getFont();
        final int width = this.watermark.getWidth();
        final int height = this.watermark.getHeight();

        String[] textArray = this.watermark.getContent().split(",");
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        // 背景透明 开始
        Graphics2D g = image.createGraphics();
        image = g.getDeviceConfiguration().createCompatibleImage(width, height, Transparency.TRANSLUCENT);
        g.dispose();
        // 背景透明 结束
        g = image.createGraphics();
        // 设定画笔颜色
        g.setColor(new Color(Integer.parseInt(this.watermark.getColor().substring(1), 16)));
        // 设置画笔字体
        g.setFont(font);
        // 设定倾斜度
        g.shear(this.watermark.getShearX(), this.watermark.getShearY());

        // 设置字体平滑
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int y = this.watermark.getyAxis();
        for (String s : textArray) {
            // 从画框的y轴开始画字符串.假设电脑屏幕中心为0，y轴为正数则在下方
            g.drawString(s, 0, y);
            y = y + font.getSize();
        }

        // 释放画笔
        g.dispose();
        return image;
    }


    /**
     * 为Excel打上水印工具函数
     *
     * @param sheet         excel sheet
     * @param bufferedImage 水印图片字节数组
     */
    public static void putWaterRemarkToExcel(XSSFSheet sheet, BufferedImage bufferedImage) {
        //add relation from sheet to the picture data
        XSSFWorkbook workbook = sheet.getWorkbook();
        int pictureIdx = workbook.addPicture(ImgUtil.toBytes(bufferedImage, ImgUtil.IMAGE_TYPE_PNG), Workbook.PICTURE_TYPE_PNG);
        String rId = sheet.addRelation(null, XSSFRelation.IMAGES, workbook.getAllPictures().get(pictureIdx))
                .getRelationship().getId();
        //set background picture to sheet
        sheet.getCTWorksheet().addNewPicture().setId(rId);
    }

    @Override
    public void beforeSheetCreate(WriteWorkbookHolder writeWorkbookHolder, WriteSheetHolder writeSheetHolder) {

    }

    @SneakyThrows
    @Override
    public void afterSheetCreate(WriteWorkbookHolder writeWorkbookHolder, WriteSheetHolder writeSheetHolder) {
        BufferedImage waterMark = createWatermarkImage(watermark);
        XSSFSheet sheet = (XSSFSheet) writeSheetHolder.getSheet();
        putWaterRemarkToExcel(sheet, waterMark);
    }
}

