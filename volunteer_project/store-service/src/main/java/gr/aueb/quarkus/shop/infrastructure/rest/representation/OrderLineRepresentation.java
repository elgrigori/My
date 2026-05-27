package gr.aueb.quarkus.shop.infrastructure.rest.representation;

import io.quarkus.runtime.annotations.RegisterForReflection;

@RegisterForReflection
public class OrderLineRepresentation {

	public ProductRepresentation product;
	public int quantity;

}
