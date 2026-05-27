package gr.aueb.quarkus.shop.infrastructure.rest.representation;

import io.quarkus.runtime.annotations.RegisterForReflection;

import java.util.List;

@RegisterForReflection
public class OrderRepresentation {

	public long id;
	public String createdAt;
	public CustomerRepresentation customer;
	public List<OrderLineRepresentation> orderLines;


}
