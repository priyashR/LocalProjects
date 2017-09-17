package com.gmail.ramawthar.priyash.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.gmail.ramawthar.priyash.domain.Bgt_categories;

public interface Bgt_categoriesRepository extends CrudRepository<Bgt_categories, Long>{
	
	List<Bgt_categories> findByCategory(String category);

}
