package com.test.spring.repositories;

import com.test.spring.entities.City;
import org.springframework.data.repository.CrudRepository;

public interface CityRepository extends CrudRepository<City,Integer>{
}
