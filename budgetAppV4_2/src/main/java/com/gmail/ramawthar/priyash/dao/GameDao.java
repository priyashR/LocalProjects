package com.gmail.ramawthar.priyash.dao;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import com.gmail.ramawthar.priyash.model.Game1;
import org.springframework.stereotype.Repository;

@Repository
public class GameDao {
	@PersistenceContext
	private EntityManager em;

	public void persist(Game1 game) {
		em.persist(game);
	}

	public List<Game1> findAll() {
		return em.createQuery("SELECT name FROM Game1 game").getResultList();
	}
}
