create table practice_x_team (
    practice_id   int8    not null,
    team_id       int8    not null,
    primary key (practice_id, team_id)
);

alter table if exists practice_x_team
    add constraint practice_x_team_practice_fk
        foreign key (practice_id) references practice;

alter table if exists practice_x_team
    add constraint practice_x_team_team_fk
        foreign key (team_id) references team;