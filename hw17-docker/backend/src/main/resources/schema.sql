drop table if exists authors CASCADE;
create table if not exists authors (
    id bigserial,
    full_name varchar(255) NOT NULL,
    primary key (id)
);

drop table if exists genres CASCADE;
create table if not exists genres (
    id bigserial,
    name varchar(255) NOT NULL,
    primary key (id)
);

drop table if exists books CASCADE;
create table if not exists books (
    id bigserial,
    title varchar(255) NOT NULL,
    author_id bigint references authors (id) on delete cascade,
    primary key (id)
);

drop table if exists books_genres CASCADE;
create table if not exists books_genres (
    book_id bigint references books(id) on delete cascade,
    genre_id bigint references genres(id) on delete cascade,
    primary key (book_id, genre_id)
);

drop table if exists comments CASCADE;
create table if not exists comments (
    id bigserial,
    message varchar(255) NOT NULL,
    book_id bigint references books(id) on delete cascade,
    primary key (id)
);
