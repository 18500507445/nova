package com.nova.tools.utils.hutool.core.lang.test.bean;

import lombok.Data;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

/**
 * 用户信息
 *
 * @author:质量过关
 */
@Data
public class UserInfoDict implements Serializable {
    private static final long serialVersionUID = -936213991463284306L;
    // 用户Id
    private Integer id;
    // 要展示的名字
    private String realName;
    // 头像地址
    private String photoPath;
    private List<ExamInfoDict> examInfoDict;
    private UserInfoRefundCount userInfoRefundCount;

    public UserInfoRefundCount getUserInfoRedundCount() {
        return userInfoRefundCount;
    }

    public void setUserInfoRedundCount(UserInfoRefundCount userInfoRefundCount) {
        this.userInfoRefundCount = userInfoRefundCount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        UserInfoDict that = (UserInfoDict) o;
        return Objects.equals(id, that.id) && Objects.equals(realName, that.realName) && Objects.equals(photoPath, that.photoPath) && Objects.equals(examInfoDict, that.examInfoDict);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, realName, photoPath, examInfoDict);
    }

    @Override
    public String toString() {
        return "UserInfoDict [id=" + id + ", realName=" + realName + ", photoPath=" + photoPath + ", examInfoDict=" + examInfoDict + ", userInfoRedundCount=" + userInfoRefundCount + "]";
    }
}
