create table submission (
    id          bigserial not null,
    query       varchar   not null,
    is_correct  boolean   not null,
    task_id     int8      not null,
    practice_id int8      not null,
    user_id     int8      not null,
    date        timestamp not null,
    primary key (id)
);

alter table if exists submission
    add constraint submission_task_fk
        foreign key (task_id) references task;

alter table if exists submission
    add constraint submission_practice_fk
        foreign key (practice_id) references practice;

alter table if exists submission
    add constraint submission_person_fk
        foreign key (user_id) references person;