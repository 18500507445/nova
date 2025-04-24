package com.nova.excel.entity;

import lombok.Data;

import java.awt.*;

/**
 * 水印配置类
 * @author: wzh
 */
@Data
public class WaterMark {
    /**
     * 水印内容
     */
    private String content = "";

    /**
     * 画笔颜色. 格式为"#RRGGBB"，eg: "#C5CBCF"
     */
    private String color = "#C5CBCF";

    /**
     * 字体样式
     */
    private Font font = new Font("Microsoft YaHei", Font.BOLD, 40);

    /**
     * 水印宽度
     */
    private int width = 300;

    /**
     * 水印高度
     */
    private int height = 100;

    /**
     * 水平倾斜度
     */
    private double shearX = 0.1;

    /**
     * 垂直倾斜度
     */
    private double shearY = -0.26;

    /**
     * 字体的y轴位置
     */
    private int yAxis = 50;
}

