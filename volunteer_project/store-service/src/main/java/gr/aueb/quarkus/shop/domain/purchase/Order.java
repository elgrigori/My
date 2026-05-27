package gr.aueb.quarkus.shop.domain.purchase;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import gr.aueb.quarkus.shop.domain.customer.Customer;
import gr.aueb.quarkus.shop.domain.product.Product;
import jakarta.persistence.*;

@Entity
@Table(name = "orders")
public class Order {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	@Column(name = "created_at")
	private LocalDate orderDate;
	@ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE},
			fetch = FetchType.EAGER)
	@JoinColumn(name = "customer_id")
	private Customer customer;
	
	@OneToMany(mappedBy = "order", cascade = CascadeType.ALL, fetch = FetchType.LAZY, 
			orphanRemoval = true)
	private Set<OrderLine> orderLines = new HashSet<>();
	
	@Enumerated(EnumType.STRING)
	@Column(name = "order_status")
	private OrderStatus status = OrderStatus.SUBMITTED;

	@Embedded
	private Payment payment;
	
	public LocalDate getOrderDate() {
		return orderDate;
	}

	public void setOrderDate(LocalDate orderDate) {
		this.orderDate = orderDate;
	}

	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	public Long getId() {
		return id;
	}

	public Set<OrderLine> getOrderLines() {
		return orderLines;
	}

	public void addOrderLine(Product p, int quantity) {
		if (p == null || quantity <= 0) {
			return;
		}
		
		OrderLine orderLine = new OrderLine(p, quantity);
		orderLines.add(orderLine);
		orderLine.setOrder(this);
	}

	public OrderStatus getStatus() {
		return status;
	}

	public void setStatus(OrderStatus status) {
		this.status = status;
	}

	public void setPayment(Payment payment) {
		this.payment = payment;
	}

	public Payment getPayment() {
		return payment;
	}
}
