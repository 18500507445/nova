package com.nova.starter.sensitive.bean;

import com.nova.starter.sensitive.annotation.Sensitive;
import lombok.Data;

/**
 * @author: wzh
 * @description: 加密基类
 * @date: 2024/03/08 13:44
 */
@Data
public class SensitiveBaseDTO {

    @Sensitive(value = Sensitive.Type.CHINESE_NAME)
    private String name;

    @Sensitive(value = Sensitive.Type.ID_CARD)
    private String idCard;

    @Sensitive(value = Sensitive.Type.BANK_CARD)
    private String bankCard;

    @Sensitive(value = Sensitive.Type.FIXED_PHONE)
    private String fixedPhone;

    @Sensitive(value = Sensitive.Type.MOBILE)
    private String mobile;

    @Sensitive(value = Sensitive.Type.ADDRESS)
    private String address;

    @Sensitive(value = Sensitive.Type.EMAIL)
    private String email;

    @Sensitive(value = Sensitive.Type.CUSTOM_RETAIN_HIDE, start = 3, end = 10)
    private String remark;

    @Sensitive(value = Sensitive.Type.CUSTOM_HIDE, start = 3, end = 7)
    private String remarkA;

    @Sensitive(value = Sensitive.Type.CUSTOM_OVERLAY, start = 3, end = 7, overlay = 3)
    private String remarkB;


}
