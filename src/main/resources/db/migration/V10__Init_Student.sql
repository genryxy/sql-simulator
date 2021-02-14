create table student (
    id   int8    not null,
    user_id       int8    not null,
    group_id      int8    not null,
    primary key (id)
);

alter table if exists student
    add constraint student_user_fk
        foreign key (user_id) references person;
