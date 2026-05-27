package gr.aueb.quarkus.shop.infrastructure.rest.representation;

import gr.aueb.quarkus.shop.domain.product.Product;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;

import java.util.HashSet;
import java.util.stream.Collectors;

@Mapper(componentModel = "jakarta",
        injectionStrategy = InjectionStrategy.CONSTRUCTOR,
imports = {HashSet.class, Collectors.class})
public abstract class ProductMapper {

    public abstract Product toModel(ProductRepresentation dto);

    public abstract ProductRepresentation toRepresentation(Product p);

}
