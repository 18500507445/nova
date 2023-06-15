package com.nova.tools.utils.hutool.cron.pattern;

import cn.hutool.cron.CronException;
import cn.hutool.cron.pattern.CronPatternBuilder;
import cn.hutool.cron.pattern.Part;
import cn.hutool.core.lang.Assert;
import org.junit.jupiter.api.Test;

public class CronPatternBuilderTest {

    @Test
    public void buildMatchAllTest() {
        String build = CronPatternBuilder.of().build();
        Assert.equals("* * * * *", build);

        build = CronPatternBuilder.of()
                .set(Part.SECOND, "*")
                .build();
        Assert.equals("* * * * * *", build);

        build = CronPatternBuilder.of()
                .set(Part.SECOND, "*")
                .set(Part.YEAR, "*")
                .build();
        Assert.equals("* * * * * * *", build);
    }

    @Test
    public void buildRangeTest() {
        String build = CronPatternBuilder.of()
                .set(Part.SECOND, "*")
                .setRange(Part.HOUR, 2, 9)
                .build();
        Assert.equals("* * 2-9 * * *", build);
    }

    @Test
    public void buildRangeErrorTest() {
        String build = CronPatternBuilder.of()
                .set(Part.SECOND, "*")
                // 55无效值
                .setRange(Part.HOUR, 2, 55)
                .build();
        Assert.equals("* * 2-9 * * *", build);
    }
}
