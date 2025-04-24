package com.nova.tools.utils.hutool.core.util;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.ReflectUtil;
import cn.hutool.core.util.TypeUtil;
import lombok.Data;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * {@link TypeUtil} 针对 Type 的工具类封装
 *
 * @author: Looly
 */
public class TypeUtilTest {

    @Test
    public void getEleTypeTest() {
        Method method = ReflectUtil.getMethod(TestClass.class, "getList");
        Type type = TypeUtil.getReturnType(method);
        Assert.equals("java.util.List<java.lang.String>", type.toString());

        Type type2 = TypeUtil.getTypeArgument(type);
        Assert.equals(String.class, type2);
    }

    @Test
    public void getParamTypeTest() {
        Method method = ReflectUtil.getMethod(TestClass.class, "intTest", Integer.class);
        Type type = TypeUtil.getParamType(method, 0);
        Assert.equals(Integer.class, type);

        Type returnType = TypeUtil.getReturnType(method);
        Assert.equals(Integer.class, returnType);
    }

    public static class TestClass {
        public List<String> getList() {
            return new ArrayList<>();
        }

        public Integer intTest(Integer integer) {
            return 1;
        }
    }

    @Test
    public void getTypeArgumentTest() {
        // 测试不继承父类，而是实现泛型接口时是否可以获取成功。
        final Type typeArgument = TypeUtil.getTypeArgument(IPService.class);
        Assert.equals(String.class, typeArgument);
    }

    public interface OperateService<T> {
        void service(T t);
    }

    public static class IPService implements OperateService<String> {
        @Override
        public void service(String string) {
        }
    }

    @Test
    public void getActualTypesTest() {
        // 测试多层级泛型参数是否能获取成功
        Type idType = TypeUtil.getActualType(Level3.class,
                ReflectUtil.getField(Level3.class, "id"));

        Assert.equals(Long.class, idType);
    }

    public static class Level3 extends Level2<Level3> {

    }

    public static class Level2<E> extends Level1<Long> {

    }

    @Data
    public static class Level1<T> {
        private T id;
    }

}
