--liquibase formatted sql
--changeset vinay:1
create table erail_notif_endpoint (token varchar(255) not null, user_id varchar(36) not null, createdOn datetime(6) not null, deleted bit not null, endpoint varchar(255), type integer, primary key (token, user_id)) engine=InnoDB
