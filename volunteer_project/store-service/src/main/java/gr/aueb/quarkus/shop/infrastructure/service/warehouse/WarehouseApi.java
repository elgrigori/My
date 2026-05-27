package gr.aueb.quarkus.shop.infrastructure.service.warehouse;

import gr.aueb.quarkus.shop.infrastructure.service.warehouse.representation.PurchaseOrderRepresentation;
import gr.aueb.quarkus.shop.infrastructure.service.warehouse.representation.StockReservationRepresentation;
import jakarta.enterprise.context.ApplicationScoped;

import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.core.Response;

import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

@Path("/api")
@ApplicationScoped
@RegisterRestClient(configKey="warehouse-api")
public interface WarehouseApi {

    @POST
    @Path("/orders/{orderId}/stock-reservations/")
    Response reserveProductStock(@PathParam("orderId") Long orderId,
            PurchaseOrderRepresentation purchaseOrderRepresentation);

}
