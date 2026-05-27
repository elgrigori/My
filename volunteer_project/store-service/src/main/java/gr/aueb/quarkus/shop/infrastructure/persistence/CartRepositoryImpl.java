package gr.aueb.quarkus.shop.infrastructure.persistence;

import gr.aueb.quarkus.shop.domain.cart.Cart;
import gr.aueb.quarkus.shop.domain.cart.CartRepository;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.Query;

import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class CartRepositoryImpl implements PanacheRepository<Cart>, CartRepository {
	
	@Override
	public List<Cart> listWithCartItems(){
		Query query = getEntityManager()
				.createQuery("select c from Cart c join fetch c.cartItems");
		List<Cart> cart = query.getResultList();
		return cart;
	}

	@Override
	public Optional<Cart> findCartWithItemsForCustomer(long customerId){

		Query query = getEntityManager()
				.createQuery("select c from Cart c " +
						" join fetch c.cartItems i" +
						" join fetch i.product p" +
						" where customer.id = :id");
		query.setParameter("id", customerId);

		List<Cart> carts = query.getResultList();
		if (carts.isEmpty()){
			return Optional.empty();
		} else {
			return Optional.of(carts.get(0));
		}
	}

	@Override
	public Optional<Cart> cartOfId(long cartId){
		return findByIdOptional(cartId);
	}

	@Override
	public void save(Cart cart){
		persist(cart);
	}
	
}