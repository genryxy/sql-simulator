create table practice_x_task (
    practice_id   int8    not null,
    task_id       int8    not null,
    primary key (practice_id, task_id)
);

alter table if exists practice_x_task
    add constraint practice_x_task_practice_fk
        foreign key (practice_id) references practice
            on delete cascade;

alter table if exists practice_x_task
    add constraint practice_x_task_task_fk
        foreign key (task_id) references task
        on delete cascade;