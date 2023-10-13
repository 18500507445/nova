package com.nova.shopping.common.constant.dto;

import com.nova.shopping.common.constant.BaseController;
import lombok.*;
import lombok.experimental.Accessors;

/**
 * @description:
 * @author: wzh
 * @date: 2023/4/15 17:02
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class BaseReqDTO extends BaseController {

    private Long userId;

    private Long goodsId;

    private String requestIp = getIp();
}
