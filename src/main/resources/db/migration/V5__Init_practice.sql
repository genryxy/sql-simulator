create table practice (
    id        int8 not null,
    name      varchar(255) not null,
    author_id int8 not null,
    primary key (id)
);

alter table if exists task
    add constraint practice_person_fk
        foreign key (author_id) references person;