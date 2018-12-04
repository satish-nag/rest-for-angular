package com.test.spring.entities;

import org.hibernate.Session;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Entity
@XmlRootElement
public class Product {
    @Id
    @GeneratedValue(generator = "prod_id_seq",strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name = "prod_id_seq",initialValue = 1,allocationSize = 1,sequenceName = "PRODUCT_ID_SEQ")
    public int productId;
    public String productName;
    public String productType;
    public int price;
    public String seller;
    @ManyToMany(fetch = FetchType.LAZY)
    @Fetch(FetchMode.JOIN)
    public List<City> availableCities;

    public Product(){}
    public Product(String productName, String productType, int price, String seller, List<City> availableCities){
        Session session = null;
        this.productName = productName;
        this.productType = productType;
        this.price = price;
        this.seller = seller;
        this.availableCities = availableCities;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductType() {
        return productType;
    }

    public void setProductType(String productType) {
        this.productType = productType;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getSeller() {
        return seller;
    }

    public void setSeller(String seller) {
        this.seller = seller;
    }

    public List<City> getAvailableCities() {
        return availableCities;
    }

    public void setAvailableCities(List<City> availableCities) {
        this.availableCities = availableCities;
    }

    @Override
    public String toString() {
        return "{" +
                "productId=" + productId +
                ", productName='" + productName + '\'' +
                ", productType='" + productType + '\'' +
                ", price=" + price +
                ", seller='" + seller + '\'' +
                ", availableCities=" + availableCities +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Product product = (Product) o;
        return productId == product.productId &&
                price == product.price &&
                Objects.equals(productName, product.productName) &&
                Objects.equals(productType, product.productType) &&
                Objects.equals(seller, product.seller) &&
                Objects.equals(availableCities, product.availableCities);
    }

    @Override
    public int hashCode() {

        return Objects.hash(productId, productName, productType, price, seller, availableCities);
    }
}
