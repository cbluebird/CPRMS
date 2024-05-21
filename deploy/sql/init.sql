CREATE TABLE department (
                            id BIGINT PRIMARY KEY AUTO_INCREMENT,
                            type TINYINT NOT NULL COMMENT '部门类型 1:行政部门 2:直属部门 3:学院',
                            name VARCHAR(20) UNIQUE NOT NULL COMMENT '部门名称',
                            public BOOLEAN NOT NULL,
                            business BOOLEAN NOT NULL
)

CREATE TABLE admin (
                            id BIGINT PRIMARY KEY AUTO_INCREMENT,
                            admin_type TINYINT NOT NULL COMMENT '部门类型 1:行政部门 2:直属部门 3:学院',
                            name VARCHAR(20) UNIQUE NOT NULL COMMENT '用户名称',
                            phone VARCHAR(20) UNIQUE NOT NULL COMMENT '电话',
                            password longtext UNIQUE NOT NULL COMMENT '密码',
                            department_id BIGINT NOT NULL COMMENT '对应的部门id',
)