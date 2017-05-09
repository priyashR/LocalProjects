package za.co.absa.pangea.ops.service.product;

import java.io.Serializable;

/**
 * @author Hannes
 * @since  11/11/2016
 */
public class ProductDTO implements Serializable {
	
	private static final long serialVersionUID = 6696876253385463931L;
	
	private Long id;
	private String description;
//	private Set<ProductTypeDTO> productTypes;
	
	public ProductDTO() {}
	
	public ProductDTO(Long id, String description) {
		this.id = id;
		this.description = description;
	}
		
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	
//	public Set<ProductTypeDTO> getProductTypes() {
//		return productTypes;
//	}
//
//	public void setProductTypes(Set<ProductTypeDTO> productTypes) {
//		this.productTypes = productTypes;
//	}
}