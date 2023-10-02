create table tb_product_image (
    id_product bigint not null,
    ds_file_name varchar(150) not null,
    ds_description varchar(150),
    ds_content_type varchar(80) not null,
    vl_size int not null
);
alter table tb_product_image add constraint tb_product_image_pk primary key (id_product);
alter table tb_product_image add constraint tb_product_image_product foreign key (id_product) references tb_product (id_product);
