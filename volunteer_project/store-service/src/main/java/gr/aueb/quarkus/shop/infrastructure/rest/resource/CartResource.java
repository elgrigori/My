package gr.aueb.quarkus.shop.infrastructure.rest.resource;

import gr.aueb.quarkus.shop.application.CartService;
import gr.aueb.quarkus.shop.domain.purchase.CreditCard;
import gr.aueb.quarkus.shop.infrastructure.rest.ApiPath.Root;
import gr.aueb.quarkus.shop.infrastructure.rest.representation.*;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;
import org.jboss.logging.Logger;

import java.net.URI;

@RequestScoped
@Path(Root.CARTS)
public class CartResource {

	@Inject
	Logger logger;

	@Inject
	CartMapper cartMapper;

	@Inject
	CartService cartService;

	@Inject
	CreditCardMapper creditCardMapper;

	@Context
	UriInfo uriInfo;

    @GET
	@Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getCartForId(@PathParam("id") Long cartId){
		return Response.serverError().build();
    }

	@POST
	@Path("/{id}/checkout")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response cartCheckout(@PathParam("id") Long cartId,
								 CreditCardRepresentation dto){

		logger.info("Checking out cart " + cartId);

		CreditCard creditCard = creditCardMapper.toModel(dto);
		Long orderId = cartService.checkout(cartId, creditCard);

		URI resourceURI = uriInfo
				.getBaseUriBuilder().fragment(Root.ORDERS)
				.fragment(Long.toString(orderId)).build();
		return Response.created(resourceURI).build();
	}

}
