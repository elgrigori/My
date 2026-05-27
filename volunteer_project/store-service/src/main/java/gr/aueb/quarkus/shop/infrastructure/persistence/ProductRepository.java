package gr.aueb.quarkus.shop.infrastructure.persistence;

import jakarta.enterprise.context.ApplicationScoped;

import gr.aueb.quarkus.shop.domain.product.Product;
import io.quarkus.hibernate.orm.panache.PanacheRepository;

@ApplicationScoped
public class ProductRepository implements PanacheRepository<Product> {

   // put your custom logic here as instance methods

	/*
	 * public Person findByName(String name){ return find("name",
	 * name).firstResult(); }
	 * 
	 * public List<Person> findAlive(){ return list("status", Status.Alive); }
	 * 
	 * public void deleteStefs(){ delete("name", "Stef"); }
	 */
}