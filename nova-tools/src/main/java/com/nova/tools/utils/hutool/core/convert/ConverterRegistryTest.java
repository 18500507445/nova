package com.nova.tools.utils.hutool.core.convert;

import cn.hutool.core.convert.Converter;
import cn.hutool.core.convert.ConverterRegistry;
import cn.hutool.core.lang.Assert;
import org.junit.jupiter.api.Test;

/**
 * ConverterRegistry 单元测试
 *
 * @author: Looly
 */
public class ConverterRegistryTest {

    @Test
    public void getConverterTest() {
        Converter<Object> converter = ConverterRegistry.getInstance().getConverter(CharSequence.class, false);
        Assert.notNull(converter);
    }

    @Test
    public void customTest() {
        int a = 454553;
        ConverterRegistry converterRegistry = ConverterRegistry.getInstance();

        CharSequence result = converterRegistry.convert(CharSequence.class, a);
        Assert.equals("454553", result);

        //此处做为示例自定义CharSequence转换，因为Hutool中已经提供CharSequence转换，请尽量不要替换
        //替换可能引发关联转换异常（例如覆盖CharSequence转换会影响全局）
        converterRegistry.putCustom(CharSequence.class, CustomConverter.class);
        result = converterRegistry.convert(CharSequence.class, a);
        Assert.equals("Custom: 454553", result);
    }

    public static class CustomConverter implements Converter<CharSequence> {
        @Override
        public CharSequence convert(Object value, CharSequence defaultValue) throws IllegalArgumentException {
            return "Custom: " + value.toString();
        }
    }
}
