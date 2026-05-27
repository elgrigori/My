package gr.aueb.quarkus.shop.infrastructure.rest.representation;

import gr.aueb.quarkus.shop.domain.customer.Customer;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;

import java.util.HashSet;
import java.util.stream.Collectors;

@Mapper(componentModel = "jakarta",
        injectionStrategy = InjectionStrategy.CONSTRUCTOR,
imports = {HashSet.class, Collectors.class})
public abstract class CustomerMapper {

    public abstract Customer toModel(CustomerRepresentation dto);

}
