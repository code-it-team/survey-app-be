create table if not exists survey_user(
    id uuid not null primary key,
    username varchar_ignorecase(50) not null unique,
    password varchar_ignorecase(50) not null,
    enabled boolean not null,
    user_role varchar_ignorecase(50) not null unique,
    constraint fk_survey_user_auth foreign key(user_role) references auth(role)
);

create table if not exists auth (
    id uuid not null primary key,
    role varchar_ignorecase(50) not null unique
);
