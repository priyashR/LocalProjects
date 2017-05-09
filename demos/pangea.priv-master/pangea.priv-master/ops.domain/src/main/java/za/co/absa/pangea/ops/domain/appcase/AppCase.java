package za.co.absa.pangea.ops.domain.appcase;

import java.io.Serializable;
import javax.persistence.*;

/**
 * @author hannes
 * @since 02/02/2017
 */
@Entity
@Table(name="APPCASE")
public class AppCase implements Serializable {
	
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="APPCASE_SEQ")
	@SequenceGenerator(name="APPCASE_SEQ", sequenceName="APPCASE_SEQ")
	@Column(name = "ID", nullable = false)
	private Long id;
	
	@Column(name = "CUSTOMER_NAME")
	private String customerName;
	
	@Column(name = "CUSTOMER_ACCOUNTNUMBER")
	private Long customerAccountNumber;
	
	@Column(name = "STEP_CODE")
	private String stepCode;
	
	@Column(name = "PRODUCT_ID")
	private Long productId;
	
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public Long getCustomerAccountNumber() {
		return customerAccountNumber;
	}

	public void setCustomerAccountNumber(Long customerAccountNumber) {
		this.customerAccountNumber = customerAccountNumber;
	}

	public String getStepCode() {
		return stepCode;
	}

	public void setStepCode(String stepCode) {
		this.stepCode = stepCode;
	}

	public Long getProductId() {
		return productId;
	}

	public void setProductId(Long productId) {
		this.productId = productId;
	}
}