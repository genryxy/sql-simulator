create table team (
    id         bigserial  primary key not null,
    author_id       int8    not null,
    name       varchar(255) not null,
    invitation varchar(511) not null,
    unique (invitation)
);
