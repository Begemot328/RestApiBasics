drop schema if exists certificates;

create schema if not exists certificates;

use
    certificates;

create table tag
(
    id   int primary key auto_increment,
    name varchar(40) not null unique,

    index certificate_id_index (id),
    index certificate_name_index (name)
);

create table certificate
(
    id               int primary key auto_increment,
    name             varchar(200) not null,
    description      text,
    price            double       not null,
    duration         int          not null,
    create_date      timestamp    not null,
    last_update_date timestamp    not null,

    index certificate_id_index (id),
    index certificate_price_index (price),
    index certificate_name_index (name)

);

create table certificate_tag
(
    tag_id         int,
    certificate_id int,
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

drop table if exists user;

create table if not exists user
(
    id         int primary key auto_increment,
    first_name varchar(40) not null,
    last_name  varchar(40) not null,
    index user_id_index (id),
    index user_first_name_index (first_name),
    index user_last_name_index (last_name)
);

drop table if exists account;

create table if not exists account
(
    id       int primary key auto_increment,
    login    varchar(40)  not null unique,
    password varchar(100) not null,
    is_active boolean default true,
    index user_id_index (id),
    index user_login_index (login),
    foreign key (id) references user (id) on update cascade on DELETE cascade
);

drop view if exists user_account;

create view user_account as
select user.id, first_name, last_name, login, password
from user
         join account on account.id = user.id;

drop table if exists orders;

create table if not exists orders
(
    id             int primary key auto_increment,
    user_id        int       not null,
    certificate_id int       not null,
    purchase_date  timestamp not null,
    amount         double    not null,
    quantity       int       not null,
    index order_id_index (id),
    index order_user_id_index (user_id),
    index order_certificate_id_index (certificate_id),
    index order_current_price_index (amount),
    index order_purchase_date_index (purchase_date),
    foreign key (user_id) references user (id) on update cascade on delete cascade,
    foreign key (user_id) references account (id) on update cascade on delete cascade,
    foreign key (certificate_id) references certificate (id) on update cascade on delete cascade
);

drop view if exists order_full;
create view order_full as
select orders.id,
       user_account.id as user_id,
       first_name,
       last_name,
       login,
       password,
       c.id            as certificate_id,
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