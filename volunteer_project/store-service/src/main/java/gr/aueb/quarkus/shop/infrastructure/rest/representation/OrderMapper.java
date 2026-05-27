package gr.aueb.quarkus.shop.infrastructure.rest.representation;

import gr.aueb.quarkus.shop.domain.purchase.Order;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.stream.Collectors;

@Mapper(componentModel = "jakarta",
        injectionStrategy = InjectionStrategy.CONSTRUCTOR,
imports = {HashSet.class, Collectors.class})
public abstract class OrderMapper {

    @Mapping(target = "orderDate", source = "createdAt", qualifiedByName = "dateParser")
    public abstract Order toModel(OrderRepresentation dto);

    @Mapping(target = "createdAt", source = "orderDate", qualifiedByName = "dateFormatter")
    public abstract OrderRepresentation toRepresentation(Order model);

    @Named("dateFormatter")
    public String formatDate(LocalDate date){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        return date.format(formatter);
    }

    @Named("dateParser")
    public LocalDate parseDate(String dateString){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        LocalDate date = formatter.parse(dateString, LocalDate::from);
        return date;
    }

}
