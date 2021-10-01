create TABLE users (
    id serial primary key ,
    name varchar(64) not null,
    email varchar(64) unique,
    password varchar(128) not null,
    active boolean
)