package com.nova.excel.utils;

import cn.hutool.core.img.ImgUtil;
import com.alibaba.excel.write.handler.SheetWriteHandler;
import com.alibaba.excel.write.metadata.holder.WriteSheetHolder;
import com.alibaba.excel.write.metadata.holder.WriteWorkbookHolder;
import com.nova.excel.entity.WaterMark;
import org.apache.poi.openxml4j.opc.PackagePartName;
import org.apache.poi.openxml4j.opc.PackageRelationship;
import org.apache.poi.openxml4j.opc.TargetMode;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.*;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * 多个工作表导出
 * @author wzh
 */
public class CustomWaterMarkHandler implements SheetWriteHandler {

    private final WaterMark watermark;

    public CustomWaterMarkHandler(WaterMark watermark) {
        this.watermark = watermark;
    }

    @Override
    public void beforeSheetCreate(WriteWorkbookHolder writeWorkbookHolder, WriteSheetHolder writeSheetHolder) {

    }

    @Override
    public void afterSheetCreate(WriteWorkbookHolder writeWorkbookHolder, WriteSheetHolder writeSheetHolder) {
        try {
            BufferedImage bufferedImage = createWatermarkImage();
            setWaterMarkToExcel((XSSFWorkbook) writeWorkbookHolder.getWorkbook(), bufferedImage);
        } catch (Exception e) {
            throw new RuntimeException("添加水印出错");
        }
    }

    private BufferedImage createWatermarkImage() {
        final Font font = watermark.getFont();
        final int width = watermark.getWidth();
        final int height = watermark.getHeight();

        String[] textArray = watermark.getContent().split(",");
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        // 背景透明 开始
        Graphics2D g = image.createGraphics();
        image = g.getDeviceConfiguration().createCompatibleImage(width, height, Transparency.TRANSLUCENT);
        g.dispose();
        // 背景透明 结束
        g = image.createGraphics();
        // 设定画笔颜色
        g.setColor(new Color(Integer.parseInt(watermark.getColor().substring(1), 16)));
        // 设置画笔字体
        g.setFont(font);
        // 设定倾斜度
        g.shear(watermark.getShearX(), watermark.getShearY());

        // 设置字体平滑
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int y = watermark.getYAxis();
        for (String s : textArray) {
            // 从画框的y轴开始画字符串.假设电脑屏幕中心为0，y轴为正数则在下方
            g.drawString(s, 0, y);
            y = y + font.getSize();
        }

        // 释放画笔
        g.dispose();
        return image;
    }

    private void setWaterMarkToExcel(XSSFWorkbook workbook, BufferedImage bfi) {
        //将图片添加到工作簿
        int pictureIdx = workbook.addPicture(ImgUtil.toBytes(bfi, ImgUtil.IMAGE_TYPE_PNG), Workbook.PICTURE_TYPE_PNG);

        //锁住水印
        XSSFCellStyle cellStyle = workbook.createCellStyle();
        cellStyle.setLocked(true);

        //建立 sheet 和 图片 的关联关系
        XSSFPictureData xssfPictureData = workbook.getAllPictures().get(pictureIdx);
        for (int i = 0; i < workbook.getNumberOfSheets(); i++) {
            XSSFSheet xssfSheet = workbook.getSheetAt(i);
            PackagePartName packagePartName = xssfPictureData.getPackagePart().getPartName();
            PackageRelationship packageRelationship = xssfSheet.getPackagePart()
                    .addRelationship(packagePartName, TargetMode.INTERNAL, XSSFRelation.IMAGES.getRelation(), null);
            //添加水印到工作表
            xssfSheet.getCTWorksheet().addNewPicture().setId(packageRelationship.getId());
        }
    }
}
