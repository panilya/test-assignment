create table customer(
    id bigserial not null,
    object_id varchar(255) not null,
    first_name varchar(255),
    last_name varchar(255),
    phone_number varchar(255),
    email varchar(255),
    app_name varchar(255),
    created_at timestamp with time zone default now(),
    primary key (id)
);