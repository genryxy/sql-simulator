create table practice_deadlines(
	practice_id int8 not null,
	date_start timestamp not null,
	date_end timestamp not null,
	after_deadline boolean default false
);

alter table if exists practice_deadlines
    add constraint practice_deadlines_practice_fk
        foreign key (practice_id) references practice;