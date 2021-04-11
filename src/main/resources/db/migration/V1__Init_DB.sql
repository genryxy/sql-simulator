create sequence hibernate_sequence start 1 increment 1;

create table person (
    id              bigserial  primary key not null,
    activation_code varchar(255),
    active          boolean not null,
    email           varchar(255) not null,
    password        varchar(255) not null,
    username        varchar(255) not null,
    firstname        varchar(255) not null,
    lastname        varchar(255) not null
);

create table person_role (
    user_id int8 not null,
    roles   varchar(255)
);

alter table if exists person_role
    add constraint person_role_person_fk
    foreign key (user_id) references person;