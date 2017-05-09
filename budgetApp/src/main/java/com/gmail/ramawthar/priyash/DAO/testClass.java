package com.gmail.ramawthar.priyash.DAO;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;


public class testClass {
    private static final EntityManagerFactory ENTITY_MANAGER_FACTORY = Persistence
            .createEntityManagerFactory("budgetApp");
	public static void main(String[] args) {
		// TODO Auto-generated method stub
        // Create an EntityManager
        EntityManager manager = ENTITY_MANAGER_FACTORY.createEntityManager();
        EntityTransaction transaction = null;

		
		try {
            // Get a transaction
            transaction = manager.getTransaction();
            // Begin the transaction
            transaction.begin();

        	System.out.println(1);/*
//-----------------------------------------------------------------------------------        	
            // Create a new Bank object
    		TRXN_bank t = new TRXN_bank(); 
    		//t.setBank_id(2);
    		t.setBank_name("PRI");

        	System.out.println(3);
            // Save the student object
            manager.persist(t);
//-----------------------------------------------------------------------------------            
*/
//-----------------------------------------------------------------------------------        	
            // Create a new SMS object
    		Original_SMS t = new Original_SMS(); 
    		//t.setBank_id(2);
    		t.setOriginal_SMS("Absa: CHEQ4993, Pmnt, 25/11/16 SETTLEMENT/C - IBANK PAYMENT TO, ABSA BANK Ari, R-450.00, Available R9,176.54. Help 0860008600; RAMAWPR001");

        	System.out.println(3);
            // Save the student object
            manager.persist(t);
//-----------------------------------------------------------------------------------            

        	
        	
        	System.out.println(4);
            // Commit the transaction
            transaction.commit();
        	System.out.println(5);
        } catch (Exception ex) {
        	System.out.println(ex);
            // If there are any exceptions, roll back the changes
            if (transaction != null) {
                transaction.rollback();
            }
            // Print the Exception
            ex.printStackTrace();
        } finally {
            // Close the EntityManager
            manager.close();
        }
		
	}

}
