package com.nova.rpc.manual.entity;

import com.nova.common.core.model.pojo.BaseBO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BlogBO extends BaseBO {

    private Integer id;

    private Integer useId;

    private String title;
}
