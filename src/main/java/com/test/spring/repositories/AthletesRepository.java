package com.test.spring.repositories;

import com.test.spring.entities.Athletes;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AthletesRepository extends CrudRepository<Athletes,Integer>{
}
