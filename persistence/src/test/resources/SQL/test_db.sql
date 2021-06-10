SET MODE MySQL;
DROP
ALL OBJECTS;


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
    id               tinyint primary key auto_increment,
    name             varchar(100) not null,
    description      text,
    price            double       not null,
    duration         int          not null,
    create_date      timestamp    not null,
    last_update_date timestamp    not null
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
select tag.id,
       tag.name,
       c.id   as certificate_id,
       c.name as certificate_name,
       c.price,
       c.description,
       c.duration,
       c.create_date,
       c.last_update_date
from tag
         left join certificate_tag ct on ct.tag_id = tag.id
         left join certificate c on c.id = ct.certificate_id;

drop view if exists certificate_tags;

create view certificate_tags as
select c.id,
       c.name,
       c.price,
       c.description,
       c.duration,
       c.create_date,
       c.last_update_date,
       tag.id   as tag_id,
       tag.name as tag_name
from certificate c
         left join certificate_tag ct on ct.certificate_id = c.id
         left join tag on tag.id = ct.tag_id;


insert into tag(name)
values ('sport');
insert into tag(name)
values ('games');
insert into tag(name)
values ('books');
insert into tag(name)
values ('films');
insert into tag(name)
values ('bicycle');


insert into certificate(name, description, price, duration, create_date, last_update_date)
values ('OZ.by', null, 140.1, 10, '2021-03-22 09:20:11', '2021-03-22 09:20:11');
insert into certificate(name, description, price, duration, create_date, last_update_date)
values ('nastolka.by', 'board games certificate', 40.1, 50, '2021-01-22 09:20:11', '2021-03-22 09:20:11');
insert into certificate(name, description, price, duration, create_date, last_update_date)
values ('rider.by', null, 140.5, 5, '2021-02-22 09:20:11', '2021-03-22 09:20:11');
insert into certificate(name, description, price, duration, create_date, last_update_date)
values ('tsum.by', null, 120.5, 3, '2020-02-22 09:20:11', '2020-03-22 09:20:11');
insert into certificate(name, description, price, duration, create_date, last_update_date)
values ('Kryshtal', null, 7.63, 2, '2021-03-22 09:20:11', '2021-04-22 09:20:11');

insert into certificate_tag(tag_id, certificate_id)
VALUES (3, 1);
insert into certificate_tag(tag_id, certificate_id)
VALUES (2, 2);
insert into certificate_tag(tag_id, certificate_id)
VALUES (5, 3);
insert into certificate_tag(tag_id, certificate_id)
VALUES (4, 1);
insert into certificate_tag(tag_id, certificate_id)
VALUES (5, 2);
insert into certificate_tag(tag_id, certificate_id)
VALUES (5, 5);
insert into certificate_tag(tag_id, certificate_id)
VALUES (2, 3);


drop table if exists user;

create table if not exists user
(
    id  int primary key auto_increment,
    first_name varchar (40) not null,
    last_name varchar (40) not null,
    foreign key(id) references user(id) on update cascade on DELETE cascade);

drop table if exists account;

create table if not exists account
(
    id    int primary key auto_increment,
    login    varchar(40) not null unique,
    password varchar(100) not null,
    is_active boolean default true,
    foreign key(id) references user(id) on update cascade on DELETE cascade);

drop table if exists role;
create table if not exists role
(
    id   int primary key,
    name varchar(40) not null unique,
    description varchar(100)
);

create table user_role
(
    user_id int,
    role_id int,
    primary key (user_id, role_id),
    foreign key (user_id) references user (id) ON DELETE CASCADE ON UPDATE CASCADE,
    foreign key (role_id) references role (id) ON DELETE CASCADE ON UPDATE CASCADE
);

drop view if exists user_account;

create view user_account as
select user.id, first_name, last_name, login, password
from user
         join account on account.id = user.id;

drop table if exists orders;

create table if not exists orders
(
    id    int primary key auto_increment,
    user_id int not  null,
    certificate_id int not null,
    purchase_date timestamp not null,
    amount double not null,
    quantity    int not null,
    foreign key(user_id) references user(id) on update cascade  on delete cascade,
    foreign key(user_id) references account(id)  on update cascade on delete cascade,
    foreign key(certificate_id) references certificate(id) on update cascade on delete cascade
);

drop view if exists order_full;
create view order_full as
select orders.id,
       user_account.id as user_id,
       first_name,
       last_name,
       login,
       password,
       c.id as certificate_id,
       c.name,
       c.price,
       c.description,
       c.duration,
       c.create_date,
       c.last_update_date,
       purchase_date,
       amount,
       quantity
from orders
         join user_account on orders.user_id = user_account.id
         join certificate as c on orders.certificate_id = c.id;

insert into
    user(first_name, last_name)  values ('Yury','Zmushko');
insert into
    user(first_name, last_name)  values ('Ivan','Ivanov');
insert into
    user(first_name, last_name)  values ('Petr','Petrov');
insert into
    user(first_name, last_name)  values ('Sidor','Sidorov');

insert into
    account(login, password) VALUES ('root','$2y$12$nDIbpnb/9S61LuSKF2JFt.AUHSESFw.xPwr/Ie6U6DvACFJuZACuq');
insert into
    account(login, password) VALUES ('Ivanov','$2y$12$nDIbpnb/9S61LuSKF2JFt.AUHSESFw.xPwr/Ie6U6DvACFJuZACuq');
insert into
    account(login, password) VALUES ('Petrov','$2y$12$nDIbpnb/9S61LuSKF2JFt.AUHSESFw.xPwr/Ie6U6DvACFJuZACuq');
insert into
    account(login, password) VALUES ('Sidorov','$2y$12$nDIbpnb/9S61LuSKF2JFt.AUHSESFw.xPwr/Ie6U6DvACFJuZACuq');

insert into
    role(id, name, description) VALUES (2,'USER', 'Regular user');
insert into
    role(id, name, description) VALUES (1,'ADMIN', 'Super admin');
insert into
    user_role(user_id, role_id) VALUES (1,1);
insert into
    user_role(user_id, role_id) VALUES (2,2);

insert into
    orders(user_id, certificate_id, purchase_date, amount, quantity) VALUES (2, 2, '2021-03-22 09:20:11', 150, 1);
insert into
    orders(user_id, certificate_id, purchase_date, amount, quantity) VALUES (3, 3, '2021-04-22 19:21:21', 122, 1);
insert into
    orders(user_id, certificate_id, purchase_date, amount, quantity) VALUES (4, 1, '2021-03-22 09:20:11', 33, 3);
insert into
    orders(user_id, certificate_id, purchase_date, amount, quantity) VALUES (3, 3, '2021-04-22 19:21:21', 12, 2);
insert into
    orders(user_id, certificate_id, purchase_date, amount, quantity) VALUES (1, 3, '2021-03-22 09:20:11', 10, 2);
insert into
    orders(user_id, certificate_id, purchase_date, amount, quantity) VALUES (4, 5, '2021-04-22 19:21:21', 11, 1);

create table if not exists audit
(
    id          int auto_increment
        primary key,
    user_id     int         not null,
    entity_id   int         not null,
    entity_name varchar(20) not null,
    operation   varchar(20) not null,
    date        timestamp   not null,
    foreign key (user_id) references user (id) on update cascade on delete cascade
);