insert into `users` (`email`, `username`, `password`)
values
    ('test1@test.com', 'aaaa',  '{noop}1111!'),
    ('test1@test.com', 'bbbb',  '{noop}1111!')
;

insert into `user_roles`(`user_id`, `roles`)
values
    (1, 'USER'),
    (2, 'USER')
;

insert into `questions`(`subject`, `content`, `author_id`)
values
    ('test', 'tttatat', 1),
    ('test1', 'asdgdqdf', 1),
    ('test2', 'tttasdfasdatat', 1)
;

insert into `answers`(`content`, `author_id`, `question_id`)
values
    ('answer', 2, 1),
    ('answer111', 2, 1),
    ('answer', 2, 1)
;
