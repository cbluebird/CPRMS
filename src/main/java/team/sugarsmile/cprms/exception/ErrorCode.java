package team.sugarsmile.cprms.exception;

/**
 * @author XiMo
 */

public enum ErrorCode {
    SERVER_ERROR(10001, "服务异常"),
    DB_ERROR(10002, "数据库异常"),
    REDIS_ERROR(10003, "Redis异常"),
    ZXING_ERROR(10004, "二维码异常"),

    PARAM_ERROR(20001, "参数有误"),

    DEPARTMENT_ALREADY_EXIST(20101, "部门已存在"),
    DEPARTMENT_NOT_EXIST(20102, "部门不存在"),

    ADMIN_USERNAME_ALREADY_EXIST(20201, "该登录名已存在"),
    ADMIN_NOT_EXIST(20202, "管理员不存在"),
    ADMIN_LOGIN_ERROR(20203, "管理员登录时密码错误"),
    PASSWORD_ERROR_TO_MANY(20204, "密码错误次数过多，请五分钟后再试"),
    PASSWORD_NEED_UPDATE(20205, "密码需要更新"),
    ADMIN_NOT_LOGIN(200206, "管理员未登录"),
    PERMISSION_DENIED(200207, "没有操作权限"),
    PASSWORD_UPDATE_SAME(200208, "更新后的密码与更新前相同"),
    ADMIN_PHONE_ALREADY_EXIST(20209, "该联系电话已存在"),

    APPOINTMENT_NOT_EXIST(20301, "预约不存在"),
    APPOINTMENT_HISTORY_NOT_EXIST(20302, "暂无历史预约记录"),

    PASSCODE_NOT_BELONG(200401, "通行码不属于该用户"),
    PASSCODE_OFFICIAL_NOT_APPROVED(200402, "公务通行码未通过审批"),
    ;

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

