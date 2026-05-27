package gr.aueb.quarkus.shop.infrastructure.persistence;

import java.util.List;
import java.util.Optional;

import gr.aueb.quarkus.shop.domain.purchase.OrderRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.Query;

import gr.aueb.quarkus.shop.domain.purchase.Order;
import io.quarkus.hibernate.orm.panache.PanacheRepository;

@ApplicationScoped
public class OrderRepositoryImpl implements PanacheRepository<Order>, OrderRepository {
	
	@Override
	public List<Order> listWithOrderLines(){
		Query query = getEntityManager().createQuery("select o from Order o join fetch o.orderLines");
		List<Order> orders = query.getResultList();
		return orders;
	}

	@Override
	public Optional<Order> orderWithOrderLines(long orderId){

		Query query = getEntityManager()
				.createQuery("select o from Order o " +
						" left join fetch o.orderLines i" +
						" left join fetch i.product p" +
						" where o.id = :id");
		query.setParameter("id", orderId);

		List<Order> orders = query.getResultList();
		if (orders.isEmpty()){
			return Optional.empty();
		} else {
			return Optional.of(orders.get(0));
		}
	}

	@Override
	public void save(Order order){
		persist(order);
	}
	
}