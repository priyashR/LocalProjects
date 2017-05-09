package za.co.absa.pangea.ops.service.appcase;

public class AppCaseDTO {
	
	private Long id;
	private String stepCode;
	private Long productId;
	private String customerName;
	private Long customerAccountNumber;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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

	public void setProductId(Long product) {
		this.productId = product;
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
}
