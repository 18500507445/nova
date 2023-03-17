package com.nova.tools.java8.optional;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Optional;

/**
 * 住址对象
 *
 * @author wzh
 * @date 2018/2/11
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
class Address {

    /**
     * 街道
     */
    private String street;

    /**
     * 门牌
     */
    private String door;

}

@Data
class User {
    private String username;
    private String password;
    private Integer age;
    private Address address;

    private Optional<Address> optAddress;

}
