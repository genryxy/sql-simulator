create table practice_deadlines(
	practice_id int8 not null,
	date_start timestamp not null,
	date_end timestamp not null,
	after_deadline boolean default false
);

insert into practice_deadlines (practice_id, date_start, date_end)
 values (2, '2021-04-12 21:35:13.908944', '2022-10-21 21:35:13.908944');


alter table if exists practice_deadlines
    add constraint practice_deadlines_practice_fk
        foreign key (practice_id) references practice
            on delete cascade;