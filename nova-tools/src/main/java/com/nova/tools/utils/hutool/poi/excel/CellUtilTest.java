package com.nova.tools.utils.hutool.poi.excel;

import cn.hutool.core.lang.Console;
import org.apache.poi.ss.usermodel.BuiltinFormats;
import jdk.nashorn.internal.ir.annotations.Ignore;
import org.junit.jupiter.api.Test;

public class CellUtilTest {

    @Test
    @Ignore
    public void isDateTest() {
        String[] all = BuiltinFormats.getAll();
        for (int i = 0; i < all.length; i++) {
            Console.log("{} {}", i, all[i]);
        }
    }
}
