package com.test.spring.controllers;

import com.test.spring.entities.Product;
import com.test.spring.model.FraudPointPayload;
import com.test.spring.model.Header;
import com.test.spring.model.Response;
import com.test.spring.repositories.CityRepository;
import com.test.spring.repositories.ProductRepository;
import io.swagger.annotations.ApiOperation;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cassandra.core.CqlTemplate;
import org.springframework.http.*;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.persistence.EntityManagerFactory;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Root;
import java.net.InetAddress;
import java.net.URI;
import java.net.UnknownHostException;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.Min;

import static org.apache.log4j.Logger.getLogger;

@CrossOrigin
@RestController
@Validated
@RequestMapping("/product")
public class ProductController {

    private final Logger LOG = getLogger(getClass().getName());
    @Autowired
    ProductRepository productRepository;
    @Autowired
    CityRepository cityRepository;
    @Autowired EntityManagerFactory entityManagerFactory;
    @Autowired RestTemplate restTemplate;
    @Autowired JmsTemplate jmsTemplate;
    @Autowired CqlTemplate cqlTemplate;
    @Autowired DummyTask dummyTask1,dummyTask2;

    @PostMapping(path = "postMsg/{destination}")
    public void sendMsgToJms(@PathVariable String destination,@RequestBody String message){
        jmsTemplate.convertAndSend(destination,message);
    }

    @GetMapping(path = "allProducts",produces = {MediaType.APPLICATION_JSON_VALUE})
    public List<Product> getAllProducts(HttpServletRequest req){
        List<Product> products = new ArrayList<>();
        productRepository.findAll().forEach(products::add);
        LOG.info("products "+products);
        //commented for testing while using SpringBootTest.WebEnvironment.MOCK as port is not there this resttemplate will not work
		/*UriComponents url = ServletUriComponentsBuilder.fromServletMapping(req).path("product/oneProduct").queryParam("id","3").build();
        System.out.println(url.toString());
        restTemplate.getForObject(url.toString(),Product.class);*/
        return products;
    }

    @PostMapping("modifyProduct")
    public Product modifyProduct(@RequestBody Product product){
        cityRepository.save(product.availableCities);
        return productRepository.save(product);
    }

    @GetMapping(path="oneProduct")
    public Product getOneProduct(@Min(value = 3,message = "value must be greater than 3") int id){
        LOG.info("fetching one product with ID "+id);
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
            System.out.println();
        });
    }

    @GetMapping("/dum")
    public String dum() throws ExecutionException, InterruptedException {
        CompletableFuture<String> completableFuture1 = dummyTask1.Test("4", "kukatpally");
        CompletableFuture<String> completableFuture2 = dummyTask2.Test("4", "kphb");
        CompletableFuture<Void> voidCompletableFuture = CompletableFuture.allOf(completableFuture1, completableFuture2);
        voidCompletableFuture.get();
        return "success";
    }

    @ApiOperation(value = "this operation returns Response", response = Response.class)
    @GetMapping(value = "/test2",produces = MediaType.APPLICATION_JSON_VALUE)
    public Response getResponse(HttpServletRequest httpServletRequest){
        FraudPointPayload fraudPointPayload = new FraudPointPayload();
        fraudPointPayload.getProps().put("attr1","1");
        fraudPointPayload.getProps().put("attr2","2");
        fraudPointPayload.getProps().put("attr3","3");
        fraudPointPayload.getProps().put("attr4","4");

        Header header = new Header();
        header.setStatusCode("3001");
        header.setStatusMessage("processed");

        Response response = new Response();
        response.setHeader(header);
        response.setPayload(fraudPointPayload);
        URI uri = ServletUriComponentsBuilder.fromContextPath(httpServletRequest).path("/product/test3").build().toUri();
        try {
            RequestEntity<Void> requestEntity = RequestEntity.get(uri).build();
            ResponseEntity<String> forEntity = restTemplate.exchange(requestEntity, String.class);
            HttpStatus statusCode = forEntity.getStatusCode();
        }catch (HttpClientErrorException | HttpServerErrorException e){ // 4xx and 5xx error codes are handled here
            HttpStatus statusCode = e.getStatusCode();
            System.out.println(statusCode==HttpStatus.INTERNAL_SERVER_ERROR);
        }
        catch (UnknownHttpStatusCodeException e){ // other error codes handled here
            e.printStackTrace();
        }
        catch (ResourceAccessException e){ // IOException are handled here like SocketTimeout
            e.printStackTrace();
        }
        catch (Exception e){ // application exceptions are handled here
            e.printStackTrace();
        }
        return response;
    }

    @GetMapping(value = "/test3",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> test3() throws InterruptedException {
        Thread.sleep(2000);
        return ResponseEntity.status(HttpStatus.PERMANENT_REDIRECT).build();
    }


}