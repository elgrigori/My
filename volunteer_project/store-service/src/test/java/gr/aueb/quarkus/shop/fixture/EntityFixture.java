package gr.aueb.quarkus.shop.fixture;

import gr.aueb.quarkus.shop.domain.customer.Customer;
import gr.aueb.quarkus.shop.domain.cart.Cart;
import gr.aueb.quarkus.shop.domain.purchase.CreditCard;
import gr.aueb.quarkus.shop.domain.purchase.Order;
import gr.aueb.quarkus.shop.domain.product.Product;

import java.time.LocalDate;

public class EntityFixture {

    public static class ValueFixture {
        public static CreditCard creditCard(){
            CreditCard creditCard = new CreditCard("John", "1234-1234",
                    "555", 2026, 12);
            return creditCard;
        }
    }

    public static class CartFixture {
        public static Cart cartWithItem(){
            Cart cart = emptyCart();
            Product product = ProductFixture.productWithId(200l);
            cart.addItem(product, 2);
            return cart;
        }

        public static Cart emptyCart(){
            Cart cart = new Cart();
            cart.setId(500l);
            cart.setCustomer(CustomerFixture.customerWithId(100l));
            return cart;
        }

    }
    public static class CustomerFixture {
        public static Customer customerWithId(Long id){
            Customer customer = new Customer();
            customer.setId(id);
            return customer;
        }
    }

    public static class ProductFixture {
        public static Product productWithSkuAndCost(String sku, double cost){
            Product  product = new Product("Product-"+ sku, cost, sku);
            product.setId(200l);
            return product;
        }

        public static Product productWithId(long id){
            Product product = new Product();
            product.setId(id);
            return product;
        }
    }

    public static class OrderFixture {
        public static Order newOrderWithSingleProduct(){
            Customer c1 = CustomerFixture.customerWithId(1000l);
            Product p = ProductFixture.productWithSkuAndCost("SKU-1", 100);

            Order o1 = new Order();
            o1.setCustomer(c1);
            o1.setOrderDate(LocalDate.of(2021, 1, 1));
            o1.addOrderLine(p, 2);
            return o1;
        }
    }
}
