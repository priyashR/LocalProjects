package za.co.absa.pangea.ops.service.appcase;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import za.co.absa.pangea.ops.PangeaServiceApplication;

/**
 * @author hannes
 * @since 08/02/2016
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = PangeaServiceApplication.class)
public class AppCaseServiceTest {
	
	@Autowired
	private AppCaseService serv;

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
	}

	/**
	 * Test method for {@link za.co.absa.pangea.ops.service.appcase.AppCaseService#save(java.lang.Long, za.co.absa.pangea.ops.service.appcase.AppCaseDTO)}.
	 */
	@Test
	public void testSave() {
		AppCaseDTO dtoToSave = new AppCaseDTO();
		dtoToSave.setId(null);
		dtoToSave.setCustomerName("Phteven");
		dtoToSave.setCustomerAccountNumber(9898989898L);
		
		AppCaseDTO savedDTO = serv.save(null, dtoToSave);
		
		assertNotNull(savedDTO);
		assertNotNull(savedDTO.getId());
		System.out.println("ID=["+savedDTO.getId()+"]");
		assertEquals("Phteven", savedDTO.getCustomerName());
		assertEquals(new Long(9898989898L), savedDTO.getCustomerAccountNumber());
	}

}
