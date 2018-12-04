package com.test.spring.entities;

import org.springframework.data.cassandra.mapping.PrimaryKey;
import org.springframework.data.cassandra.mapping.Table;

import javax.persistence.Entity;
import javax.persistence.Id;

@Table("athletes")
public class Athletes {
    @PrimaryKey
    private int id;
    private int bronze;
    private String dob;
    private int gold;
    private double height;
    private String name;
    private String nationality;
    private String sex;
    private int silver;
    private String sport;
    private double weight;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getBronze() {
        return bronze;
    }

    public void setBronze(int bronze) {
        this.bronze = bronze;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public int getGold() {
        return gold;
    }

    public void setGold(int gold) {
        this.gold = gold;
    }

    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNationality() {
        return nationality;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public int getSilver() {
        return silver;
    }

    public void setSilver(int silver) {
        this.silver = silver;
    }

    public String getSport() {
        return sport;
    }

    public void setSport(String sport) {
        this.sport = sport;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    @Override
    public String toString() {
        return "Athletes{" +
                "id=" + id +
                ", bronze=" + bronze +
                ", dob='" + dob + '\'' +
                ", gold=" + gold +
                ", height=" + height +
                ", name='" + name + '\'' +
                ", nationality='" + nationality + '\'' +
                ", sex='" + sex + '\'' +
                ", silver=" + silver +
                ", sport='" + sport + '\'' +
                ", weight=" + weight +
                '}';
    }
}
