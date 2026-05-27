package gr.aueb.quarkus.shop.infrastructure.service.warehouse;

import gr.aueb.quarkus.shop.application.WarehouseService;
import gr.aueb.quarkus.shop.common.BusinessRuleException;
import gr.aueb.quarkus.shop.domain.purchase.Order;
import gr.aueb.quarkus.shop.domain.purchase.OrderLine;
import gr.aueb.quarkus.shop.infrastructure.service.warehouse.representation.PurchaseOrderRepresentation;
import gr.aueb.quarkus.shop.infrastructure.service.warehouse.representation.StockReservationRepresentation;
import jakarta.annotation.Priority;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Alternative;
import jakarta.enterprise.inject.Default;
import jakarta.inject.Inject;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.jboss.logging.Logger;

import java.util.ArrayList;


@ApplicationScoped
public class WarehouseServiceImpl implements WarehouseService {

    @Inject
    Logger logger;

    @Inject
    @RestClient
    WarehouseApi warehouseApi;

    @Override
    public boolean reserveStockFor(Order order) {

        PurchaseOrderRepresentation purchaseOrder = new PurchaseOrderRepresentation();
        purchaseOrder.id = order.getId();
        purchaseOrder.stockReservations = new ArrayList<>();

        for(OrderLine orderLine: order.getOrderLines()){
            StockReservationRepresentation stockReservation = new StockReservationRepresentation();
            stockReservation.productId = orderLine.getProduct().getId();
            stockReservation.quantity = orderLine.getQuantity();
            purchaseOrder.stockReservations.add(stockReservation);
        }

        Response response = warehouseApi.reserveProductStock(order.getId(), purchaseOrder);
        if (!response.getStatusInfo().equals(Response.Status.NO_CONTENT)){
            throw new BusinessRuleException("Stock reservation failed for order id: " + order.getId());
        }
        return true;
    }
}
