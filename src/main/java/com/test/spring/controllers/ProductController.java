package com.test.spring.controllers;

import com.test.spring.entities.Product;
import com.test.spring.repositories.CityRepository;
import com.test.spring.repositories.ProductRepository;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.hibernate.mapping.Collection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.util.MimeType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.persistence.EntityManagerFactory;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Root;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@CrossOrigin
@RestController
public class ProductController {

    @Autowired
    ProductRepository productRepository;
    @Autowired
    CityRepository cityRepository;
    @Autowired EntityManagerFactory entityManagerFactory;

    @GetMapping(path = "allProducts",produces = {MediaType.APPLICATION_XML_VALUE,MediaType.APPLICATION_JSON_VALUE})
    public List<Product> getAllProducts(){
        List<Product> products = new ArrayList<>();
        productRepository.findAll().forEach(products::add);
        return products;
    }

    @PostMapping("modifyProduct")
    public Product modifyProduct(@RequestBody Product product){
        cityRepository.save(product.availableCities);
        return productRepository.save(product);
    }

    @GetMapping(path="oneProduct")
    public Product getOneProduct(@RequestParam int id){
        return productRepository.findOne(id);
    }

    @GetMapping(path = "hostname")
    public String getHostname() throws UnknownHostException {
        return InetAddress.getLocalHost().getHostName();
    }

    @GetMapping(path = "test")
    public void test(){
        CriteriaBuilder criteriaBuilder = entityManagerFactory.getCriteriaBuilder();
        CriteriaQuery<Object[]> query = criteriaBuilder.createQuery(Object[].class);
        Root<Product> productRoot = query.from(Product.class);
        Path<Integer> productId = productRoot.get("productId");
        Path<String> productName = productRoot.get("productName");
        query.multiselect(productId,productName);
        query.where(criteriaBuilder.gt(productId,1));
        entityManagerFactory.createEntityManager().createQuery(query).getResultList().forEach(o->{
            System.out.println(Arrays.stream(o).reduce((a, b)-> a+","+b).get());
        });
    }
}
