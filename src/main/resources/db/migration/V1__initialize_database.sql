create table if not exists rm_category
(
    id         bigserial
        constraint pk__rm_category__id primary key,
    name       varchar(300) not null,
    status     varchar(20)  not null
        constraint c__rm_category__status check (status in ('ACTIVE', 'INACTIVE')),
    created_at timestamp(0)    not null,
    updated_at timestamp(0)
);

create table if not exists rm_product
(
    id          varchar(36)
        constraint pk__rm_product__id primary key,
    category_id bigint
        constraint fk__rm_product__category__id references rm_category (id),
    name        varchar(300)   not null,
    ingredient  varchar(2048)  not null,
    price       numeric(50, 8) not null,
    status      varchar(20)    not null
        constraint c__rm_product__status check (status in ('ACTIVE', 'INACTIVE')),
    extent      integer        not null,
    extent_type varchar(5)     not null
        constraint c__rm_product__extent_type check (extent_type IN ('ML', 'GR')),
    created_at  timestamp(0)      not null,
    updated_at  timestamp(0)
);

create table if not exists rm_dining_table
(
    id                  bigserial
        constraint pk__rm_dining_table__id primary key,
    merge_id            varchar(36) not null,
    dining_table_status pg_enum     not null,
    size                int         not null,
    created_at          timestamp(0)   not null,
    updated_at          timestamp(0)
);

create table if not exists rm_order
(
    id                    varchar(36)
        constraint pk__rm_order__id primary key,
    dining_table_merge_id varchar(36)    not null,
    order_status          pg_enum        not null,
    price                 numeric(50, 8) not null,
    created_at            timestamp(3)   not null,
    updated_at            timestamp(3)
);

create table if not exists rm_order_item
(
    id                varchar(36)
        constraint pk__rm_order_item__id primary key,
    order_id          varchar(36)
        constraint fk__rm_order_item__order_id references rm_order (id),
    product_id        varchar(36)
        constraint fk__rm_order_item__product_id references rm_product (id),
    price             numeric(50, 8) not null,
    order_item_status pg_enum        not null,
    created_at        timestamp(3)   not null,
    updated_at        timestamp(3)
);

create table if not exists rm_parameter
(
    id         bigserial
        constraint pk__rm_parameter__id primary key,
    name       varchar(200) not null
 constraint u__rm_parameter_name unique,
    definition   char(3),
    created_at timestamp(0)   not null,
    updated_at timestamp(0)
);

create type dining_table_status as enum ('VACANT', 'OCCUPIED', 'TAKING_ORDERS', 'RESERVED');
create type order_status as enum ('OPEN', 'IN_PROGRESS', 'COMPLETED', 'CANCELED');
create type order_item_status as enum ('PREPARING', 'READY', 'DELIVERED', 'CANCELED');


insert into rm_category(name, status, created_at)
values ('category 1', 'ACTIVE', current_timestamp),
       ('category 2', 'ACTIVE', current_timestamp);

insert into rm_product (id, category_id, name, ingredient, price, status, extent, extent_type, created_at)
values ('0fe5d76a-99b6-11ea-bb37-0242ac130002', 1, 'product 1', 'ingredient', 20.3, 'ACTIVE', 200, 'ML',
        current_timestamp),
       ('e94186d1-8c52-4c57-b7c4-a5d5cfe2977c', 2, 'product 2', 'ingredient', 20.3, 'ACTIVE', 200, 'GR',
        current_timestamp);

insert into rm_parameter(name, definition, created_at)
values ('CURRENCY', 'TRY', current_timestamp);

