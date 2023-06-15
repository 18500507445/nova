package com.nova.tools.utils.hutool.core.lang.loader;

import cn.hutool.core.lang.loader.LazyFunLoader;
import cn.hutool.core.lang.Assert;
import org.junit.jupiter.api.Test;

public class LazyFunLoaderTest {

    static class BigObject {

        private boolean isDestroy = false;

        public void destroy() {
            this.isDestroy = true;
        }
    }

    @Test
    public void test1() {

        LazyFunLoader<BigObject> loader = new LazyFunLoader<>(BigObject::new);

        Assert.notNull(loader.get());
        Assert.isTrue(loader.isInitialize());

        // 对于某些对象，在程序关闭时，需要进行销毁操作
        loader.ifInitialized(BigObject::destroy);

        Assert.isTrue(loader.get().isDestroy);
    }

    @Test
    public void test2() {

        LazyFunLoader<BigObject> loader = new LazyFunLoader<>(BigObject::new);

        // 若从未使用，则可以避免不必要的初始化
        loader.ifInitialized(it -> {

            //Assert.fail();
            it.destroy();
        });

        Assert.isFalse(loader.isInitialize());
    }

    @Test
    public void testOnLoadStaticFactoryMethod1() {

        LazyFunLoader<BigObject> loader = LazyFunLoader.on(BigObject::new);

        Assert.notNull(loader.get());
        Assert.isTrue(loader.isInitialize());

        // 对于某些对象，在程序关闭时，需要进行销毁操作
        loader.ifInitialized(BigObject::destroy);

        Assert.isTrue(loader.get().isDestroy);
    }

    @Test
    public void testOnLoadStaticFactoryMethod2() {

        LazyFunLoader<BigObject> loader = LazyFunLoader.on(BigObject::new);

        // 若从未使用，则可以避免不必要的初始化
        loader.ifInitialized(it -> {

            //Assert.fail();
            it.destroy();
        });

    }
}
