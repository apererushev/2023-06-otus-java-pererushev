-- Для @GeneratedValue(strategy = GenerationType.IDENTITY)
/*
create table client
(
    id   bigserial not null primary key,
    name varchar(50)
);

 */

-- Для @GeneratedValue(strategy = GenerationType.SEQUENCE)
--create sequence client_SEQ start with 1 increment by 1;

create table client
(
    id bigserial not null primary key,
    address_id bigint,
    name varchar(50)
);

CREATE TABLE address (
    id bigserial not null primary key,
    street varchar(255)
);

CREATE TABLE phone (
    id bigserial not null primary key,
    client_id bigint,
    number varchar(255)
);