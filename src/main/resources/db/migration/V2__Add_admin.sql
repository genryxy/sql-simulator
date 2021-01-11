insert into person (id, username, password, active)
    values (1, 'admin', '123', true);

insert into person_role (user_id, roles)
    values (1, 'USER'), (1, 'ADMIN');