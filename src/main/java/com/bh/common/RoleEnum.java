package com.bh.common;
/*
* 用户格式枚举类
* */
public enum RoleEnum {
    ROLE_USER(1,"普通用户"),
    ROLE_ADMIN(0,"管理员")
    ;


    private int role;
    private String desc;
    RoleEnum(int role,String desc){
        this.desc = desc;
        this.role = role;
    }

    public int getRole() {
        return role;
    }

    public void setRole(int role) {
        this.role = role;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
