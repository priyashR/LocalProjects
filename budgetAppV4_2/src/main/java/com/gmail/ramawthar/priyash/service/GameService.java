package com.gmail.ramawthar.priyash.service;
import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import com.gmail.ramawthar.priyash.model.*;
import com.gmail.ramawthar.priyash.dao.*;
@Component
public class GameService {
	
	@Autowired
	private GameDao gameDao;

	@Transactional
	public void add(Game1 game) {
		gameDao.persist(game);
	}
	
	@Transactional
	public void addAll(Collection<Game1> games) {
		for (Game1 game : games) {
			gameDao.persist(game);
		}
	}

	@Transactional(readOnly = true)
	public List<Game1> listAll() {
		return gameDao.findAll();

	}
}

