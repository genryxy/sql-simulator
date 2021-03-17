insert into task (author_id, name, text, ddl_script, correct_query, points, is_private, category_id)
    values ( 1, 'name task 1', 'Some text for task 1', 'create table test_table (id int8 not null, tag_id int8, text_id int8 not null, user_id int8, primary key (id)); insert into test_table values (1, 11, 111, 1); insert into test_table values (2, 21, 221, 1); insert into test_table values (3, 31, 331, 1);', 'select * from test_table', 4, false, 1);

insert into task (author_id, name, text, ddl_script, correct_query, points, is_private, category_id)
    values (1, 'name task 2', 'Some text for task 2', 'ddlScript2' ,'Correct query for task 2', 4, false, 1);