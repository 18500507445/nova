package com.nova.common.utils.id;

/**
 * ID生成器工具类
 */
public class IdUtils {

    private IdUtils() {

    }


    /**
     /**
     * 获取随机UUID
     *
     * @return 随机UUID
     */
    public static String randomUuid() {
        return UUID.randomUUID().toString();
    }

    /**
     * 简化的UUID，去掉了横线
     *
     * @return 简化的UUID，去掉了横线
     */
    public static String simpleUuid() {
        return UUID.randomUUID().toString(true);
    }

    /**
     * 获取随机UUID，使用性能更好的ThreadLocalRandom生成UUID
     *
     * @return 随机UUID
     */
    public static String fastUuid() {
        return UUID.fastUUID().toString();
    }

    /**
     * 简化的UUID，去掉了横线，使用性能更好的ThreadLocalRandom生成UUID
     *
     * @return 简化的UUID，去掉了横线
     */
    public static String fastSimpleUuid() {
        return UUID.fastUUID().toString(true);
    }
}
