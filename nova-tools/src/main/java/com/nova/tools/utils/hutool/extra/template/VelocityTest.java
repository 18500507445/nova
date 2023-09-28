package com.nova.tools.utils.hutool.extra.template;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.lang.Dict;
import cn.hutool.core.util.CharsetUtil;
import cn.hutool.extra.template.Template;
import cn.hutool.extra.template.TemplateConfig;
import cn.hutool.extra.template.TemplateEngine;
import cn.hutool.extra.template.TemplateUtil;
import cn.hutool.extra.template.engine.velocity.VelocityEngine;
import org.junit.jupiter.api.Test;

public class VelocityTest {

    @Test
    public void charsetTest() {
        final TemplateConfig config = new TemplateConfig("templates", TemplateConfig.ResourceMode.CLASSPATH);
        config.setCustomEngine(VelocityEngine.class);
        config.setCharset(CharsetUtil.CHARSET_GBK);
        final TemplateEngine engine = TemplateUtil.createEngine(config);
        Template template = engine.getTemplate("velocity_test_gbk.vtl");
        String result = template.render(Dict.create().set("name", "hutool"));
        Assert.equals("你好,hutool", result);
    }
}
