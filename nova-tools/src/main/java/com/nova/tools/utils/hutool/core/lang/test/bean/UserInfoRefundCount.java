package com.nova.tools.utils.hutool.core.lang.test.bean;

import lombok.Data;

import java.io.Serializable;

@Data
public class UserInfoRefundCount implements Serializable {

    private static final long serialVersionUID = -8397291070139255181L;
    // 完成率
    private String finishedRatio;

    // 自己有多少道题
    private Integer ownershipExamCount;

    // 当前回答了多少道题
    private Integer answeredExamCount;

}
