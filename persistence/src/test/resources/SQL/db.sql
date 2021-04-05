drop schema if exists certificates;

create schema if not exists certificates;

use certificates;

create table tag
(
      id   tinyint primary key auto_increment,
      name varchar(40) not null,

      index certificate_id_index (id),
      index certificate_name_index (name)
  );

create table certificate
(
    id              tinyint primary key auto_increment,
    name            varchar(100) not null,
    description     text,
    price           double       not null,
    duration        int          not null,
    create_date     date         not null,
    last_update_date date         not null,

    index certificate_id_index (id),
    index certificate_price_index (price),
    index certificate_name_index (name)

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

insert tag(name) values('sport');
insert tag(name) values('games');
insert tag(name) values('books');
insert tag(name) values('films');
insert tag(name) values('bicycle');

insert certificate(name, description, price, duration, create_date, last_update_date)
values('OZ.by', null, 140.1, 10, '2021_03_22', '2021_03_22' );
insert certificate(name, description, price, duration, create_date, last_update_date)
values('nastolka.by', 'board games certificate', 40.1, 50, '2021_01_22', '2021_03_22' );
insert certificate(name, description, price, duration, create_date, last_update_date)
values('rider.by', null, 140.5, 5, '2021_02_22', '2021_03_22' );
insert certificate(name, description, price, duration, create_date, last_update_date)
values('tsum.by', null, 120.5, 3, '2020_02_22', '2020_03_22' );
insert certificate(name, description, price, duration, create_date, last_update_date)
values('Kryshtal', null, 7.63, 2, '2021_03_22', '2021_04_22' );

insert certificate_tag(tag_id, certificate_id) VALUES (3, 1);
insert certificate_tag(tag_id, certificate_id) VALUES (2, 2);
insert certificate_tag(tag_id, certificate_id) VALUES (5, 3);
insert certificate_tag(tag_id, certificate_id) VALUES (4, 1);
insert certificate_tag(tag_id, certificate_id) VALUES (5, 2);
insert certificate_tag(tag_id, certificate_id) VALUES (5, 5);
insert certificate_tag(tag_id, certificate_id) VALUES (2, 3);