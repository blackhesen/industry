package com.hesen.base;

import java.io.Serializable;

public class BaseBean implements Serializable {

    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = 1L;

    private String deleteFlag;//   删除标记(N正常 -Y删除)

    private Long createTime;

    private String createUser;

    private Long updateTime;

    private String updateUser;

    private Integer version;

    public String getDeleteFlag() {
        return deleteFlag;
    }

    public BaseBean setDeleteFlag(String deleteFlag) {
        this.deleteFlag = deleteFlag;
        return this;
    }

    public Long getCreateTime() {
        return createTime;
    }

    public BaseBean setCreateTime(Long createTime) {
        this.createTime = createTime;
        return this;
    }

    public String getCreateUser() {
        return createUser;
    }

    public BaseBean setCreateUser(String createUser) {
        this.createUser = createUser;
        return this;
    }

    public Long getUpdateTime() {
        return updateTime;
    }

    public BaseBean setUpdateTime(Long updateTime) {
        this.updateTime = updateTime;
        return this;
    }

    public String getUpdateUser() {
        return updateUser;
    }

    public BaseBean setUpdateUser(String updateUser) {
        this.updateUser = updateUser;
        return this;
    }

    public Integer getVersion() {
        return version;
    }

    public BaseBean setVersion(Integer version) {
        this.version = version;
        return this;
    }
}
