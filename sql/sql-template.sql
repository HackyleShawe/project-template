DROP DATABASE IF EXISTS sql_template_dev;
CREATE DATABASE sql_template_dev CHARSET = utf8mb4 COLLATE = utf8mb4_general_ci;
USE sql_template_dev;

# 实体说明
DROP TABLE IF EXISTS 实体(表)名;
CREATE TABLE 实体(表)名 (
    id          BIGINT       NOT NULL PRIMARY KEY,
   
    deleted     BIT           DEFAULT 0 COMMENT '0-False-未删除, 1-True-已删除',
    create_by   BIGINT        DEFAULT 0,
    create_time DATETIME      DEFAULT NOW(),
    update_by   BIGINT        DEFAULT 0,
    update_time DATETIME      DEFAULT NULL ON UPDATE NOW()
)COMMENT '实体说明';

