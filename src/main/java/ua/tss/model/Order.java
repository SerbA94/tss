package ua.tss.model;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import ua.tss.model.enums.DeliveryStatus;
import ua.tss.model.enums.PaymentStatus;


@Entity
@Table(name = "orders")
public class Order implements Serializable {
	
	private static final long serialVersionUID = -8538383382391252333L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	    
    @CreatedDate
	@Column(name = "date_created"/* , nullable = false, updatable = false */)
    private LocalDate dateCreated;
    
    @LastModifiedDate
	@Column(name = "date_updated"/* , nullable = false */)
    private LocalDate dateUpdated;
	
    @OneToMany(mappedBy = "pk.order")
    private List<OrderProduct> orderProducts = new ArrayList<>();
    
    @ManyToOne
    @JoinColumn(name="user_id")
    private User user;
    
	@ElementCollection(targetClass = DeliveryStatus.class, fetch = FetchType.EAGER)
	@CollectionTable(name = "order_deliveryStatus", joinColumns = @JoinColumn(name = "order_id"))
	@Enumerated(EnumType.STRING)
	private Set<DeliveryStatus> deliveryStatus = new LinkedHashSet<DeliveryStatus>();

	@ElementCollection(targetClass = PaymentStatus.class, fetch = FetchType.EAGER)
	@CollectionTable(name = "order_paymentStatus", joinColumns = @JoinColumn(name = "order_id"))
	@Enumerated(EnumType.STRING)
	private Set<PaymentStatus> paymentStatus = new LinkedHashSet<PaymentStatus>();
	
    
    @OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "deliveryDetails_deliveryDetailsId", referencedColumnName = "deliveryDetailsId")
	private DeliveryDetails deliveryDetails;
  
    
    @Transient
    public Double getTotalOrderPrice() {
        double sum = 0D;
        List<OrderProduct> orderProducts = getOrderProducts();
        for (OrderProduct op : orderProducts) {
            sum += op.getTotalPrice();
        }

        return sum;
    }
    
    @Transient
    public int getNumberOfProducts() {
        return this.orderProducts.size();
    }

	public List<OrderProduct> getOrderProducts() {
		return orderProducts;
	}

	public void setOrderProducts(List<OrderProduct> orderProducts) {
		this.orderProducts = orderProducts;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public LocalDate getDateCreated() {
		return dateCreated;
	}

	public void setDateCreated(LocalDate localDate) {
		this.dateCreated = localDate;
	}

	public LocalDate getDateUpdated() {
		return dateUpdated;
	}

	public void setDateUpdated(LocalDate dateUpdated) {
		this.dateUpdated = dateUpdated;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Set<DeliveryStatus> getDeliveryStatus() {
		return deliveryStatus;
	}

	public void setDeliveryStatus(Set<DeliveryStatus> deliveryStatus) {
		this.deliveryStatus = deliveryStatus;
	}

	public Set<PaymentStatus> getPaymentStatus() {
		return paymentStatus;
	}

	public void setPaymentStatus(Set<PaymentStatus> paymentStatus) {
		this.paymentStatus = paymentStatus;
	}

	public DeliveryDetails getDeliveryDetails() {
		return deliveryDetails;
	}

	public void setDeliveryDetails(DeliveryDetails deliveryDetails) {
		this.deliveryDetails = deliveryDetails;
	}
	
	

	
	

}
