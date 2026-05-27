package gr.aueb.quarkus.shop.infrastructure.service.warehouse;

import gr.aueb.quarkus.shop.infrastructure.service.warehouse.representation.PurchaseOrderRepresentation;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Alternative;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.rest.client.inject.RestClient;

@Alternative
@RestClient
@ApplicationScoped
public class WarehouseApiStub implements WarehouseApi {

    @Override
    public Response reserveProductStock(Long orderId, PurchaseOrderRepresentation purchaseOrderRepresentation) {
        return Response.noContent().build();
    }
}
