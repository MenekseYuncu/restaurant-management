create table if not exists product
(
    id          varchar(36)
        constraint pk__product__id primary key,
    category_id bigint
        constraint fk__product__category__id references category (id),
    name        varchar(300)   not null,
    ingredient  varchar(2048)  not null,
    price       numeric(50, 8) not null,
    status      varchar(20)    not null
        constraint p__product__status check (status in ('ACTIVE', 'INACTIVE')),
    extent      integer        not null,
    extent_type varchar(5)     not null check (extent_type IN ('ML', 'GR')),
    created_at  timestamp      not null,
    updated_at  timestamp
);

create table if not exists category
(
    id         bigserial
        constraint pk__category__id primary key,
    name       varchar(300) not null,
    status     varchar(20)  not null
        constraint c__category__status check (status in ('ACTIVE', 'INACTIVE')),
    created_at timestamp    not null,
    updated_at timestamp
);

create table if not exists dining_table
(
    id                  bigserial
        constraint pk__dining_table__id primary key,
    merge_id            varchar(36) not null,
    dining_table_status pg_enum     not null,
    created_at          timestamp   not null,
    updated_at          timestamp
);

create table if not exists orders
(
    id                    varchar(36)
        constraint pk__orders__id primary key,
    dining_table_merge_id varchar(36)    not null,
    order_status          pg_enum        not null,
    price                 numeric(50, 8) not null,
    created_at            timestamp(3)   not null,
    updated_at            timestamp
);

create table if not exists order_item
(
    id                varchar(36)
        constraint pk__order__id primary key,
    order_id          varchar(36)
        constraint fk__order_item__id references orders (id),
    product_id        varchar(36)
        constraint fk__product__id references product (id),
    price             numeric(50, 8) not null,
    order_item_status pg_enum        not null,
    created_at        timestamp(3)   not null,
    updated_at        timestamp
);

create table if not exists parameter
(
    id          bigserial
        constraint pk__parameter__id primary key,
    name        varchar(50) not null,
    description char(3),
    created_at  timestamp   not null,
    updated_at  timestamp
);

create type dining_table_status as enum ('VACANT', 'OCCUPİED', 'TAKING_ORDERS', 'RESERVED');
create type order_status as enum ('OPEN', 'IN_PROGRESS', 'COMPLETED', 'CANCELED');
create type order_item_status as enum ('PREPARİNG', 'READY', 'DELİVERED', 'CANCELED');


INSERT INTO product (id, category_id, name, ingredient, price, status, extent, extent_type, created_at)
VALUES ('0fe5d76a-99b6-11ea-bb37-0242ac130002', '1s2a376a-99b6-11ea-bb37-0242ac161589', 'pname', 'ingredient',
        20.3, 'ACTIVE', 200, 'ML', CURRENT_TIMESTAMP),
       ('e94186d1-8c52-4c57-b7c4-a5d5cfe2977c', '63c8c4b7-59af-476c-8295-30e4881132c1', 'pname2', 'ingredient',
        20.3, 'ACTIVE', 200, 'GR', CURRENT_TIMESTAMP);

INSERT INTO category(id, name, status, created_at)
VALUES ('1s2a376a-99b6-11ea-bb37-0242ac161589', 'cname', 'ACTIVE', CURRENT_TIMESTAMP),
       ('63c8c4b7-59af-476c-8295-30e4881132c1', 'cname2', 'ACTIVE', CURRENT_TIMESTAMP);

INSERT INTO parameter(id, name, description, created_at)
VALUES ('7c8afe20-7f49-4c1e-b044-81d975d940f5', 'name', 'TRY', CURRENT_TIMESTAMP);