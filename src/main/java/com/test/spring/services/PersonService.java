package com.test.spring.services;

import com.test.spring.entities.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSourceUtils;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Arrays;
import java.util.List;

@Component
public class PersonService {
    @Autowired
    NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Autowired
    JdbcTemplate jdbcTemplate;

    SimpleJdbcInsert simpleJdbcInsert;

    @PostConstruct
    public void init(){
         simpleJdbcInsert= new SimpleJdbcInsert(jdbcTemplate);
    }
    public int addPerson(Person[] persons){
        SqlParameterSource[] batch = SqlParameterSourceUtils.createBatch(persons);
        int[] people = simpleJdbcInsert.withTableName("person").executeBatch(batch);
        return Arrays.stream(people).sum();
    }

    public List<Person> getPersonList(){
        return namedParameterJdbcTemplate.query("select firstname,lastname,address,company,email from person", BeanPropertyRowMapper.newInstance(Person.class));
    }
}
