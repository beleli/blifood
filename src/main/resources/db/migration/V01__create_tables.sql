create table tb_state (
    id_state serial not null,
    ds_name varchar(2) not null
);
alter table tb_state add constraint tb_state_pk primary key (id_state);
create unique index tb_state_ak01 on tb_state (ds_name);

create table tb_city (
    id_city serial not null,
    ds_name varchar(80) not null,
    id_state int not null
);
alter table tb_city add constraint tb_city_pk primary key (id_city);
alter table tb_city add constraint tb_city_state foreign key (id_state) references tb_state (id_state);
create unique index tb_city_ak01 on tb_city (ds_name, id_state);

create table tb_culinary (
    id_culinary serial not null,
    ds_name varchar(60) not null
);
alter table tb_culinary add constraint tb_culinary_pk primary key (id_culinary);
create unique index tb_culinary_ak01 on tb_culinary (ds_name);

create table tb_restaurant (
    id_restaurant bigserial not null,
    id_culinary int not null,
    ds_name varchar(80) not null,
    vl_delivery_fee decimal(10,2) not null,
    dt_create timestamp not null,
    dt_update timestamp,
    fl_active boolean not null,
    fl_open boolean not null,
    id_address_city bigint,
    ds_address_zip_code varchar(9),
    ds_address_street varchar(100),
    ds_address_number varchar(20),
    ds_address_complement varchar(60),
    ds_address_district varchar(60)
);
alter table tb_restaurant add constraint tb_restaurant_pk primary key (id_restaurant);
alter table tb_restaurant add constraint tb_restaurant_culinary foreign key (id_culinary) references tb_culinary (id_culinary);
alter table tb_restaurant add constraint tb_restaurant_city foreign key (id_address_city) references tb_city (id_city);
create unique index tb_restaurant_ak01 on tb_restaurant (ds_name, id_address_city);

create table tb_product (
    id_product bigserial not null,
    id_restaurant bigint not null,
    ds_name varchar(80) not null,
    ds_description text not null,
    vl_price decimal(10,2) not null,
    fl_active boolean not null
);
alter table tb_product add constraint tb_product_pk primary key (id_product);
alter table tb_product add constraint tb_product_restaurant foreign key (id_restaurant) references tb_restaurant (id_restaurant);
create unique index tb_product_ak01 on tb_product (id_restaurant, ds_name);

create table tb_payment_method (
    id_payment_method serial not null,
    ds_description varchar(60) not null
);
alter table tb_payment_method add constraint tb_payment_method_pk primary key (id_payment_method);
create unique index tb_payment_method_ak01 on tb_payment_method (ds_description);

create table tb_restaurant_payment_method (
    id_restaurant bigint not null,
    id_payment_method int not null
);
alter table tb_restaurant_payment_method add constraint tb_restaurant_payment_method_pk primary key (id_restaurant, id_payment_method);
alter table tb_restaurant_payment_method add constraint tb_restaurant_payment_method_restaurant foreign key (id_restaurant) references tb_restaurant (id_restaurant);
alter table tb_restaurant_payment_method add constraint tb_restaurant_payment_method_payment_method foreign key (id_payment_method) references tb_payment_method(id_payment_method);

create table tb_order (
    id_order bigserial not null,
    cd_code varchar(36) not null,
    id_restaurant bigint not null,
    id_payment_method bigint not null,
    vl_subtotal decimal(10,2) not null,
    vl_delivery_fee decimal(10,2) not null,
    vl_total decimal(10,2) not null,
    id_address_city bigint,
    ds_address_zip_code varchar(9),
    ds_address_street varchar(100),
    ds_address_number varchar(20),
    ds_address_complement varchar(60),
    ds_address_district varchar(60),
    cd_status varchar(10) not null,
    dt_create timestamp not null,
    dt_confirm timestamp,
    dt_cancel timestamp,
    dt_delivery timestamp
);
alter table tb_order add constraint tb_order_pk primary key (id_order);
alter table tb_order add constraint tb_order_restaurant foreign key (id_restaurant) references tb_restaurant (id_restaurant);
alter table tb_order add constraint tb_order_payment_method foreign key (id_payment_method) references tb_payment_method(id_payment_method);
alter table tb_order add constraint tb_order_city foreign key (id_address_city) references tb_city (id_city);
create unique index tb_order_ak01 on tb_order (cd_code);

create table tb_order_item (
    id_order_item bigserial not null,
    id_order bigint not null,
    id_product bigint not null,
    vl_amount smallint not null,
    vl_unit_price decimal(10,2) not null,
    vl_total decimal(10,2) not null,
    ds_observation varchar(255)
);
alter table tb_order_item add constraint tb_order_item_pk primary key (id_order_item);
alter table tb_order_item add constraint tb_order_item_order foreign key (id_order) references tb_order (id_order);
alter table tb_order_item add constraint tb_order_item_product foreign key (id_product) references tb_product (id_product);