---- PostgreSQL 用拡張機能の有効化（UUID生成用）
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

-- テーブルの削除（存在する場合）
DROP TABLE IF EXISTS t_order_detail;
DROP TABLE IF EXISTS t_order;
DROP TABLE IF EXISTS t_product;
DROP TABLE IF EXISTS employee;
DROP TABLE IF EXISTS authorities;
DROP TABLE IF EXISTS t_password_reset_token;
DROP TABLE IF EXISTS t_user;
DROP TABLE IF EXISTS system_info;
DROP TABLE IF EXISTS audit_log;

-- UUID生成関数はPostgreSQL標準の gen_random_uuid() を使用
-- テーブル作成
CREATE TABLE IF NOT EXISTS t_product (
    id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
    name VARCHAR(100),
    price INTEGER,
    stock INTEGER,
    imgUrl VARCHAR(100),
    createdAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS t_order (
    orderid VARCHAR(100) PRIMARY KEY,
    order_date_time TIMESTAMP,
    customerid UUID,
    customer_name VARCHAR(100),
    employee_name VARCHAR(100),
    payment_method VARCHAR(100)
);

CREATE TABLE IF NOT EXISTS t_order_detail (
    order_id VARCHAR(100),
    product_id UUID,
    quantity INTEGER,
    FOREIGN KEY (order_id) REFERENCES t_order(orderid),
    FOREIGN KEY (product_id) REFERENCES t_product(id),
    PRIMARY KEY (order_id, product_id)
);

CREATE TABLE IF NOT EXISTS employee (
    employeeid UUID DEFAULT gen_random_uuid() PRIMARY KEY,
    employeename VARCHAR(100),
    phone VARCHAR(25),
    emailaddress VARCHAR(100)
);

CREATE TABLE IF NOT EXISTS t_user (
    id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
    username VARCHAR(100) UNIQUE,
    emailaddress VARCHAR(100) UNIQUE,
    address VARCHAR(100),
    phone VARCHAR(100),
    password VARCHAR(100),
    enabled BOOLEAN,
    secret VARCHAR(100),
    mfaenabled BOOLEAN
);

CREATE TABLE IF NOT EXISTS authorities (
    id SERIAL PRIMARY KEY,
    username VARCHAR(100) NOT NULL,
    authority VARCHAR(100) NOT NULL,
    FOREIGN KEY (username) REFERENCES t_user(username)
);

CREATE TABLE IF NOT EXISTS system_info (
    id SERIAL PRIMARY KEY,
    availableprocessors INT,
    systemloadaverage DOUBLE PRECISION,
    usedheapmemory BIGINT,
    maxheapmemory BIGINT,
    recordedat TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS audit_log (
    id SERIAL PRIMARY KEY,
    username VARCHAR(255),
    eventtype VARCHAR(255),
    details TEXT,
    createdat TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS t_password_reset_token (
    id SERIAL PRIMARY KEY,
    token VARCHAR(255) NOT NULL,
    expiredate TIMESTAMP NOT NULL,
    userid UUID NOT NULL,
    FOREIGN KEY (userid) REFERENCES t_user(id)
);
