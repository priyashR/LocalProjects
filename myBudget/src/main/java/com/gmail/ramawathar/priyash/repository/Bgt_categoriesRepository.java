package com.gmail.ramawathar.priyash.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.gmail.ramawathar.priyash.domain.Bgt_categories;
import com.gmail.ramawathar.priyash.domain.Bgt_user_third_party;

public interface Bgt_categoriesRepository extends CrudRepository<Bgt_categories, Long>{
	
	List<Bgt_categories> findByCategory(String category);

}
