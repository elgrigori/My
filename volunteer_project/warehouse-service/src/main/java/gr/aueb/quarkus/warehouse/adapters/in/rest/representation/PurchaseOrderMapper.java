package gr.aueb.quarkus.warehouse.adapters.in.rest.representation;

import gr.aueb.quarkus.warehouse.application.domain.PurchaseOrder;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Named;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Mapper(componentModel = "jakarta",
        injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public abstract class PurchaseOrderMapper {


    public abstract PurchaseOrder toModel(PurchaseOrderRepresentation dto);


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
