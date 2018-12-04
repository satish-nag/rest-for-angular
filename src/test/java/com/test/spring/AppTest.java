package com.test.spring;

import com.test.spring.entities.City;
import com.test.spring.entities.Product;
import com.test.spring.repositories.CityRepository;
import com.test.spring.repositories.ProductRepository;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureWebMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Unit test for simple App.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
//@WebMvcTest
public class AppTest
{
    @Autowired
    MockMvc mockMvc;

    /*@Autowired
    private TestRestTemplate testRestTemplate;*/

    @Autowired
    CityRepository cityRepository;

    @Autowired
    ProductRepository productRepository;

    City hyderabad = new City("Hyderabad");
    City bengaluru = new City("Bengaluru");
    City chennai = new City("Chennai");
    City delhi = new City("Delhi");
    City pune = new City("Pune");
    City mumbai=new City("Mumbai");

    Product product1 = new Product("Gone girl",
            "book",339,"books online",
            Arrays.asList(hyderabad,bengaluru,chennai));
    Product product2 = new Product("The FountainHead",
            "book",599,"books online",
            Arrays.asList(delhi,hyderabad,chennai));
    Product product3 = new Product("Flash Drum",
            "Toys",299,"Toys online",
            Arrays.asList(delhi,pune,mumbai));


    /*@Before
    public void populateData(){
        cityRepository.save(Arrays.asList(hyderabad,bengaluru,chennai,delhi,pune,mumbai));
        productRepository.save(Arrays.asList(product1,product2,product3));
    }*/

    @Test
    public void test(){
        /*System.out.println(this.testRestTemplate.getForEntity("/allProducts",Object.class));*/
        /*ResponseEntity<Product[]> forEntity = this.testRestTemplate.getForEntity("/allProducts", Product[].class);
        assertThat(forEntity.getBody()[0].productName).isEqualTo("Gone girl");*/
    }

    @Test
    @Ignore
    public void testJms() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/postMsg/test").content("this is a test message"))
                .andDo(print())
                .andExpect(status().isOk());

    }

}
