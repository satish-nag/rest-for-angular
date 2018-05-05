package com.test.spring.entities;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.Objects;

@XmlRootElement
@Entity
public class City {
    @Id
    @GeneratedValue(generator = "city_id_seq",strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name = "city_id_seq",initialValue = 1,allocationSize = 1,sequenceName = "CITY_ID_SEQ")
    private int id;
    private String name;

    public City(){}
    public City(String name){this.name=name;}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        City city = (City) o;
        return id == city.id &&
                Objects.equals(name, city.name);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, name);
    }
}
