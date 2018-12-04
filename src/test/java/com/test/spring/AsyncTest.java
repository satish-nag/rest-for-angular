package com.test.spring;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
@EnableAsync
public class AsyncTest {

    @Autowired
    TaskInterface task,task2;

    @Test
    public void test1() throws InterruptedException {
        System.out.println(Thread.currentThread().getName());
        CompletableFuture<String> stringCompletableFuture = timeoutFuture(task.execute(),1);
        CompletableFuture<String> stringCompletableFuture1 = timeoutFuture(task2.execute(),5);

        CompletableFuture<Void> completableFuture = CompletableFuture.allOf(stringCompletableFuture, stringCompletableFuture1);

        try {
            completableFuture.get();
            System.out.println("=========================");
            System.out.println(stringCompletableFuture.get());
            System.out.println(stringCompletableFuture1.get());
        } catch (InterruptedException | ExecutionException e) {
            completableFuture.completeExceptionally(e);
        }

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
