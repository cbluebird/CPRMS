CREATE TABLE department
(
    id              BIGINT PRIMARY KEY AUTO_INCREMENT,
    department_type TINYINT            NOT NULL COMMENT '部门类型 1:行政部门 2:直属部门 3:学院',
    name            VARCHAR(20) UNIQUE NOT NULL COMMENT '部门名称',
    public          BOOLEAN            NOT NULL,
    official        BOOLEAN            NOT NULL
);

CREATE TABLE admin
(
    id                   BIGINT PRIMARY KEY AUTO_INCREMENT,
    admin_type           TINYINT            NOT NULL COMMENT '管理员类型 1:系统管理员 2:学校管理员 3:部门管理员 4:审计管理员',
    user_name            VARCHAR(20) UNIQUE NOT NULL COMMENT '登录名称',
    password             VARCHAR(64)        NOT NULL COMMENT '密码',
    password_update_time DATE               NOT NULL COMMENT '密码更新时间',
    name                 VARCHAR(20)        NOT NULL COMMENT '姓名',
    phone                VARCHAR(64) UNIQUE NOT NULL COMMENT '手机号',
    department_id        BIGINT             NOT NULL COMMENT '对应的部门id'
);

CREATE TABLE rule
(
    id         BIGINT PRIMARY KEY AUTO_INCREMENT,
    admin_type TINYINT     NOT NULL COMMENT '管理员类型 1:系统管理员 2:学校管理员 3:部门管理员 4:审计管理员',
    path       VARCHAR(50) NOT NULL COMMENT '规则名称'
);

CREATE TABLE public_appointment
(
    id             BIGINT PRIMARY KEY AUTO_INCREMENT,
    name           VARCHAR(20) NOT NULL COMMENT '姓名',
    id_card        VARCHAR(64) NOT NULL COMMENT '身份证号',
    phone          VARCHAR(32) NOT NULL COMMENT '手机号',
    campus         TINYINT     NOT NULL COMMENT '校区 1:朝晖校区 2:屏峰校区 3:莫干山校区',
    create_time    DATE        NOT NULL COMMENT '创建(申请)时间',
    start_time     DATE        NOT NULL COMMENT '预约进校开始时间',
    end_time       DATE        NOT NULL COMMENT '预约进校结束时间',
    unit           VARCHAR(20) NOT NULL COMMENT '所在单位',
    transportation TINYINT     NOT NULL COMMENT '交通方式 1:步行 2:自驾',
    license_plate  VARCHAR(10) NOT NULL COMMENT '车牌号(自驾填写)'
);

CREATE TABLE official_appointment
(
    id             BIGINT PRIMARY KEY AUTO_INCREMENT,
    name           VARCHAR(20)  NOT NULL COMMENT '姓名',
    id_card        VARCHAR(64)  NOT NULL COMMENT '身份证号',
    phone          VARCHAR(32)  NOT NULL COMMENT '手机号',
    campus         TINYINT      NOT NULL COMMENT '校区 1:朝晖校区 2:屏峰校区 3:莫干山校区',
    create_time    DATE         NOT NULL COMMENT '创建(申请)时间',
    start_time     DATE         NOT NULL COMMENT '预约进校开始时间',
    end_time       DATE         NOT NULL COMMENT '预约进校结束时间',
    unit           VARCHAR(20)  NOT NULL COMMENT '所在单位',
    transportation TINYINT      NOT NULL COMMENT '交通方式 1:步行 2:自驾',
    license_plate  VARCHAR(10)  NOT NULL COMMENT '车牌号(自驾填写)',
    department_id  BIGINT       NOT NULL COMMENT '公务访问部门',
    receptionist   VARCHAR(20)  NOT NULL COMMENT '公务访问接待人',
    reason         VARCHAR(200) NOT NULL COMMENT '来访事由',
    status         TINYINT      NOT NULL COMMENT '审核状态 1:未审核 2:通过 3:驳回'
);

CREATE TABLE audit
(
    id          BIGINT PRIMARY KEY AUTO_INCREMENT,
    operate     VARCHAR(64) NOT NULL COMMENT '操作',
    admin_id    BIGINT      NOT NULL COMMENT '管理员id',
    type        TINYINT     NOT NULL COMMENT '操作类型 1:登录 2:添加 3:删除 4:更新 5:查询',
    hmac        VARCHAR(64) NOT NULL COMMENT '验证数值',
    create_time DATETIME    NOT NULL COMMENT '创建时间'
);