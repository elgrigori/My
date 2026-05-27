package gr.aueb.quarkus.shop.infrastructure.rest.representation;

import io.quarkus.runtime.annotations.RegisterForReflection;

import java.util.List;

@RegisterForReflection
public class CartRepresentation {

	public long id;

	public CustomerRepresentation customer;
	public List<CartItemRepresentation> cartItems;


}
