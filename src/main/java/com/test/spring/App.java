package com.test.spring;

import com.test.spring.entities.City;
import com.test.spring.entities.Product;
import com.test.spring.repositories.CityRepository;
import com.test.spring.repositories.ProductRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.http.converter.xml.MappingJackson2XmlHttpMessageConverter;

import java.util.Arrays;

/**
 * Hello world!
 *
 */
@SpringBootApplication
public class App 
{
    public static void main( String[] args )
    {
        SpringApplication.run(App.class,args);
    }

    @Bean
    CommandLineRunner commandLineRunner(ProductRepository productRepository, CityRepository cityRepository){
        return (args) -> {
            City hyderabad = new City("Hyderabad");
            City bengaluru = new City("Bengaluru");
            City chennai = new City("Chennai");
            City delhi = new City("Delhi");
            City pune = new City("Pune");
            City mumbai=new City("Mumbai");
            cityRepository.save(Arrays.asList(hyderabad,bengaluru,chennai,delhi,pune,mumbai));

            Product product1 = new Product("Gone girl",
                    "book",339,"books online",
                    Arrays.asList(hyderabad,bengaluru,chennai));
            Product product2 = new Product("The FountainHead",
                    "book",599,"books online",
                    Arrays.asList(delhi,hyderabad,chennai));
            Product product3 = new Product("Flash Drum",
                    "Toys",299,"Toys online",
                    Arrays.asList(delhi,pune,mumbai));

            productRepository.save(Arrays.asList(product1,product2,product3));
        };
    }

    @Bean
    public MappingJackson2XmlHttpMessageConverter mappingJackson2XmlHttpMessageConverter(){
        return new MappingJackson2XmlHttpMessageConverter();
    }

    @Bean
    public MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter(){
        return new MappingJackson2HttpMessageConverter();
    }
}