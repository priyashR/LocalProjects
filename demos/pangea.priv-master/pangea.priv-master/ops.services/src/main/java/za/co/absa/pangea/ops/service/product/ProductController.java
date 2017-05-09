/*********************************************
 * Copyright 2016 Absa ©
 * 02 Aug 2016
 * @author Eon van Tonder
 * @auther Nakedi Mabusela
 * @author Abigail Munzhelele
 * @author Jannie Pieterse
 * 
 * All rights reserved
 *********************************************/
package za.co.absa.pangea.ops.service.product;

import java.util.Collection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * The Class ProductController.
 */
@RestController
public class ProductController {

	private static Logger logger = LoggerFactory.getLogger(ProductController.class);
	
	@Autowired
	private ProductService productService;
	
	/**
	 * Products.
	 *
	 * @return the iterable
	 */
	@RequestMapping(value = "/products", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Iterable<ProductDTO> products() {
		Iterable<ProductDTO> products = productService.getProducts();
		if (products != null) {
			logger.debug("Products Retrieved : " + ((products instanceof Collection<?>) ? ((Collection<?>)products).size() : "unknown"));
		} else {
			logger.debug("Products Retrieved : NONE");
		}
		return products;
	}
}
