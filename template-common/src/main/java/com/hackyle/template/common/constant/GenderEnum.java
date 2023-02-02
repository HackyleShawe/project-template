package com.hackyle.template.common.constant;

public enum GenderEnum {
    MAN(1, "man", "男"),
    WOMAN(0, "woman", "女"),
    UNKNOWN(3, "unknown", "未知");

    private final int gender;
    private final String code;
    private final String desc;

    GenderEnum(int gender, String code, String desc) {
        this.gender = gender;
        this.code = code;
        this.desc = desc;
    }

    public int getGender() {
        return gender;
    }
    public String getCode() {
        return code;
    }
    public String getDesc() {
        return desc;
    }
}
