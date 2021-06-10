create table audit
(
    id          int auto_increment
        primary key,
    user_id     int         not null,
    entity_id   int         not null,
    entity_name varchar(20) not null,
    operation   varchar(20) not null,
    date        timestamp   not null,
    foreign key (user_id) references user (id) on update cascade on delete cascade,
    foreign key (user_id) references account (id) on update cascade on delete cascade
);

create index audit_entity_id
    on audit (entity_id);

create index audit_user_id
    on audit (user_id);

