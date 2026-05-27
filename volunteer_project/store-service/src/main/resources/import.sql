-- This file allow to write SQL commands that will be emitted in test and dev.
-- The commands are commented as their support depends of the database
-- insert into myentity (id, field) values(1, 'field-1');
-- insert into myentity (id, field) values(2, 'field-2');
-- insert into myentity (id, field) values(3, 'field-3');
-- alter sequence myentity_seq restart with 4;

delete from cart_items;
delete from carts;

delete from order_lines;
delete from orders;
delete from customers;
delete from products;

insert into customers (id, email) values (100, 'bob@gmail.com');
insert into customers (id, email) values (101, 'mary@gmail.com');

insert into products (id, name, sku, cost) values (200, 'Intel i7', 'CPU-01', 300);
insert into products (id, name, sku, cost) values (201, 'Intel i5', 'CPU-02', 200);

insert into orders(id, created_at, customer_id, order_status) values (300, '20210101', 100, 'SUBMITTED');

insert into order_lines(id, order_id, product_id, quantity) values(400, 300, 200, 2);

insert into carts(id, customer_id) values (500, 101);

insert into cart_items(id, cart_id, product_id, quantity) values (600, 500, 201, 2);