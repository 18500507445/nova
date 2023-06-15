package com.nova.tools.utils.hutool.core.lang.tree;

import cn.hutool.core.lang.tree.TreeBuilder;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

public class TreeBuilderTest {

    @Test
    public void checkIsBuiltTest() {
        final TreeBuilder<Integer> of = TreeBuilder.of(0);
        of.build();
        of.append(new ArrayList<>());
    }
}
