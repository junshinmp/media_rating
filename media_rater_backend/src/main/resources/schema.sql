create table if not exists user (
    user_id integer primary key autoincrement,
    username varchar(255) not null,
    first_name varchar(50),
    last_name varchar(50),
    password varchar(255) not null
);

create table if not exists rating (
    rating_id integer primary key autoincrement,
    media_id varchar(255) not null,
    stars int check (stars >= 1 and stars <= 5) not null,
    user_id integer not null,
    comments varchar(600),
);
