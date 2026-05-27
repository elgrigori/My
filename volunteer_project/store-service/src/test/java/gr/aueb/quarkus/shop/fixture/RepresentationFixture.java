package gr.aueb.quarkus.shop.fixture;

import gr.aueb.quarkus.shop.infrastructure.rest.representation.CustomerRepresentation;
import gr.aueb.quarkus.shop.infrastructure.rest.representation.OrderLineRepresentation;
import gr.aueb.quarkus.shop.infrastructure.rest.representation.OrderRepresentation;
import gr.aueb.quarkus.shop.infrastructure.rest.representation.ProductRepresentation;

import java.util.ArrayList;

public class RepresentationFixture {


    public static class OrderFixture {

        /**
         * Customer with id 1500 and email mike@gmail.com
         * Two items for product 2000
         * @return
         */
        public static OrderRepresentation orderWithSingleProduct(){
            OrderRepresentation orderRepresentation = new OrderRepresentation();
            orderRepresentation.createdAt = "20210101";
            orderRepresentation.customer = CustomerFixture.customer(1500);
            orderRepresentation.orderLines = new ArrayList<>();
            orderRepresentation.orderLines.add(OrderLineFixture.twoItemsForProduct(200));
            return orderRepresentation;
        }
    }

    public static class CustomerFixture {

        public static CustomerRepresentation customer(long id){
            CustomerRepresentation customerRepresentation = new CustomerRepresentation();
            customerRepresentation.id = id;
            return customerRepresentation;
        }
    }

    public static class OrderLineFixture {

        public static OrderLineRepresentation twoItemsForProduct(long productId){
            OrderLineRepresentation representation = new OrderLineRepresentation();
            representation.product = ProductFixture.productWithId(productId);
            representation.quantity = 2;
            return representation;
        }

    }

    public static class ProductFixture {

        public static ProductRepresentation productWithId(long id){
            ProductRepresentation productRepresentation = new ProductRepresentation();
            productRepresentation.id = id;
            return productRepresentation;
        }

    }


}
