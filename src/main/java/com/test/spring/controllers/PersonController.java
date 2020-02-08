package com.test.spring.controllers;

import com.test.spring.entities.Person;
import com.test.spring.services.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/person")
public class PersonController {

    @Autowired
    PersonService personService;

    @PostMapping("/add")
    public int addPerson(@RequestBody Person[] personList){
        return personService.addPerson(personList);
    }

    @GetMapping("/getPersons")
    public List<Person> getPersons(){
        return personService.getPersonList();
    }
}
