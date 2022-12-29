package com.nova.tools.utils.hutool.core.util;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.CoordinateUtil;
import org.junit.jupiter.api.Test;

/**
 * {@link CoordinateUtil} 坐标转换工具类单元测试
 * 测试参考：https://github.com/wandergis/coordtransform
 * @author hongzhe.qin, looly
 */
public class CoordinateUtilTest {

	@Test
	public void wgs84ToGcj02Test(){
		final CoordinateUtil.Coordinate coordinate = CoordinateUtil.wgs84ToGcj02(116.404, 39.915);
		Assert.equals(116.41033392216508D, coordinate.getLng());
		Assert.equals(39.91640428150164D, coordinate.getLat());
	}

	@Test
	public void gcj02ToWgs84Test(){
		final CoordinateUtil.Coordinate coordinate = CoordinateUtil.gcj02ToWgs84(116.404, 39.915);
		Assert.equals(116.39766607783491D, coordinate.getLng());
		Assert.equals(39.91359571849836D, coordinate.getLat());
	}

	@Test
	public void wgs84toBd09Test(){
		final CoordinateUtil.Coordinate coordinate = CoordinateUtil.wgs84ToBd09(116.404, 39.915);
		Assert.equals(116.41671695444782D, coordinate.getLng());
		Assert.equals(39.922698713521726D, coordinate.getLat());
	}

	@Test
	public void bd09toWgs84Test(){
		final CoordinateUtil.Coordinate coordinate = CoordinateUtil.bd09toWgs84(116.404, 39.915);
		Assert.equals(116.39129143419822D, coordinate.getLng());
		Assert.equals(39.907253214522164D, coordinate.getLat());
	}

	@Test
	public void gcj02ToBd09Test() {
		final CoordinateUtil.Coordinate coordinate = CoordinateUtil.gcj02ToBd09(116.404, 39.915);
		Assert.equals(116.41036949371029D, coordinate.getLng());
		Assert.equals(39.92133699351022D, coordinate.getLat());
	}

	@Test
	public void bd09toGcj02Test(){
		final CoordinateUtil.Coordinate coordinate = CoordinateUtil.bd09ToGcj02(116.404, 39.915);
		Assert.equals(116.39762729119315D, coordinate.getLng());
		Assert.equals(39.90865673957631D, coordinate.getLat());
	}

}
