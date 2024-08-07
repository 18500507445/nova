package com.nova.tools.utils.hutool.core.convert;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.lang.Assert;
import org.junit.jupiter.api.Test;

/**
 * Enum转换单元测试
 */
public class EnumConvertTest {

    @Test
    public void convertTest() {
        TestEnum bbb = Convert.convert(TestEnum.class, "BBB");
        Assert.equals(TestEnum.B, bbb);

        bbb = Convert.convert(TestEnum.class, 22);
        Assert.equals(TestEnum.B, bbb);
    }

    @Test
    public void toEnumTest() {
        TestEnum ccc = Convert.toEnum(TestEnum.class, "CCC");
        Assert.equals(TestEnum.C, ccc);

        ccc = Convert.toEnum(TestEnum.class, 33);
        Assert.equals(TestEnum.C, ccc);
    }

    enum TestEnum {
        A, B, C;

        public static TestEnum parse(String str) {
            switch (str) {
                case "AAA":
                    return A;
                case "BBB":
                    return B;
                case "CCC":
                    return C;
            }
            return null;
        }

        public static TestEnum parseByNumber(int i) {
            switch (i) {
                case 11:
                    return A;
                case 22:
                    return B;
                case 33:
                    return C;
            }
            return null;
        }
    }
}
