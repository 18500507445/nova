package com.nova.tools.utils.hutool.json;

import cn.hutool.core.convert.Convert;
import cn.hutool.json.JSONUtil;
import lombok.Data;
import lombok.EqualsAndHashCode;
import cn.hutool.core.lang.Assert;
import org.junit.jupiter.api.Test;

import java.util.stream.Stream;

/**
 * https://gitee.com/dromara/hutool/issues/I49VZB
 */
public class IssueI49VZBTest {
    public enum NBCloudKeyType {
        /**
         * 指纹
         */
        fingerPrint,
        /**
         * 密码
         */
        password,
        /**
         * 卡片
         */
        card,
        /**
         * 临时密码
         */
        snapKey;

        public static NBCloudKeyType find(String value) {
            return Stream.of(values()).filter(e -> e.getValue().equalsIgnoreCase(value)).findFirst()
                    .orElse(null);
        }


        public static NBCloudKeyType downFind(String keyType) {
            if (fingerPrint.name().equals(keyType.toLowerCase())) {
                return NBCloudKeyType.fingerPrint;
            } else {
                return find(keyType);
            }
        }

        public String getValue() {
            return super.toString().toLowerCase();
        }

    }

    @Data
    @EqualsAndHashCode(callSuper = false)
    public static class UPOpendoor {

        private String keyId;
        private NBCloudKeyType type;
        private String time;
        private int result;

    }

    @Test
    public void toBeanTest() {
        String str = "{type: \"password\"}";
        final UPOpendoor upOpendoor = JSONUtil.toBean(str, UPOpendoor.class);
        Assert.equals(NBCloudKeyType.password, upOpendoor.getType());
    }

    @Test
    public void enumConvertTest() {
        final NBCloudKeyType type = Convert.toEnum(NBCloudKeyType.class, "snapKey");
        Assert.equals(NBCloudKeyType.snapKey, type);
    }
}
