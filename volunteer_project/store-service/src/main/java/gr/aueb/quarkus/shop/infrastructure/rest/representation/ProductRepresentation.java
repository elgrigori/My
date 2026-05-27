package gr.aueb.quarkus.shop.infrastructure.rest.representation;

import java.util.ArrayList;
import java.util.List;

import gr.aueb.quarkus.shop.domain.product.Product;
import io.quarkus.runtime.annotations.RegisterForReflection;

@RegisterForReflection
public class ProductRepresentation {

	public long id;

	/**
	 * FIXME: add additional fields as parts of ProductDetailsRepresentation for clients that require more details of the product
	 */
}
