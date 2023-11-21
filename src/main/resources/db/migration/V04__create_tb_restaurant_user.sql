create table tb_restaurant_user (
    id_restaurant bigint not null,
    id_user int not null
);
alter table tb_restaurant_user add constraint tb_restaurant_user_pk primary key (id_restaurant, id_user);
alter table tb_restaurant_user add constraint tb_restaurant_user_restaurant foreign key (id_restaurant) references tb_restaurant (id_restaurant);
alter table tb_restaurant_user add constraint tb_restaurant_user_user foreign key (id_user) references tb_user(id_user);