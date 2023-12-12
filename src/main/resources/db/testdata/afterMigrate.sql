insert into tb_state (ds_name)
select 'MG'
where not exists(select id_state from tb_state where ds_name = 'MG');

insert into tb_state (ds_name)
select 'SP'
where not exists(select id_state from tb_state where ds_name = 'SP');

insert into tb_city (ds_name, id_state)
select 'Uberlândia', 1
where not exists(select id_city from tb_city where ds_name = 'Uberlândia' and id_state = 1);

insert into tb_city (ds_name, id_state)
select 'São Paulo', 2
where not exists(select id_city from tb_city where ds_name = 'São Paulo' and id_state = 2);

insert into tb_culinary (ds_name)
select 'Brasileira'
where not exists(select id_culinary from tb_culinary where ds_name = 'Brasileira');

insert into tb_culinary (ds_name)
select 'Italiana'
where not exists(select id_culinary from tb_culinary where ds_name = 'Italiana');

insert into tb_payment_method (ds_description)
select 'Cartão de Crédito'
where not exists(select id_payment_method from tb_payment_method where ds_description = 'Cartão de Crédito');

insert into tb_payment_method (ds_description)
select 'Cartão de Débito'
where not exists(select id_payment_method from tb_payment_method where ds_description = 'Cartão de Débito');

insert into tb_restaurant (id_culinary, ds_name, vl_delivery_fee, dt_create, fl_active, fl_open, id_address_city, ds_address_zip_code, ds_address_street, ds_address_number, ds_address_complement, ds_address_district)
select 1, 'Cozinha Caseira', 5, now(), true, true, 1, '38400000', 'Rua Afonso Pena', '1200', null, 'Centro'
where not exists(select id_restaurant from tb_restaurant where ds_name = 'Cozinha Caseira' and id_address_city = 1);

insert into tb_restaurant (id_culinary, ds_name, vl_delivery_fee, dt_create, fl_active, fl_open, id_address_city, ds_address_zip_code, ds_address_street, ds_address_number, ds_address_complement, ds_address_district)
select 2, 'Cantina Tarantela', 7, now(), true, true, 1, '38400000', 'Rua Afonso Pena', '1200', null, 'Centro'
where not exists(select id_restaurant from tb_restaurant where ds_name = 'Cantina Tarantela' and id_address_city = 1);

insert into tb_restaurant_payment_method (id_restaurant, id_payment_method) values(1, 1) on conflict do nothing;
insert into tb_restaurant_payment_method (id_restaurant, id_payment_method) values(1, 2) on conflict do nothing;
insert into tb_restaurant_payment_method (id_restaurant, id_payment_method) values(2, 1) on conflict do nothing;
insert into tb_restaurant_payment_method (id_restaurant, id_payment_method) values(2, 2) on conflict do nothing;

insert into tb_product (id_restaurant, ds_name, ds_description, vl_price, fl_active)
select 1, 'Galinhada Mineira', 'Galinha a moda mineira com tutu de feijão e vinagrete', 20, true
where not exists(select id_product from tb_product where id_restaurant = 1 and ds_name = 'Galinhada Mineira');

insert into tb_product (id_restaurant, ds_name, ds_description, vl_price, fl_active)
select 2, 'Macarrão a Carbonara', 'Espagette caseira a carbonara', 25, true
where not exists(select id_product from tb_product where id_restaurant = 2 and ds_name = 'Macarrão a Carbonara');

insert into tb_user (ds_name, ds_email, ds_password, ds_type, dt_create)
select 'Gerente', 'gerente@blifood.com.br', '$2a$10$.IwnX1InDcGjL54Xk9jdzuCW.BPddZ3LywmGJ39xh/QMftXKi1uoW', 'ADMIN', now()
where not exists(select id_user from tb_user where ds_email = 'gerente@blifood.com.br');

insert into tb_user (ds_name, ds_email, ds_password, ds_type, dt_create)
select 'Cliente', 'cliente@blifood.com.br', '$2a$10$.IwnX1InDcGjL54Xk9jdzuCW.BPddZ3LywmGJ39xh/QMftXKi1uoW', 'CUSTOMER', now()
where not exists(select id_user from tb_user where ds_email = 'cliente@blifood.com.br');

insert into tb_restaurant_user (id_restaurant, id_user) values(1, 1) on conflict do nothing;
insert into tb_restaurant_user (id_restaurant, id_user) values(2, 1) on conflict do nothing;

insert into tb_order (cd_code, id_restaurant, id_payment_method, vl_subtotal, vl_delivery_fee, vl_total, id_address_city, ds_address_zip_code, ds_address_street, ds_address_number, ds_address_complement, ds_address_district, cd_status, dt_create, dt_confirm, dt_cancel, dt_delivery, id_user)
select 'ddeb8e8e-6462-492f-8124-80fa0dcd261a', 1, 1, 20, 5, 25, 1, '38400000', 'Rua Afonso Pena', '1200', null, 'Centro', 'CREATED', now(), null, null, null, 1
where not exists(select cd_code from tb_order where cd_code = 'ddeb8e8e-6462-492f-8124-80fa0dcd261a');

insert into tb_order (cd_code, id_restaurant, id_payment_method, vl_subtotal, vl_delivery_fee, vl_total, id_address_city, ds_address_zip_code, ds_address_street, ds_address_number, ds_address_complement, ds_address_district, cd_status, dt_create, dt_confirm, dt_cancel, dt_delivery, id_user)
select 'e259f8a5-1a2d-4d2b-b978-ccecfc6d3f2b', 2, 2, 25, 7, 32, 2, '38400000', 'Rua Afonso Pena', '1200', null, 'Centro', 'CREATED', now(), null, null, null, 2
where not exists(select cd_code from tb_order where cd_code = 'e259f8a5-1a2d-4d2b-b978-ccecfc6d3f2b');

insert into tb_order_item (id_order, id_product, vl_amount, vl_unit_price, vl_total, ds_observation)
select 1, 1, 1, 20, 20, 'Sem Pimenta'
where not exists(select id_order_item from tb_order_item where id_order = 1 and id_product = 1);

insert into tb_order_item (id_order, id_product, vl_amount, vl_unit_price, vl_total, ds_observation)
select 2, 2, 1, 25, 25, null
where not exists(select id_order_item from tb_order_item where id_order = 2 and id_product = 2);
