package com.nova.tools.utils.hutool.json.issueIVMD5;

import cn.hutool.core.io.resource.ResourceUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.lang.TypeReference;
import cn.hutool.json.JSONUtil;
import org.junit.jupiter.api.Test;

import java.util.List;

public class IssueIVMD5Test {

    /**
     * 测试泛型对象中有泛型字段的转换成功与否
     */
    @Test
    public void toBeanTest() {
        String jsonStr = ResourceUtil.readUtf8Str("issueIVMD5.json");

        TypeReference<BaseResult<StudentInfo>> typeReference = new TypeReference<BaseResult<StudentInfo>>() {
        };
        BaseResult<StudentInfo> bean = JSONUtil.toBean(jsonStr, typeReference.getType(), false);

        StudentInfo data2 = bean.getData2();
        Assert.equals("B4DDF491FDF34074AE7A819E1341CB6C", data2.getAccountId());
    }

    /**
     * 测试泛型对象中有包含泛型字段的类型的转换成功与否，比如List&lt;T&gt; list
     */
    @Test
    public void toBeanTest2() {
        String jsonStr = ResourceUtil.readUtf8Str("issueIVMD5.json");

        TypeReference<BaseResult<StudentInfo>> typeReference = new TypeReference<BaseResult<StudentInfo>>() {
        };
        BaseResult<StudentInfo> bean = JSONUtil.toBean(jsonStr, typeReference.getType(), false);

        List<StudentInfo> data = bean.getData();
        StudentInfo studentInfo = data.get(0);
        Assert.equals("B4DDF491FDF34074AE7A819E1341CB6C", studentInfo.getAccountId());
    }
}
