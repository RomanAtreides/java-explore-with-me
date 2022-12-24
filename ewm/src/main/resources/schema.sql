-- Users
CREATE TABLE IF NOT EXISTS users (
    id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    name VARCHAR(255) NOT NULL,
    email VARCHAR(512) NOT NULL,
    CONSTRAINT pk_user PRIMARY KEY (id),
    CONSTRAINT uq_user_email UNIQUE (email)
);

-- С категорией не должно быть связано ни одного события!
-- Categories
CREATE TABLE IF NOT EXISTS categories (
    id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    name VARCHAR(255) NOT NULL,
    CONSTRAINT pk_category PRIMARY KEY (id),
    CONSTRAINT uq_category_name UNIQUE (name)
)

-- Между событиями и подборками связь many-to-many:
-- Одно событие может участвовать в нескольких подборках
-- В одной подборке может быть несколько событий
-- Такая связь реализуется с помощью промежуточной таблицы (compilation_of_events)
-- которая содержит foreign key на таблицу compilations
-- и foreign key на таблицу events
-- В коде это всё реализуется в помощью аннотации many-to-many