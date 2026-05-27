package gr.aueb.quarkus.shop.application;

import gr.aueb.quarkus.shop.common.ResourceNotFoundException;
import gr.aueb.quarkus.shop.domain.customer.Customer;
import gr.aueb.quarkus.shop.domain.customer.CustomerRepository;
import gr.aueb.quarkus.shop.domain.cart.Cart;
import gr.aueb.quarkus.shop.domain.cart.CartRepository;
import gr.aueb.quarkus.shop.domain.purchase.CreditCard;
import gr.aueb.quarkus.shop.domain.purchase.Order;
import gr.aueb.quarkus.shop.domain.purchase.OrderRepository;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import java.util.Optional;

@RequestScoped
public class CartService {

    @Inject
    private CustomerRepository customerRepository;

    @Inject
    private CartRepository cartRepository;

    @Inject
    private OrderRepository orderRepository;

    @Inject
    private PaymentService paymentService;

    @Inject
    private WarehouseService warehouseService;

    @Transactional
    public Cart getCurrentCart(Long customerId){

        Optional<Customer> customer = customerRepository.findByCustomerId(customerId);
        if (customer.isEmpty()){
            return null; // better throw exception
        }
        Optional<Cart> cartResult = cartRepository.findCartWithItemsForCustomer(customer.get().getId());

        if (cartResult.isEmpty()){
            Cart cart = new Cart();
            cart.setCustomer(customer.get());
            cartRepository.save(cart);
            return cart;
        }
        cartResult.get().setCustomer(customer.get());
        return cartResult.get();
    }

    /**
     * Returns the id of the newly created order
     *
     * @param cartId
     * @param creditCard
     * @return
     */
    @Transactional
    public Long checkout(Long cartId, CreditCard creditCard){

        Cart cart = cartRepository.cartOfId(cartId)
                .orElseThrow(() -> new ResourceNotFoundException("Cart not found for given id"));

        Order order = cart.checkout();

        if (order != null) {
            orderRepository.save(order);
        }

        // FIXME: check results and update order status
        paymentService.pay(creditCard, order.getId());
        warehouseService.reserveStockFor(order);

        return order.getId();
    }

}
