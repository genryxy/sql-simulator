create table task (
    id              int8    not null,
    author_id       int8    not null,
    name            varchar(255) not null,
    text            varchar(4095),
    correct_query   varchar(4095),
    points          int4   not null,
    is_private      boolean not null,
    category_id     int8    not null,
    primary key (id)
);
alter table if exists task
    add constraint task_person_fk
    foreign key (author_id) references person;

