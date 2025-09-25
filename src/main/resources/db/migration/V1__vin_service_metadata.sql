------------ Start - Users Table ------------
CREATE TABLE IF NOT EXISTS "user" (
id bigserial PRIMARY KEY NOT NULL,
uid VARCHAR(36) UNIQUE NOT NULL,
first_name VARCHAR(50) NOT NULL,
last_name VARCHAR(50) NOT NULL,
phone_number VARCHAR(20) NOT NULL,
email VARCHAR(50) UNIQUE NOT NULL,
created_at TIMESTAMP NOT NULL,
updated_at TIMESTAMP NOT NULL
);

CREATE SEQUENCE IF NOT EXISTS seq_user
start 1
increment 1;
------------ End - Users Table ------------


------------ Start - User Address Table ------------
CREATE TABLE IF NOT EXISTS "user_address" (
id bigserial PRIMARY KEY NOT NULL,
user_id INT NOT NULL,
label VARCHAR NOT NULL,
line_1 VARCHAR(255) NOT NULL,
line_2 VARCHAR(255),
city VARCHAR(100) NOT NULL,
state VARCHAR(100) NOT NULL,
country VARCHAR(100) NOT NULL,
postal_code VARCHAR(20) NOT NULL,
created_at TIMESTAMP NOT NULL,
updated_at TIMESTAMP NOT NULL,
UNIQUE (user_id, label),
FOREIGN KEY (user_id) REFERENCES "user" (id) NOT DEFERRABLE
);

CREATE SEQUENCE IF NOT EXISTS seq_user_address
start 1
increment 1;
------------ End - User Address Table ------------