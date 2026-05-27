package gr.aueb.quarkus.shop.infrastructure.rest.representation;

import gr.aueb.quarkus.shop.domain.cart.Cart;
import gr.aueb.quarkus.shop.domain.purchase.CreditCard;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;

import java.util.HashSet;
import java.util.stream.Collectors;

@Mapper(componentModel = "jakarta",
        injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public abstract class CreditCardMapper {
    public abstract CreditCard toModel(CreditCardRepresentation dto);

}
