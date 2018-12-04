package com.test.spring;

import com.google.common.collect.Sets;
import com.test.spring.entities.Athletes;
import com.test.spring.entities.City;
import com.test.spring.entities.Product;
import com.test.spring.repositories.AthletesRepository;
import com.test.spring.repositories.CityRepository;
import com.test.spring.repositories.ProductRepository;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.ssl.DefaultHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.conn.util.PublicSuffixMatcherLoader;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.ssl.SSLContexts;
import org.apache.http.util.EntityUtils;
import org.springframework.boot.*;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.cassandra.CassandraAutoConfiguration;
import org.springframework.boot.autoconfigure.data.cassandra.CassandraDataAutoConfiguration;
import org.springframework.boot.autoconfigure.data.cassandra.CassandraRepositoriesAutoConfiguration;
import org.springframework.boot.autoconfigure.data.mongo.MongoDataAutoConfiguration;
import org.springframework.boot.autoconfigure.data.mongo.MongoRepositoriesAutoConfiguration;
import org.springframework.boot.autoconfigure.jms.activemq.ActiveMQAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.MockBeans;
import org.springframework.cloud.sleuth.sampler.AlwaysSampler;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.task.TaskExecutor;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.http.converter.xml.MappingJackson2XmlHttpMessageConverter;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.listener.DefaultMessageListenerContainer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.client.RestTemplate;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import javax.jms.ConnectionFactory;
import javax.net.ssl.SSLContext;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.*;
import java.security.cert.CertificateException;
import java.util.Arrays;

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

    @Bean
    public RestTemplate restTemplate(ApplicationContext applicationContext) throws NoSuchAlgorithmException, IOException, KeyStoreException, CertificateException, UnrecoverableKeyException, KeyManagementException {
        /*System.setProperty("javax.net.ssl.trustStore",applicationContext.getResource("classpath:config/keystore.jks").getURL().getPath());
        System.setProperty("javax.net.ssl.trustStorePassword", "Mber@1234");
        System.setProperty("javax.net.ssl.keyStore",applicationContext.getResource("classpath:config/keystore.jks").getURL().getPath());
        System.setProperty("javax.net.ssl.keyStorePassword", "Mber@1234");*/
        SSLContext sslcontext = SSLContexts.custom()
                .loadTrustMaterial(applicationContext.getResource("classpath:config/keystore.jks").getURL(),"Mber@1234".toCharArray())
                .build();
        CloseableHttpClient httpClient = HttpClients.custom().useSystemProperties().setSSLContext(sslcontext).build();
        return new RestTemplate(new HttpComponentsClientHttpRequestFactory(httpClient));
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
                .paths(regex("/product.*"))
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

}