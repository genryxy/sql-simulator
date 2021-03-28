create table task (
    id            bigserial primary key not null,
    author_id     int8                  not null,
    name          varchar(255)          not null,
    text          varchar               not null,
    ddl_script    varchar               not null,
    correct_query varchar               not null,
    points        int4                  not null,
    is_private    boolean               not null,
    category_id   int8                  not null
);

create table category (
    id   bigserial primary key not null,
    name varchar(255)          not null
);

alter table if exists task
    add constraint task_person_fk
        foreign key (author_id) references person;

alter table if exists task
    add constraint task_category_fk
        foreign key (category_id) references category;
