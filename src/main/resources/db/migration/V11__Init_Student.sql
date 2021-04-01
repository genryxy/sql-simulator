create table student (
    user_id int8 not null,
    team_id int8 not null,
    primary key (user_id, team_id)
);

alter table if exists student
    add constraint student_user_fk
        foreign key (user_id) references person
            on delete cascade;

alter table if exists student
    add constraint student_team_fk
        foreign key (team_id) references team
            on delete cascade;
