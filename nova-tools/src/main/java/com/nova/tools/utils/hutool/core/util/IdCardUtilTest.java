package com.nova.tools.utils.hutool.core.util;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.IdcardUtil;
import org.junit.jupiter.api.Test;

/**
 * {@link IdcardUtil} 身份证单元测试
 *
 * @author: Looly
 */
public class IdCardUtilTest {

    private static final String ID_18 = "321083197812162119";
    private static final String ID_15 = "150102880730303";

    @Test
    public void isValidCardTest() {
        boolean valid = IdcardUtil.isValidCard(ID_18);
        Assert.isTrue(valid);

        boolean valid15 = IdcardUtil.isValidCard(ID_15);
        Assert.isTrue(valid15);

        // 无效
        String idCard = "360198910283844";
        Assert.isFalse(IdcardUtil.isValidCard(idCard));

        // 生日无效
        idCard = "201511221897205960";
        Assert.isFalse(IdcardUtil.isValidCard(idCard));

        // 生日无效
        idCard = "815727834224151";
        Assert.isFalse(IdcardUtil.isValidCard(idCard));
    }

    @Test
    public void convert15To18Test() {
        String convert15To18 = IdcardUtil.convert15To18(ID_15);
        Assert.equals("150102198807303035", convert15To18);

        String convert15To18Second = IdcardUtil.convert15To18("330102200403064");
        Assert.equals("33010219200403064X", convert15To18Second);
    }

    @Test
    public void convert18To15Test() {
        String idcard15 = IdcardUtil.convert18To15("150102198807303035");
        Assert.equals(ID_15, idcard15);
    }

    @Test
    public void getAgeByIdCardTest() {
        DateTime date = DateUtil.parse("2017-04-10");

        int age = IdcardUtil.getAgeByIdCard(ID_18, date);
        Assert.equals(age, 38);

        int age2 = IdcardUtil.getAgeByIdCard(ID_15, date);
        Assert.equals(age2, 28);
    }

    @Test
    public void getBirthByIdCardTest() {
        String birth = IdcardUtil.getBirthByIdCard(ID_18);
        Assert.equals(birth, "19781216");

        String birth2 = IdcardUtil.getBirthByIdCard(ID_15);
        Assert.equals(birth2, "19880730");
    }

    @Test
    public void getProvinceByIdCardTest() {
        String province = IdcardUtil.getProvinceByIdCard(ID_18);
        Assert.equals(province, "江苏");

        String province2 = IdcardUtil.getProvinceByIdCard(ID_15);
        Assert.equals(province2, "内蒙古");
    }

    @Test
    public void getCityCodeByIdCardTest() {
        String codeByIdCard = IdcardUtil.getCityCodeByIdCard(ID_18);
        Assert.equals("3210", codeByIdCard);
    }

    @Test
    public void getDistrictCodeByIdCardTest() {
        String codeByIdCard = IdcardUtil.getDistrictCodeByIdCard(ID_18);
        Assert.equals("321083", codeByIdCard);
    }

    @Test
    public void getGenderByIdCardTest() {
        int gender = IdcardUtil.getGenderByIdCard(ID_18);
        Assert.equals(1, gender);
    }

    @Test
    public void isValidCard18Test() {
        boolean isValidCard18 = IdcardUtil.isValidCard18("3301022011022000D6");
        Assert.isFalse(isValidCard18);

        // 不忽略大小写情况下，X严格校验必须大写
        isValidCard18 = IdcardUtil.isValidCard18("33010219200403064x", false);
        Assert.isFalse(isValidCard18);
        isValidCard18 = IdcardUtil.isValidCard18("33010219200403064X", false);
        Assert.isTrue(isValidCard18);

        // 非严格校验下大小写皆可
        isValidCard18 = IdcardUtil.isValidCard18("33010219200403064x");
        Assert.isTrue(isValidCard18);
        isValidCard18 = IdcardUtil.isValidCard18("33010219200403064X");
        Assert.isTrue(isValidCard18);

        // 香港人在大陆身份证
        isValidCard18 = IdcardUtil.isValidCard18("81000019980902013X");
        Assert.isTrue(isValidCard18);

        // 澳门人在大陆身份证
        isValidCard18 = IdcardUtil.isValidCard18("820000200009100032");
        Assert.isTrue(isValidCard18);

        // 台湾人在大陆身份证
        isValidCard18 = IdcardUtil.isValidCard18("830000200209060065");
        Assert.isTrue(isValidCard18);
    }

    @Test
    public void isValidHKCardIdTest() {
        String hkCard = "P174468(6)";
        boolean flag = IdcardUtil.isValidHKCard(hkCard);
        Assert.isTrue(flag);
    }

    @Test
    public void isValidTWCardIdTest() {
        String twCard = "B221690311";
        boolean flag = IdcardUtil.isValidTWCard(twCard);
        Assert.isTrue(flag);
        String errTwCard1 = "M517086311";
        flag = IdcardUtil.isValidTWCard(errTwCard1);
        Assert.isFalse(flag);
        String errTwCard2 = "B2216903112";
        flag = IdcardUtil.isValidTWCard(errTwCard2);
        Assert.isFalse(flag);
    }
}
