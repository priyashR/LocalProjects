package za.co.absa.pangea.ops.service.product;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

/**
 * @author hannes
 * @since  11/11/2016
 */

@Service
public class ProductService {
	
//	@Autowired
//	private ProductRepository productRepo;
		
	/**
	 * Retrieves all the products.
	 * @return a list of product DTOs.
	 */
	public List<ProductDTO> getProducts() {
//		List<Product> listOfAll = (List<Product>) productRepo.findAll();
		// Let's traverses the product structure to the 2nd level.
//		return ProductMapper.toDTOs(listOfAll, 2);

		List<ProductDTO> listOfProds = new ArrayList<ProductDTO>();
		listOfProds.add(new ProductDTO(101L,"Inwards BGI"));		
		listOfProds.add(new ProductDTO(111L,"Foreign Outwards BGI"));
		listOfProds.add(new ProductDTO(112L,"Local Outward BGI"));
		listOfProds.add(new ProductDTO(201L,"Export LC"));
		listOfProds.add(new ProductDTO(211L,"Import LC"));
		return listOfProds;
	}	
}
