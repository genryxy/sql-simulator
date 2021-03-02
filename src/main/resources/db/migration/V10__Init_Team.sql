create table team (
    id         int8         not null,
    name       varchar(255) not null,
    invitation varchar(511) not null,
    primary key (id),
    unique (invitation)
);
