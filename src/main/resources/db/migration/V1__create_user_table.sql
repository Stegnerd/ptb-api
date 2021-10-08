create TABLE users (
    id serial primary key ,
    name varchar(64) not null,
    trainer_name varchar(32) not null,
    email varchar(64) unique,
    password varchar(128) not null,
    active boolean default true,
    created_utc timestamp not null default timezone('utc', now()),
    updated_utc timestamp not null default timezone('utc', now())
)