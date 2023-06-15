package com.nova.tools.utils.hutool.core.stream;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.stream.CollectorUtil;
import cn.hutool.core.stream.StreamUtil;
import cn.hutool.core.lang.Assert;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class StreamUtilTest {

    @Test
    public void ofTest() {
        final Stream<Integer> stream = StreamUtil.of(2, x -> x * 2, 4);
        final String result = stream.collect(CollectorUtil.joining(","));
        Assert.equals("2,4,8,16", result);
    }

    // === iterator ===
    @Test
    public void streamTestNullIterator() {
        //Assert.assertThrows(IllegalArgumentException.class, () -> StreamUtil.of((Iterator<Object>) null));
    }

    @SuppressWarnings({"RedundantOperationOnEmptyContainer", "RedundantCollectionOperation"})
    @Test
    public void streamTestEmptyListToIterator() {
        assertStreamIsEmpty(StreamUtil.of(new ArrayList<>().iterator()));
    }

    @Test
    public void streamTestEmptyIterator() {
        assertStreamIsEmpty(StreamUtil.of(Collections.emptyIterator()));
    }

    @Test
    public void streamTestOrdinaryIterator() {
        final ArrayList<Integer> arrayList = CollUtil.newArrayList(1, 2, 3);
        Assert.equals(new Integer[]{1, 2, 3}, StreamUtil.of(arrayList.iterator()).toArray());

        final HashSet<Integer> hashSet = CollUtil.newHashSet(1, 2, 3);
        Assert.equals(hashSet, StreamUtil.of(hashSet.iterator()).collect(Collectors.toSet()));
    }

    void assertStreamIsEmpty(final Stream<?> stream) {
        Assert.notNull(stream);
        Assert.equals(0, stream.toArray().length);
    }
    // ================ stream test end ================
}
