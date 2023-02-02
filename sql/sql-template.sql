DROP DATABASE IF EXISTS sql_template_dev;
CREATE DATABASE sql_template_dev CHARSET = utf8mb4 COLLATE = utf8mb4_general_ci;
USE sql_template_dev;

# 实体说明
DROP TABLE IF EXISTS 实体(表)名;
CREATE TABLE 实体(表)名 (
    id BIGINT NOT NULL COMMENT 'ID：为了后续数据迁移，不使用自增主键，使用时间戳',

    create_time DATETIME DEFAULT now() COMMENT '创建时间: 年-月-日 时:分:秒',
    update_time DATETIME DEFAULT now() ON UPDATE now() COMMENT '更新时间',
    is_deleted BIT DEFAULT 0 COMMENT '是否删除：0-false-未删除;1-true-已删除',

    PRIMARY KEY (id),
)COMMENT '实体说明';

