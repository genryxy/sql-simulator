create table practice (
    id             bigserial  primary key not null,
    name           varchar(255) not null,
    description    varchar(255) not null,
    author_id      int8 not null
);

alter table if exists practice
    add constraint practice_person_fk
        foreign key (author_id) references person;