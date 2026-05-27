package gr.aueb.quarkus.warehouse.adapters.out;

import java.util.ArrayList;
import java.util.List;

import gr.aueb.quarkus.warehouse.application.ports.out.ProductRepository;
import jakarta.enterprise.context.ApplicationScoped;

import gr.aueb.quarkus.warehouse.application.domain.Product;
import io.quarkus.hibernate.orm.panache.PanacheRepository;


@ApplicationScoped
public class ProductRepositoryImpl implements PanacheRepository<Product>, ProductRepository {

    @Override
    public List<Product> findBySKU(List<String> skuList) {
        if (skuList.size() == 0) {
            return new ArrayList<>();
        }
        return list("sku in ( ?1 )", skuList);
    }
}


