create table tb_user (
    id_user bigserial not null,
    ds_name varchar(80) not null,
    ds_email varchar(255) not null,
    ds_password varchar(255) not null,
    ds_type varchar(64) not null,
    dt_create timestamp not null
);
alter table tb_user add constraint tb_user_pk primary key (id_user);
create unique index tb_user_ak01 on tb_user (ds_email);

alter table tb_order add column id_user bigint not null;
alter table tb_order add constraint tb_order_user foreign key (id_user) references tb_user (id_user);