package com.test.spring;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.TreeNode;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.test.spring.entities.City;
import com.test.spring.entities.Product;
import com.test.spring.exceptions.ClientException;
import com.test.spring.model.FraudPointPayload;
import com.test.spring.model.Header;
import com.test.spring.model.MainPayload;
import com.test.spring.model.Response;
import com.test.spring.repositories.CityRepository;
import com.test.spring.repositories.ProductRepository;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.ssl.SSLContexts;
import org.apache.http.ssl.TrustStrategy;
import org.springframework.boot.*;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.mongo.MongoDataAutoConfiguration;
import org.springframework.boot.autoconfigure.data.mongo.MongoRepositoriesAutoConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.cloud.sleuth.sampler.AlwaysSampler;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.core.task.TaskExecutor;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.*;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.client.RestTemplate;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import javax.net.ssl.SSLContext;
import java.io.IOException;
import java.security.*;
import java.security.cert.CertificateException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static springfox.documentation.builders.PathSelectors.regex;
import static springfox.documentation.builders.RequestHandlerSelectors.basePackage;

/**
 * Hello world!
 *
 */
@SpringBootApplication(exclude = {MongoAutoConfiguration.class, MongoDataAutoConfiguration.class, MongoRepositoriesAutoConfiguration.class}) //,CassandraAutoConfiguration.class,CassandraDataAutoConfiguration.class, CassandraRepositoriesAutoConfiguration.class
//@EnableZipkinServer
@EnableAspectJAutoProxy
@EnableSwagger2
@EnableAsync
/*@ComponentScan("com.test.spring")
@EntityScan("com.test.spring")
@EnableJpaRepositories("com.test.spring")*/
public class App
{

    public static void main( String[] args )
    {
        SpringApplication application = new SpringApplication();
        //application.setBannerMode(Banner.Mode.OFF);
        //application.setMainApplicationClass(App.class);
        ConfigurableApplicationContext configurableApplicationContext = application.run(App.class,args);
        /*SpringApplication.run(App.class,args);*/
    }

    ApplicationRunner applicationRunner(){
        return (ApplicationArguments args) -> {
            args.getOptionNames();
        };
    }
    @Bean
    CommandLineRunner commandLineRunner(ProductRepository productRepository,
                                        CityRepository cityRepository,
                                        ObjectMapper objectMapper,
                                        /*JmsTemplate jmsTemplate,*/
                                        DefaultJmsListenerContainerFactory messageListenerContainer/*,
                                        AthletesRepository athletesRepository*/){
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
            /*jmsTemplate.convertAndSend("test","satish");*/
            /*Athletes oneAthlete = athletesRepository.findOne(757107967);
            System.out.println(oneAthlete);*/
            FraudPointPayload fraudPointPayload = new FraudPointPayload();
            fraudPointPayload.getProps().put("attr1","1");
            fraudPointPayload.getProps().put("attr2","2");
            fraudPointPayload.getProps().put("attr3","3");
            fraudPointPayload.getProps().put("attr4","4");

            System.out.println("=================");
            String s = objectMapper.writer().withRootName(fraudPointPayload.getPayloadName()).writeValueAsString(fraudPointPayload);
            System.out.println(s);
            JsonNode jsonNode = objectMapper.readTree(s);
            jsonNode.fields().forEachRemaining(stringJsonNodeEntry -> {
                try {
                    FraudPointPayload fraudPointPayload1 = objectMapper.reader().treeToValue(stringJsonNodeEntry.getValue(), FraudPointPayload.class);
                    System.out.println(fraudPointPayload1);
                } catch (JsonProcessingException e) {
                    e.printStackTrace();
                }
            });
            System.out.println("=================");
            Header header = new Header();
            header.setStatusCode("3001");
            header.setStatusMessage("processed");

            Response response = new Response();
            response.setHeader(header);
            response.setPayload(fraudPointPayload);


            Response response1 = new Response();

            Header header1 = new Header();
            header1.setStatusCode("2001");
            header1.setStatusMessage("processed");

            MainPayload mainPayload = new MainPayload();
            mainPayload.setFraudPointResponse(response);

            response1.setHeader(header1);
            response1.setPayload(mainPayload);
            String valueAsString = objectMapper.writeValueAsString(response1);
            System.out.println(valueAsString);
            Response response2 = objectMapper.readValue(valueAsString, Response.class);
            System.out.println(response2);
            String valueAsString1 = objectMapper.writeValueAsString(response2);
            System.out.println(valueAsString1);
        };
    }

    /*@Bean
    public MappingJackson2XmlHttpMessageConverter mappingJackson2XmlHttpMessageConverter(){
        return new MappingJackson2XmlHttpMessageConverter();
    }

    @Bean
    public MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter(){
        return new MappingJackson2HttpMessageConverter();
    }*/

    @Bean
    public RestTemplate restTemplate(ApplicationContext applicationContext) throws IOException, CertificateException, NoSuchAlgorithmException, KeyStoreException, KeyManagementException {
        /*System.setProperty("javax.net.ssl.trustStore",applicationContext.getResource("classpath:config/keystore.jks").getURL().getPath());
        System.setProperty("javax.net.ssl.trustStorePassword", "Mber@1234");
        System.setProperty("javax.net.ssl.keyStore",applicationContext.getResource("classpath:config/keystore.jks").getURL().getPath());
        System.setProperty("javax.net.ssl.keyStorePassword", "Mber@1234");*/
        SSLContext sslcontext = SSLContexts.custom()
                .loadTrustMaterial(applicationContext.getResource("classpath:config/keystore.jks").getURL(),"Mber@1234".toCharArray())
                .build();
        RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(1000).build();
        CloseableHttpClient httpClient = HttpClients.custom().setDefaultRequestConfig(requestConfig).useSystemProperties().setSSLContext(sslcontext).build();
        RestTemplate restTemplate = new RestTemplate(new HttpComponentsClientHttpRequestFactory(httpClient));
        List<ClientHttpRequestInterceptor> clientHttpRequestInterceptors = new ArrayList<>();
        clientHttpRequestInterceptors.add(new ClientHttpRequestInterceptor() {
            @Override
            public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
                ClientHttpResponse clientHttpResponse = execution.execute(request,body);
                /*try {
                    clientHttpResponse = execution.execute(request, body);
                }catch (Exception e){
                    e.printStackTrace();
                    throw new ClientException(e);
                }*/
                return clientHttpResponse;
            }
        });
        restTemplate.setInterceptors(clientHttpRequestInterceptors);
        return restTemplate;
    }

    @Bean
    public AlwaysSampler alwaysSampler() {
        return new AlwaysSampler();
    }

    @Bean
    public Docket docket(){
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(basePackage("com.test.spring.controllers"))
                //.paths(regex("/product.*"))
                .build();
    }

    @Bean
    public TaskExecutor taskExecutor(){
        ThreadPoolTaskExecutor threadPoolTaskExecutor = new ThreadPoolTaskExecutor();
        threadPoolTaskExecutor.setCorePoolSize(10);
        threadPoolTaskExecutor.setMaxPoolSize(20);
        threadPoolTaskExecutor.setThreadNamePrefix("Service Thread #");
        threadPoolTaskExecutor.setQueueCapacity(20);
        threadPoolTaskExecutor.setWaitForTasksToCompleteOnShutdown(true);
        threadPoolTaskExecutor.initialize();

        return threadPoolTaskExecutor;
    }

    /*@Bean
    public RestTemplate restTemplate() throws Exception {
        RestTemplate restTemplate = new RestTemplate();
        HttpComponentsClientHttpRequestFactory httpComponentsClientHttpRequestFactory = new HttpComponentsClientHttpRequestFactory();
        httpComponentsClientHttpRequestFactory.setHttpClient(createHttpClient());
        restTemplate.setRequestFactory(httpComponentsClientHttpRequestFactory);
        return restTemplate;
    }*/

    /*private HttpClient createHttpClient() throws Exception {
        KeyStore keyStore = KeyStore.getInstance("jks");
        keyStore.load(App.class.getResourceAsStream("keystore.jks"),"Mber@1234".toCharArray());
        SSLContext sslContext = SSLContexts.custom().loadKeyMaterial(keyStore, "Mber@1234".toCharArray()).build();
        return HttpClients.custom().useSystemProperties().setSSLContext(sslContext).build();
    }*/


}