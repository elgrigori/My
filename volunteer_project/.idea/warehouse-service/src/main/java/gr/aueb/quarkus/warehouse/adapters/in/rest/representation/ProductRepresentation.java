package gr.aueb.quarkus.warehouse.adapters.in.rest.representation;

import java.util.ArrayList;
import java.util.List;

import gr.aueb.quarkus.warehouse.application.domain.Product;



public class ProductRepresentation {

	private long id;
	private String sku;
	private String name;
	private Integer stock;

	public ProductRepresentation() {

	}

	public ProductRepresentation(String sku) {
		super();
		this.sku = sku;
	}

	public static ProductRepresentation from(Product p) {
		ProductRepresentation dto = new ProductRepresentation();
		dto.id = p.getId();
		dto.name = p.getName();
		dto.sku = p.getSku();
		dto.stock = p.getStock();
		return dto;
	}

	public static List<ProductRepresentation> from(List<Product> list) {

		List<ProductRepresentation> dtoList = new ArrayList<>();
		for (Product p : list) {
			dtoList.add(from(p));
		}

		return dtoList;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getSku() {
		return sku;
	}

	public void setSku(String sku) {
		this.sku = sku;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getStock() {
		return stock;
	}

	public void setStock(Integer stock) {
		this.stock = stock;
	}

	

}
