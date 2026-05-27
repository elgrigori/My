-- This file allow to write SQL commands that will be emitted in test and dev.
-- The commands are commented as their support depends of the database
-- insert into myentity (id, field) values(1, 'field-1');
-- insert into myentity (id, field) values(2, 'field-2');
-- insert into myentity (id, field) values(3, 'field-3');
-- alter sequence myentity_seq restart with 4;

delete from stock_reservations;
delete from purchase_orders;
delete from products;

insert into products (id, name, sku, stock) values (200, 'Intel i7', 'CPU-01', 3);
insert into products (id, name, sku, stock) values (201, 'Intel i5', 'CPU-02', 2);
insert into products (id, name, sku, stock) values (202, 'DDR4 SO-DIMM 16GB', 'DDR-16-01', 3);
insert into products (id, name, sku, stock) values (203, 'DDR4 SO-DIMM 32GB', 'DDR-32-01', 2);

insert into purchase_orders(id, purchase_date) values (300, '20210101');

insert into stock_reservations(id, order_id, product_id, quantity) values(400, 300, 200, 2);
