package com.nova.tools.java8.grow.jdk8;

import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

/**
 * Base64 增强
 *
 * @author wzh
 * @date 2018/2/8
 */
class Base64Demo {

    @Test
    public void demoA() {
        final String text = "Lets Learn Java 8!";

        final String encoded = Base64
                .getEncoder()
                .encodeToString(text.getBytes(StandardCharsets.UTF_8));
        System.out.println(encoded);

        final String decoded = new String(
                Base64.getDecoder().decode(encoded),
                StandardCharsets.UTF_8);
        System.out.println(decoded);
    }

}
