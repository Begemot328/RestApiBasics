SET MODE MySQL;
DROP ALL OBJECTS;

drop schema if exists certificates;

create schema if not exists certificates;

use certificates;

create table tag
(
    id   tinyint primary key auto_increment,
    name varchar(40) not null
);

create table certificate
(
    id              tinyint primary key auto_increment,
    name            varchar(100) not null,
    description     text,
    price           double       not null,
    duration        int          not null,
    create_date     timestamp         not null,
    last_update_date timestamp         not null
);

create table certificate_tag
(
    tag_id         tinyint,
    certificate_id tinyint,
    primary key (tag_id, certificate_id),
    foreign key (tag_id) references tag (id) ON DELETE CASCADE ON UPDATE CASCADE,
    foreign key (certificate_id) references certificate (id) ON DELETE CASCADE ON UPDATE CASCADE
);

drop view if exists tag_certificates;

create view tag_certificates as
select tag.id, tag.name, c.id as certificate_id, c.name as certificate_name,  c.price, c.description,
       c.duration, c.create_date, c.last_update_date
from tag
         left join certificate_tag ct on ct.tag_id = tag.id
         left join certificate c on c.id = ct.certificate_id;

drop view if exists certificate_tags;

create view certificate_tags as
select  c.id, c.name, c.price, c.description,
        c.duration, c.create_date, c.last_update_date,
        tag.id as tag_id, tag.name as tag_name
from certificate c
         left join certificate_tag ct on ct.certificate_id = c.id
         left join tag on tag.id = ct.tag_id;


insert into tag(name) values('sport');
insert into tag(name) values('games');
insert into tag(name) values('books');
insert into tag(name) values('films');
insert into tag(name) values('bicycle');


insert into certificate(name, description, price, duration, create_date, last_update_date)
values('OZ.by', null, 140.1, 10, '2021-03-22 09:20:11', '2021-03-22 09:20:11' );
insert  into certificate(name, description, price, duration, create_date, last_update_date)
values('nastolka.by', 'board games certificate', 40.1, 50, '2021-01-22 09:20:11', '2021-03-22 09:20:11' );
insert  into certificate(name, description, price, duration, create_date, last_update_date)
values('rider.by', null, 140.5, 5, '2021-02-22 09:20:11', '2021-03-22 09:20:11' );
insert  into certificate(name, description, price, duration, create_date, last_update_date)
values('tsum.by', null, 120.5, 3, '2020-02-22 09:20:11', '2020-03-22 09:20:11' );
insert  into certificate(name, description, price, duration, create_date, last_update_date)
values('Kryshtal', null, 7.63, 2, '2021-03-22 09:20:11', '2021-04-22 09:20:11');

insert into certificate_tag(tag_id, certificate_id) VALUES (3, 1);
insert into certificate_tag(tag_id, certificate_id) VALUES (2, 2);
insert into certificate_tag(tag_id, certificate_id) VALUES (5, 3);
insert into certificate_tag(tag_id, certificate_id) VALUES (4, 1);
insert into certificate_tag(tag_id, certificate_id) VALUES (5, 2);
insert into certificate_tag(tag_id, certificate_id) VALUES (5, 5);
insert into certificate_tag(tag_id, certificate_id) VALUES (2, 3);