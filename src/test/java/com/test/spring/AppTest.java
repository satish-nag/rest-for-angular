package com.test.spring;

import com.test.spring.entities.City;
import com.test.spring.entities.Product;
import com.test.spring.repositories.CityRepository;
import com.test.spring.repositories.ProductRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Matchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.cassandra.core.CqlTemplate;
import org.springframework.cassandra.core.ResultSetExtractor;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Arrays;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
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
    WebApplicationContext webApplicationContext;

    @Autowired
    MockMvc mockMvc;

    @SpyBean
    CqlTemplate cqlTemplate;

    // use this only when you are using SpringBootTest.WebEnvironment.RANDOM_PORT,SpringBootTest.WebEnvironment.DEFINED_PORT
    /*@Autowired
    private TestRestTemplate testRestTemplate;*/

    @Autowired
    CityRepository cityRepository;

    @Autowired
    ProductRepository productRepository;

    @Autowired
    JmsTemplate jmsTemplate;

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


    @Before
    public void populateData(){
        cityRepository.save(Arrays.asList(hyderabad,bengaluru,chennai,delhi,pune,mumbai));
        productRepository.save(Arrays.asList(product1,product2,product3));
    }


    @Test
    public void test() throws Exception {
        /*ResponseEntity<Product[]> forEntity = this.testRestTemplate.getForEntity("/product/allProducts", Product[].class);
        assertThat(forEntity.getBody()[0].productName).isEqualTo("Gone girl");
        System.out.println("cql template "+cqlTemplate);*/
        mockMvc.perform(get("/product/allProducts"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].productName",is("Gone girl")));
    }

    @Test
    //@Ignorev
    public void testJms() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/product/postMsg/test").content("this is a test message"))
                .andDo(print())
                .andExpect(status().isOk());

    }

    @Test
    public void dumTest() throws Exception {
        mockMvc.perform(get("/product/dum"))
                .andDo(print())
                .andExpect(status().isOk());

        Mockito.verify(cqlTemplate,Mockito.times(2)).query(Matchers.any(String.class),Matchers.any(),Matchers.any(ResultSetExtractor.class));
    }

}
