insert into person (id, username, password, email, active)
    values (1, 'admin', '123', 'some@email.com', true);

insert into person_role (user_id, roles)
    values (1, 'USER'), (1, 'ADMIN');