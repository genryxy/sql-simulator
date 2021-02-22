create table student_query (
    id        int8          not null,
    query     varchar(4095) not null,
    is_correct boolean       not null,
    task_id   int8          not null,
    primary key (id)
);

alter table if exists student_query
    add constraint student_query_task_fk
        foreign key (task_id) references task;