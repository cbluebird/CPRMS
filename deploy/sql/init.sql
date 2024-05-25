CREATE TABLE department (
                            id BIGINT PRIMARY KEY AUTO_INCREMENT,
                            type TINYINT NOT NULL COMMENT '部门类型 1:行政部门 2:直属部门 3:学院',
                            name VARCHAR(20) UNIQUE NOT NULL COMMENT '部门名称',
                            public BOOLEAN NOT NULL,
                            business BOOLEAN NOT NULL
);

CREATE TABLE admin (
                       id BIGINT PRIMARY KEY AUTO_INCREMENT,
                       admin_type TINYINT NOT NULL COMMENT '管理员类型 1:系统管理员 2:学校管理员 3:部门管理员 4:审计管理员',
                       name VARCHAR(20)  NOT NULL COMMENT '用户名称',
                       user_name VARCHAR(20) UNIQUE  NOT NULL COMMENT '登录名称',
                       phone VARCHAR(20) UNIQUE NOT NULL COMMENT '电话',
                       password VARCHAR(64)  NOT NULL COMMENT '密码',
                       department_id BIGINT NOT NULL COMMENT '对应的部门id',
                       date DATE NOT NULL COMMENT '密码更新时间'
);

CREATE TABLE role (
                      id BIGINT PRIMARY KEY AUTO_INCREMENT,
                      admin_type TINYINT NOT NULL COMMENT '管理员类型 1:系统管理员 2:学校管理员 3:部门管理员 4:审计管理员',
                      path VARCHAR(20)  NOT NULL COMMENT '规则名称'
)