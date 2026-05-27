package gr.aueb.quarkus.warehouse.application.ports.out;

import gr.aueb.quarkus.warehouse.application.domain.Product;

import java.util.List;

public interface ProductRepository {
    List<Product> findBySKU(List<String> skuList);
}
