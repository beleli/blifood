insert into tb_state (ds_name) values ('MG') ON CONFLICT DO NOTHING;
insert into tb_state (ds_name) values ('SP') ON CONFLICT DO NOTHING;

insert into tb_city (ds_name, id_state) values ('Uberlândia', 1) ON CONFLICT DO NOTHING;
insert into tb_city (ds_name, id_state) values ('São Paulo', 2) ON CONFLICT DO NOTHING;

insert into tb_culinary (ds_name) values ('Brasileira') ON CONFLICT DO NOTHING;
insert into tb_culinary (ds_name) values ('Italiana') ON CONFLICT DO NOTHING;

insert into tb_payment_method (ds_description) values ('Cartão de Crédito') ON CONFLICT DO NOTHING;
insert into tb_payment_method (ds_description) values ('Cartão de Débito') ON CONFLICT DO NOTHING;

insert into tb_restaurant (id_culinary, ds_name, vl_delivery_fee, dt_create, fl_active, fl_open, id_address_city, ds_address_zip_code, ds_address_street, ds_address_number, ds_address_complement, ds_address_district)
	values (1, 'Cozinha Caseira', 5, now(), true, true, 1, '38400000', 'Rua Afonso Pena', '1200', null, 'Centro') ON CONFLICT DO NOTHING;

insert into tb_restaurant (id_culinary, ds_name, vl_delivery_fee, dt_create, fl_active, fl_open, id_address_city, ds_address_zip_code, ds_address_street, ds_address_number, ds_address_complement, ds_address_district)
	values (2, 'Cantina Tarantela', 7, now(), true, true, 1, '38400000', 'Rua Afonso Pena', '1200', null, 'Centro') ON CONFLICT DO NOTHING;

insert into tb_restaurant_payment_method (id_restaurant, id_payment_method) values(1, 1) ON CONFLICT DO NOTHING;
insert into tb_restaurant_payment_method (id_restaurant, id_payment_method) values(1, 2) ON CONFLICT DO NOTHING;
insert into tb_restaurant_payment_method (id_restaurant, id_payment_method) values(2, 1) ON CONFLICT DO NOTHING;
insert into tb_restaurant_payment_method (id_restaurant, id_payment_method) values(2, 2) ON CONFLICT DO NOTHING;

insert into tb_product (id_restaurant, ds_name, ds_description, vl_price, fl_active)
	values (1, 'Galinhada Mineira', 'Galinha a moda mineira com tutu de feijão e vinagrete', 20, true) ON CONFLICT DO NOTHING;

insert into tb_product (id_restaurant, ds_name, ds_description, vl_price, fl_active)
	values (2, 'Macarrão a Carbonara', 'Espagette caseira a carbonara', 25, true) ON CONFLICT DO NOTHING;

insert into tb_user (ds_name, ds_email, ds_password, ds_type, dt_create)
	values ('Gerente', 'gerente@blifood.com.br', '$2a$10$.IwnX1InDcGjL54Xk9jdzuCW.BPddZ3LywmGJ39xh/QMftXKi1uoW', 'ADMIN', now()) ON CONFLICT DO NOTHING;

insert into tb_user (ds_name, ds_email, ds_password, ds_type, dt_create)
	values ('Cliente', 'cliente@blifood.com.br', '$2a$10$.IwnX1InDcGjL54Xk9jdzuCW.BPddZ3LywmGJ39xh/QMftXKi1uoW', 'CUSTOMER', now()) ON CONFLICT DO NOTHING;

insert into tb_order (cd_code, id_restaurant, id_payment_method, vl_subtotal, vl_delivery_fee, vl_total, id_address_city, ds_address_zip_code, ds_address_street, ds_address_number, ds_address_complement, ds_address_district, cd_status, dt_create, dt_confirm, dt_cancel, dt_delivery, id_user)
	values ('ddeb8e8e-6462-492f-8124-80fa0dcd261a', 1, 1, 20, 5, 25, 1, '38400000', 'Rua Afonso Pena', '1200', null, 'Centro', 'CREATED', now(), null, null, null, 1) ON CONFLICT DO NOTHING;

insert into tb_order_item (id_order, id_product, vl_amount, vl_unit_price, vl_total, ds_observation)
	values (1, 1, 1, 20, 20, 'Sem Pimenta') ON CONFLICT DO NOTHING;

insert into tb_order (cd_code, id_restaurant, id_payment_method, vl_subtotal, vl_delivery_fee, vl_total, id_address_city, ds_address_zip_code, ds_address_street, ds_address_number, ds_address_complement, ds_address_district, cd_status, dt_create, dt_confirm, dt_cancel, dt_delivery, id_user)
	values ('e259f8a5-1a2d-4d2b-b978-ccecfc6d3f2b', 2, 2, 25, 7, 32, 2, '38400000', 'Rua Afonso Pena', '1200', null, 'Centro', 'CREATED', now(), null, null, null, 2) ON CONFLICT DO NOTHING;

insert into tb_order_item (id_order, id_product, vl_amount, vl_unit_price, vl_total, ds_observation)
	values (2, 2, 1, 25, 25, null) ON CONFLICT DO NOTHING;