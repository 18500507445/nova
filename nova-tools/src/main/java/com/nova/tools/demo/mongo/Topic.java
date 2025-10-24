package com.nova.tools.demo.mongo;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.IdUtil;
import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @description:
 * @author: wzh
 * @date: 2022/9/8 21:47
 */
@Component
//动态表名，如果用SpEL表达式也不行，因为必须是静态方法，但是静态方法初始化就固定值了，所以把当前对象注入到Spring中，当做bean，使用#beanName.method()调用
@Document(collection = "#{@topic.getCollectionName()}")
@CompoundIndexes({@CompoundIndex(name = "union_index01", def = "{'subject': 1,'type': 1}")})
@Data
@Accessors(chain = true)
public class Topic {

    public String getCollectionName() {
        return "tb_topic" + DateUtil.today();
    }

    /**
     * id
     */
    @Id
    private Long id = IdUtil.getSnowflake().nextId();

    /**
     * 名称
     */
    private String name;

    /**
     * 选择题的选项
     */
    private Map<Integer, String> select;

    /**
     * 答案
     */
    private Integer answer;

    /**
     * 题目类型（0：选择题，1：判断题）
     */
    private Integer type;

    /**
     * 题目的学科类型id
     */
    private Long subject;

    /**
     * 备注信息
     */
    private String note;

    /**
     * 相关的图片地址
     */
    private String image;

    /**
     * 创建时间
     */
    private String createTime;

    /**
     * 逻辑删除
     */
    private Boolean delFlag;

    //屏蔽字段
    @Transient
    private Integer pageNo;

    @Transient
    private Integer pageSize;

}
