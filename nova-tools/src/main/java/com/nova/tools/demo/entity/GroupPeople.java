package com.nova.tools.demo.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import java.util.List;

/**
 * @description:
 * @author: wzh
 * @date: 2019/6/14 20:04
 */
@Data
@SuperBuilder
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
