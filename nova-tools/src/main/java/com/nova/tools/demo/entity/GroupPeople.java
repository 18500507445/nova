package com.nova.tools.demo.entity;

import lombok.*;

import java.util.List;

/**
 * @Description:
 * @Author: wangzehui
 * @Date: 2019/6/14 20:04
 */
@Data
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class GroupPeople {

    /**
     * 分组id
     */
    private Integer groupId;

    /**
     * 分组名称
     */
    private String groupName;

    /**
     * PeopleList
     */
    private List<People> listPeople;
}
