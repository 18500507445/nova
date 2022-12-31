package com.nova.design.create.builder;

import lombok.Data;


/**
 * @description: 用户实体类
 * @author: wzh
 * @date: 2022/12/31 8:24
 */
@Data
public class User {
    private final Long id;
    private final String nickname;

    private User(Long id, String nickname) {
        this.id = id;
        this.nickname = nickname;
    }

    public static UserBuilder builder() {
        return new UserBuilder();
    }

    public static class UserBuilder {
        private Long id;
        private String nickname;

        public UserBuilder id(final Long id) {
            this.id = id;
            return this;
        }

        public UserBuilder nickname(final String nickname) {
            this.nickname = nickname;
            return this;
        }

        public User build() {
            return new User(this.id, this.nickname);
        }
    }
}
