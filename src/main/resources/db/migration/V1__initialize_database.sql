create table if not exists rm_category
(
    id         bigserial
        constraint pk__rm_category__id primary key,
    name       varchar(200) not null
        constraint u__rm_category__name unique,
    status     varchar(20)  not null
        constraint c__rm_category__status check (status in ('ACTIVE', 'INACTIVE', 'DELETED')),
    created_at timestamp(0) not null,
    updated_at timestamp(0)
);

create table if not exists rm_parameter
(
    id         bigserial
        constraint pk__rm_parameter__id primary key,
    name       varchar(200) not null
        constraint u__rm_parameter__name unique,
    definition char(3),
    created_at timestamp(0) not null,
    updated_at timestamp(0)
);

create table if not exists rm_product
(
    id          varchar(36)
        constraint pk__rm_product__id primary key,
    category_id bigint
        constraint fk__rm_product__category__id references rm_category (id),
    name varchar(300) not null
        constraint u__rm_product__name unique,
    ingredient  varchar(2048)  not null,
    price       numeric(50, 8) not null,
    status      varchar(20)    not null
        constraint c__rm_product__status check (status in ('ACTIVE', 'INACTIVE', 'DELETED')),
    extent      integer        not null,
    extent_type varchar(5)     not null
        constraint c__rm_product__extent_type check (extent_type IN ('ML', 'GR')),
    created_at  timestamp(0)   not null,
    updated_at  timestamp(0)
);

create table if not exists rm_dining_table
(
    id                  bigserial
        constraint pk__rm_dining_table__id primary key,
    merge_id            varchar(36)  not null,
    dining_table_status varchar(20)  not null
        constraint c__rm_dining_table_status check (dining_table_status in
                                                    ('VACANT', 'OCCUPIED', 'TAKING_ORDERS', 'RESERVED', 'DELETED')),
    size                int          not null,
    created_at          timestamp(0) not null,
    updated_at          timestamp(0)
);

create table if not exists rm_order
(
    id                    varchar(36)
        constraint pk__rm_order__id primary key,
    dining_table_merge_id varchar(36)    not null,
    order_status          varchar(15)     not null
        constraint c__rm_order__order_status check (order_status in ('OPEN', 'IN_PROGRESS', 'COMPLETED', 'CANCELED')),
    total_amount          numeric(50, 8) not null,
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
    quantity          int            not null,
    order_item_status varchar(10)     not null
        constraint c__rm_order_item__order_item_status check (order_item_status in ('PREPARING', 'READY', 'DELIVERED', 'CANCELED')),
    created_at        timestamp(3)   not null,
    updated_at        timestamp(3)
);

create table if not exists rm_payment
(
    id            varchar(36)
    constraint pk__rm_payment__id primary key,
    order_id      varchar(36) not null
    constraint fk__rm_payment__order_id references rm_order (id),
    payment_type  varchar(20) not null
    constraint c__rm_payment__payment_type check (payment_type in ('CASH', 'CREDIT_CARD', 'DEBIT_CARD', 'OTHER')),
    amount        numeric(50, 8) not null,
    status        varchar(20) not null
    constraint c__rm_payment__status check (status in ('PENDING', 'COMPLETED', 'FAILED', 'CANCELED')),
    created_at    timestamp(3) not null,
    updated_at    timestamp(3)
    );

create table if not exists rm_payment_item
(
    id              varchar(36)
    constraint pk__rm_payment_item__id primary key,
    payment_id      varchar(36) not null
    constraint fk__rm_payment_item__payment_id references rm_payment (id),
    order_item_id   varchar(36) not null
    constraint fk__rm_payment_item__order_item_id references rm_order_item (id),
    amount          numeric(50, 8) not null,
    created_at      timestamp(3) not null,
    updated_at      timestamp(3)
    );

insert into rm_category (name, status, created_at)
values ('category 1', 'ACTIVE', current_timestamp),
       ('category 2', 'ACTIVE', current_timestamp);


insert into rm_parameter (name, definition, created_at)
values ('CURRENCY', 'TRY', current_timestamp);

insert into rm_product (id, category_id, name, ingredient, price, status, extent, extent_type, created_at)
values (gen_random_uuid(), 1, 'product 1', 'ingredient', 20.3, 'ACTIVE', 200, 'ML',
        current_timestamp),
       (gen_random_uuid(), 2, 'product 2', 'ingredient', 20.3, 'ACTIVE', 200, 'GR',
        current_timestamp);
