package com.test.spring;

import org.cassandraunit.spring.CassandraDataSet;
import org.cassandraunit.spring.CassandraUnitDependencyInjectionTestExecutionListener;
import org.cassandraunit.spring.EmbeddedCassandra;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.cassandra.CassandraAutoConfiguration;
import org.springframework.boot.autoconfigure.cassandra.CassandraProperties;
import org.springframework.boot.autoconfigure.data.cassandra.CassandraDataAutoConfiguration;
import org.springframework.boot.test.mock.mockito.MockReset;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.boot.test.mock.mockito.SpyBeans;
import org.springframework.cassandra.core.CqlTemplate;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {CassandraAutoConfiguration.class, CassandraDataAutoConfiguration.class})
@EmbeddedCassandra(timeout = 25000)
@CassandraDataSet(keyspace = "cassandra_practice",value = {"dataset.cql"})
@TestExecutionListeners(listeners = {
        CassandraUnitDependencyInjectionTestExecutionListener.class,
        DependencyInjectionTestExecutionListener.class}
)
@EnableAsync
@TestPropertySource(properties = {"spring.data.cassandra.contact-points=127.0.0.1","spring.data.cassandra.port=9142","spring.data.cassandra.keyspace-name=cassandra_practice","spring.data.cassandra.username=cassandra","spring.data.cassandra.password=cassandra"})
@SpyBean(AsyncTest.TaskInterface.class)
public class AsyncTest {

    @Autowired
    TaskInterface task,task2,task4;

    @Autowired
    CqlTemplate cqlTemplate;

    @Autowired
    TaskInterface taskInterface;


    @Test
    public void test1() throws InterruptedException {
        System.out.println(Thread.currentThread().getName());
        CompletableFuture<String> stringCompletableFuture = timeoutFuture(task.execute(),1);
        CompletableFuture<String> stringCompletableFuture1 = timeoutFuture(task2.execute(),5);

        CompletableFuture<Void> completableFuture = CompletableFuture.allOf(stringCompletableFuture, stringCompletableFuture1);

        boolean wasApplied = cqlTemplate.query("insert into person(id,address) values(4,'kukatpally') if not exists").wasApplied();
        System.out.println(wasApplied);
        cqlTemplate.query("select id,address from person").all().forEach(row -> {
            System.out.println(row.getInt(0));
            System.out.println(row.getString(1));
        });
        System.out.println("Cluster Name: "+cqlTemplate.getSession().getCluster().getClusterName());

        try {
            completableFuture.get();
            System.out.println("=========================");
            System.out.println(stringCompletableFuture.get());
            System.out.println(stringCompletableFuture1.get());
        } catch (InterruptedException | ExecutionException e) {
            completableFuture.completeExceptionally(e);
        }

        Mockito.verify(task,Mockito.times(2)).execute();

        //Thread.sleep(11000);

    }

    private CompletableFuture<String> timeoutFuture(CompletableFuture<String> execute, int i) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                return execute.get(i,TimeUnit.SECONDS);
            } catch (InterruptedException |TimeoutException | ExecutionException e) {
                execute.cancel(true);
                return "failed "+e.getClass().getName();
            }
        });
    }

    @Configuration
    @EnableAsync
    static class TestConfig {

        @Bean
        public TaskExecutor taskExecutor(){
            ThreadPoolTaskExecutor threadPoolTaskExecutor = new ThreadPoolTaskExecutor();
            threadPoolTaskExecutor.setCorePoolSize(10);
            threadPoolTaskExecutor.setMaxPoolSize(50);
            threadPoolTaskExecutor.setQueueCapacity(50);
            threadPoolTaskExecutor.setAllowCoreThreadTimeOut(true);
            threadPoolTaskExecutor.setKeepAliveSeconds(120);
            threadPoolTaskExecutor.setThreadNamePrefix("custom-");
            threadPoolTaskExecutor.initialize();

            return threadPoolTaskExecutor;
        }

    }

    @Component
    static class Task implements TaskInterface{

        @Override
        public CompletableFuture<String> callMethod(){
            System.out.println("current thread name "+Thread.currentThread().getName());
            try {
                TimeUnit.SECONDS.sleep(10);
            } catch (InterruptedException e) {
                System.out.println("-------------------------Exception thrown "+e.getMessage());
            }
            System.out.println("after sleeping +++++++++++++++++");
            return CompletableFuture.completedFuture("Thread Execution completed");
        }
    }


    interface TaskInterface{
        @Async
        default CompletableFuture<String> execute(){
            return callMethod();
        }

        CompletableFuture<String> callMethod();
    }
}
