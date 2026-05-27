package gr.aueb.quarkus.shop.domain.cart;

import java.util.List;
import java.util.Optional;

public interface CartRepository {
    List<Cart> listWithCartItems();

    Optional<Cart> findCartWithItemsForCustomer(long customerId);

    Optional<Cart> cartOfId(long cartId);

    void save(Cart cart);
}
