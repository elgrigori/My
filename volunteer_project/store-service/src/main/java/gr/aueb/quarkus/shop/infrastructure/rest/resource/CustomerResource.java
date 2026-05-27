package gr.aueb.quarkus.shop.infrastructure.rest.resource;

import gr.aueb.quarkus.shop.application.CartService;
import gr.aueb.quarkus.shop.domain.cart.Cart;
import gr.aueb.quarkus.shop.infrastructure.rest.ApiPath.Root;
import gr.aueb.quarkus.shop.infrastructure.rest.representation.CartMapper;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.jboss.logging.Logger;

@RequestScoped
@Path(Root.CUSTOMERS)
public class CustomerResource {

	@Inject
	Logger logger;

    @Inject
    CartService cartService;

    @Inject
    CartMapper cartMapper;


    @GET
	@Path("/{customerId}/cart")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getCurrentCart(@PathParam("customerId") Long customerId){

        Cart cart = cartService.getCurrentCart(customerId);
        if (cart == null){
            return Response.status(Response.Status.NOT_FOUND).build();
        }

		return Response.ok(cartMapper.toRepresentation(cart)).build();
    }


}
