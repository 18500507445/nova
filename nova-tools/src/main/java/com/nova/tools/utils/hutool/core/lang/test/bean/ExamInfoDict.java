package com.nova.tools.utils.hutool.core.lang.test.bean;

import lombok.Data;

import java.io.Serializable;

/**
 * @author: 质量过关
 */
@Data
public class ExamInfoDict implements Serializable {
    private static final long serialVersionUID = 3640936499125004525L;

    // 主键、可当作题号
    private Integer id;
    // 试题类型 客观题 0主观题 1
    private Integer examType;
    // 试题是否作答
    private Integer answerIs;

    public Integer getId(Integer defaultValue) {
        return this.id == null ? defaultValue : this.id;
    }
}
