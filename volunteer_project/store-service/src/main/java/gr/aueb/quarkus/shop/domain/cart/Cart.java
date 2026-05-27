package gr.aueb.quarkus.shop.domain.cart;

import gr.aueb.quarkus.shop.common.SystemDate;
import gr.aueb.quarkus.shop.domain.customer.Customer;
import gr.aueb.quarkus.shop.domain.product.Product;
import gr.aueb.quarkus.shop.domain.purchase.Order;
import gr.aueb.quarkus.shop.domain.purchase.OrderStatus;
import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Entity
@Table(name = "carts")
public class Cart {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne(cascade = {CascadeType.MERGE}, fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL, fetch = FetchType.LAZY,
            orphanRemoval = true)
    private Set<CartItem> cartItems = new HashSet<>();

    public void addItem(Product product, int quantity) {
        Optional<CartItem> cartItemResult = getItem(product);
        cartItemResult.ifPresentOrElse(item -> item.increaseQuantity(quantity), () -> {
            cartItems.add(new CartItem(product, quantity));
        });
    }

    public void removeItem(Product product) {

    }

    public Optional<CartItem> getItem(Product product) {
        return cartItems.stream()
                .filter(item -> item.getProduct().equals(product))
                .findFirst();
    }

    /**
     * Cart checkout and create order
     * @return
     */
    public Order checkout() {
        Order order = new Order();
        order.setOrderDate(SystemDate.now());
        order.setCustomer(this.customer);

        cartItems.stream()
                .forEach(item -> {
                    order.addOrderLine(item.getProduct(), item.getQuantity());
                });
        order.setStatus(OrderStatus.SUBMITTED);
        return order;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public Customer getCustomer() {
        return customer;
    }

    public Long getId() {
        return id;
    }

    public Set<CartItem> getCartItems() {
        return cartItems;
    }
}
