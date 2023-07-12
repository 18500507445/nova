package com.nova.tools.java8.optional;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Optional;

/**
 * @author: wzh
 * @description 用户对象
 * @date: 2023/07/12 09:16
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    private String username;
    private String password;
    private Integer age;
    private Address address;

    private Optional<Address> optAddress;
}

@Data
@NoArgsConstructor
@AllArgsConstructor
class Address{

    /**
     * 街道
     */
    private String street;

    /**
     * 门牌
     */
    private String door;
}
