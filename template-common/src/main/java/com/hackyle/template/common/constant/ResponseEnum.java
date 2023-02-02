package com.hackyle.template.common.constant;

/**
 * 状态码以及状态描述枚举类
 */
public enum ResponseEnum {
    OP_OK(2000, "操作成功"),
    OP_FAIL(5000, "操作失败"),

    //前端出现错误
    FRONT_END_ERROR(4000, "前端错误"),
    PARAMETER_MISSING(4001, "必传参数未传"),

    //后端出现错误
    BACK_END_ERROR(5000, "后端错误"),
    EXCEPTION(5001, "出现异常"),
    ERROR(5999, "未知错误"),

    //通用
    SIGN_UP_OK(6010, "注册成功"),
    SIGN_UP_FAIL(6011, "注册失败"),

    SIGN_IN_OK(6020, "登录成功"),
    SIGN_IN_FAIL(6021, "登录失败"),

    LOG_OUT_OK(6030, "注销成功"),
    LOG_OUT_FAIL(6031, "注销失败"),

    //CRUD
    CREATE_OK(7010, "新增成功"),
    CREATE_FAIL(7011, "新增失败"),

    DELETE_OK(7020, "删除成功"),
    DELETE_FAIL(7021, "删除失败"),

    UPDATE_OK(7030, "更新成功"),
    UPDATE_FAIL(7031, "更新失败"),

    QUERY_OK(7040, "查询成功"),
    QUERY_FAIL(7041, "查询失败")
    ;

    private final Integer code;
    private final String message;

    public Integer getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    ResponseEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
}
