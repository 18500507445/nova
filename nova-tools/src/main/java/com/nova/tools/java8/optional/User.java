package com.nova.tools.java8.optional;

import lombok.Data;

import java.util.Optional;

/**
 * User
 *
 * @author biezhi
 * @date 2018/2/11
 */
@Data
public class User {

    private String  username;
    private String  password;
    private Integer age;
    private io.github.biezhi.java8.optional.Address address;

    private Optional<io.github.biezhi.java8.optional.Address> optAddress;

}
