SET MODE MySQL;

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
    create_date     date         not null,
    last_update_date date         not null

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
select tag.id as tag_id, tag.name as tag_name, c.id, c.name,
       c.price, c.description,
       c.duration, c.create_date, c.last_update_date
from tag
         join certificate_tag ct on ct.tag_id = tag.id
         join certificate c on c.id = ct.certificate_id;

drop view if exists certificate_tags;

create view certificate_tags as
select c.id as certificate_id, c.name as certificate_name,  c.price, c.description,
       c.duration, c.create_date, c.last_update_date, tag.id,
       tag.name
from certificate c
         join certificate_tag ct on ct.certificate_id = c.id
         join tag on tag.id = ct.tag_id;
