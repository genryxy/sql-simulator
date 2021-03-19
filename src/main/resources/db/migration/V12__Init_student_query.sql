create table student_query (
    id          bigserial not null,
    query       varchar   not null,
    is_correct  boolean   not null,
    task_id     int8      not null,
    practice_id int8      not null,
    primary key (id)
);

alter table if exists student_query
    add constraint student_query_task_fk
        foreign key (task_id) references task;

alter table if exists student_query
    add constraint student_query_practice_fk
        foreign key (practice_id) references practice;