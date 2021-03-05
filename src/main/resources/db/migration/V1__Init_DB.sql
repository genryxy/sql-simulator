create sequence hibernate_sequence start 1 increment 1;

create table message (
    id      int8 not null,
    tag     varchar(255),
    text    varchar(65535) not null,
    user_id int8,
    primary key (id)
);

create table person (
    id              bigserial  primary key not null,
    activation_code varchar(255),
    active          boolean not null,
    email           varchar(255),
    password        varchar(255) not null,
    username        varchar(255) not null
);

create table person_role (
    user_id int8 not null,
    roles   varchar(255)
);

alter table if exists message
    add constraint message_person_fk
    foreign key (user_id) references person;

alter table if exists person_role
    add constraint person_role_person_fk
    foreign key (user_id) references person;