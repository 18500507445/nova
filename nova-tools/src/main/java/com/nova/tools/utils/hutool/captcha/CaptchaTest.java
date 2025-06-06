package com.nova.tools.utils.hutool.captcha;

import cn.hutool.captcha.*;
import cn.hutool.captcha.generator.MathGenerator;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.lang.Console;
import org.junit.jupiter.api.Test;

import java.awt.*;

/**
 * 直线干扰验证码单元测试
 *
 * @author: looly
 */
public class CaptchaTest {

    @Test
    public void lineCaptchaTest1() {
        // 定义图形验证码的长和宽
        LineCaptcha lineCaptcha = CaptchaUtil.createLineCaptcha(200, 100);
        Assert.notNull(lineCaptcha.getCode());
        Assert.isTrue(lineCaptcha.verify(lineCaptcha.getCode()));
    }

    @Test
    public void lineCaptchaTest3() {
        // 定义图形验证码的长和宽
        LineCaptcha lineCaptcha = CaptchaUtil.createLineCaptcha(200, 70, 4, 15);
        lineCaptcha.setBackground(Color.yellow);
        lineCaptcha.write("f:/test/captcha/tellow.png");
    }

    @Test
    public void lineCaptchaWithMathTest() {
        // 定义图形验证码的长和宽
        LineCaptcha lineCaptcha = CaptchaUtil.createLineCaptcha(200, 80);
        lineCaptcha.setGenerator(new MathGenerator());
        lineCaptcha.setTextAlpha(0.8f);
        lineCaptcha.write("f:/captcha/math.png");
    }

    @Test
    public void lineCaptchaTest2() {

        // 定义图形验证码的长和宽
        LineCaptcha lineCaptcha = CaptchaUtil.createLineCaptcha(200, 100);
        // LineCaptcha lineCaptcha = new LineCaptcha(200, 100, 4, 150);
        // 图形验证码写出，可以写出到文件，也可以写出到流
        lineCaptcha.write("f:/captcha/line.png");
        Console.log(lineCaptcha.getCode());
        // 验证图形验证码的有效性，返回boolean值
        lineCaptcha.verify("1234");

        lineCaptcha.createCode();
        lineCaptcha.write("f:/captcha/line2.png");
        Console.log(lineCaptcha.getCode());
        // 验证图形验证码的有效性，返回boolean值
        lineCaptcha.verify("1234");
    }

    @Test
    public void circleCaptchaTest() {

        // 定义图形验证码的长和宽
        CircleCaptcha captcha = CaptchaUtil.createCircleCaptcha(200, 100, 4, 20);
        // CircleCaptcha captcha = new CircleCaptcha(200, 100, 4, 20);
        // 图形验证码写出，可以写出到文件，也可以写出到流
        captcha.write("f:/captcha/circle.png");
        // 验证图形验证码的有效性，返回boolean值
        captcha.verify("1234");
    }

    @Test
    public void shearCaptchaTest() {

        // 定义图形验证码的长和宽
        ShearCaptcha captcha = CaptchaUtil.createShearCaptcha(203, 100, 4, 4);
        // ShearCaptcha captcha = new ShearCaptcha(200, 100, 4, 4);
        // 图形验证码写出，可以写出到文件，也可以写出到流
        captcha.write("f:/captcha/shear.png");
        // 验证图形验证码的有效性，返回boolean值
        captcha.verify("1234");
    }

    @Test
    public void shearCaptchaTest2() {

        // 定义图形验证码的长和宽
        ShearCaptcha captcha = new ShearCaptcha(200, 100, 4, 4);
        // 图形验证码写出，可以写出到文件，也可以写出到流
        captcha.write("d:/test/shear.png");
        // 验证图形验证码的有效性，返回boolean值
        captcha.verify("1234");
    }

    @Test
    public void ShearCaptchaWithMathTest() {
        // 定义图形验证码的长和宽
        ShearCaptcha captcha = CaptchaUtil.createShearCaptcha(200, 45, 4, 4);
        captcha.setGenerator(new MathGenerator());
        // ShearCaptcha captcha = new ShearCaptcha(200, 100, 4, 4);
        // 图形验证码写出，可以写出到文件，也可以写出到流
        captcha.write("f:/captcha/shear_math.png");
        // 验证图形验证码的有效性，返回boolean值
        captcha.verify("1234");
    }

    @Test
    public void GifCaptchaTest() {
        GifCaptcha captcha = CaptchaUtil.createGifCaptcha(200, 100, 4);
        captcha.write("d:/test/gif_captcha.gif");
        assert captcha.verify(captcha.getCode());
    }

    @Test
    public void bgTest() {
        LineCaptcha captcha = CaptchaUtil.createLineCaptcha(200, 100, 4, 1);
        captcha.setBackground(Color.WHITE);
        captcha.write("d:/test/test.jpg");
    }
}
