create table pending_registration(id serial not null primary key, user_id integer not null, pin integer not null);
create table users(id serial not null primary key, email varchar(255) not null, nickname varchar(64) not null);
