package gr.aueb.quarkus.warehouse.adapters.in.rest;

import gr.aueb.quarkus.warehouse.adapters.in.rest.representation.PurchaseOrderMapper;
import gr.aueb.quarkus.warehouse.adapters.in.rest.representation.PurchaseOrderRepresentation;
import gr.aueb.quarkus.warehouse.application.ports.in.StockReservationUseCase;
import gr.aueb.quarkus.warehouse.application.domain.PurchaseOrder;
import jakarta.inject.Inject;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Response;
import org.jboss.logging.Logger;

@Path(ApiPath.ROOT.ORDERS)
public class PurchaseOrderResource {

    @Inject
    Logger logger;

    @Inject
    PurchaseOrderMapper purchaseOrderMapper;

    @Inject
    StockReservationUseCase stockReservationUseCase;

    @POST
    @Path("/{orderId}/stock-reservations")
    public Response stockReservation(PurchaseOrderRepresentation dto){

        PurchaseOrder purchaseOrder = purchaseOrderMapper.toModel(dto);
        boolean result = stockReservationUseCase.reserveStock(purchaseOrder);
        if (!result) {
            return Response.status(409).build();
        }
        logger.info("Reserved stock for order " + dto.id);
        return Response.noContent().build();

    }
}
