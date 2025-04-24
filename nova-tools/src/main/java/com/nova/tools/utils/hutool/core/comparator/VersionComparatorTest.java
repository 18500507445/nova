package com.nova.tools.utils.hutool.core.comparator;

import cn.hutool.core.comparator.VersionComparator;
import cn.hutool.core.lang.Assert;
import org.junit.jupiter.api.Test;

/**
 * 版本比较单元测试
 *
 * @author:looly
 */
public class VersionComparatorTest {

    @Test
    public void versionComparatorTest1() {
        int compare = VersionComparator.INSTANCE.compare("1.2.1", "1.12.1");
        Assert.isTrue(compare < 0);
    }

    @Test
    public void versionComparatorTest2() {
        int compare = VersionComparator.INSTANCE.compare("1.12.1", "1.12.1c");
        Assert.isTrue(compare < 0);
    }

    @Test
    public void versionComparatorTest3() {
        int compare = VersionComparator.INSTANCE.compare(null, "1.12.1c");
        Assert.isTrue(compare < 0);
    }

    @Test
    public void versionComparatorTest4() {
        int compare = VersionComparator.INSTANCE.compare("1.13.0", "1.12.1c");
        Assert.isTrue(compare > 0);
    }

    @Test
    public void versionComparatorTest5() {
        int compare = VersionComparator.INSTANCE.compare("V1.2", "V1.1");
        Assert.isTrue(compare > 0);
    }

    @Test
    public void versionComparatorTes6() {
        int compare = VersionComparator.INSTANCE.compare("V0.0.20170102", "V0.0.20170101");
        Assert.isTrue(compare > 0);
    }

    @Test
    public void equalsTest() {
        VersionComparator first = new VersionComparator();
        VersionComparator other = new VersionComparator();
        Assert.notEquals(first, other);
    }
}
