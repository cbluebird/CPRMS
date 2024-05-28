package team.sugarsmile.cprms.exception;

/**
 * @author XiMo
 */

public enum ErrorCode {
    SERVER_ERROR(10001, "服务异常"),
    DB_ERROR(10002, "数据库异常"),

    PARAM_ERROR(20001, "参数有误"),
    DEPARTMENT_ALREADY_EXIST(20101, "部门已存在"),
    DEPARTMENT_NOT_EXIST(20102, "部门不存在"),

    ADMIN_ALREADY_EXIST(20201, "管理员已存在"),
    ADMIN_NOT_EXIST(202012, "管理员不存在"),
    ADMIN_LOGIN_ERROR(202013, "管理员登录时密码错误"),
    PASSWORD_SHORT(202014, "密码太短"),
    ADMIN_PASSWORD_OUT_DATE(202015, "密码需要更新"),
    APPOINTMENT_NOT_EXIST(20301,"预约不存在");



    private final Integer code;
    private final String message;

    ErrorCode(final Integer code, final String message) {
        this.code = code;
        this.message = message;
    }

    public static ErrorCode getByCode(Integer code) {
        for (ErrorCode value : ErrorCode.values()) {
            if (code.equals(value.getCode())) {
                return value;
            }
        }
        return SERVER_ERROR;
    }

    public Integer getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}

