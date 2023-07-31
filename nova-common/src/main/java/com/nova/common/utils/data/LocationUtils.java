package com.nova.common.utils.data;

import java.awt.geom.Point2D;
import java.math.BigDecimal;

/**
 * @date: 2019/5/27 19:50
 * @description: 根据两点坐标计算两点之间的距离
 */
public class LocationUtils {

    /**
     * 平均半径,单位：m
     */
    private static final double EARTH_RADIUS = 6371393;

    /**
     * 通过AB点经纬度获取距离
     *
     * @param pointA A点(经，纬)
     * @param pointB B点(经，纬)
     * @return 距离(单位 : 米)
     */
    public static double getDistance(Point2D pointA, Point2D pointB) {
        // 经纬度（角度）转弧度。弧度用作参数，以调用Math.cos和Math.sin
        // A经弧度
        double radiansAX = Math.toRadians(pointA.getX());
        // A纬弧度
        double radiansAY = Math.toRadians(pointA.getY());
        // B经弧度
        double radiansBX = Math.toRadians(pointB.getX());
        // B纬弧度
        double radiansBY = Math.toRadians(pointB.getY());

        // 公式中“cosβ1cosβ2cos（α1-α2）+sinβ1sinβ2”的部分，得到∠AOB的cos值
        double cos = Math.cos(radiansAY) * Math.cos(radiansBY) * Math.cos(radiansAX - radiansBX)
                + Math.sin(radiansAY) * Math.sin(radiansBY);
//        System.err.println("cos = " + cos); // 值域[-1,1]
        // 反余弦值
        double acos = Math.acos(cos);
//        System.err.println("acos = " + acos); // 值域[0,π]
//        System.err.println("∠AOB = " + Math.toDegrees(acos)); // 球心角 值域[0,180]

        BigDecimal bd = new BigDecimal(EARTH_RADIUS * acos);
        return bd.divide(new BigDecimal(1000)).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    public static void main(String[] args) {
        Point2D point2D = new Point2D.Double(116.50655600000000, 39.78813600000000);
        Point2D point2D1 = new Point2D.Double(116.5072996577004, 39.78825559300053);
        System.err.println(getDistance(point2D, point2D1));
    }
}
