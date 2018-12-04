package com.test.spring.controllers;

import com.datastax.driver.core.BoundStatement;
import com.datastax.driver.core.PreparedStatement;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;
import com.datastax.driver.core.exceptions.DriverException;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cassandra.core.CqlTemplate;
import org.springframework.cassandra.core.PreparedStatementBinder;
import org.springframework.cassandra.core.ResultSetExtractor;
import org.springframework.context.annotation.Scope;
import org.springframework.dao.DataAccessException;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;

import static org.slf4j.LoggerFactory.getLogger;

@Component
@Scope("prototype")
public class DummyTask {
    private static final Logger log = getLogger(DummyTask.class);

    @Autowired
    CqlTemplate cqlTemplate;

    @Async("taskExecutor")
    public CompletableFuture<String> Test(String id, String address) throws InterruptedException {
        Row one = cqlTemplate.query("select id,address,name from person where id=" + id).one();
        Boolean b = null;
        if (one == null) {
            log.info("inside if block");
            b = cqlTemplate.query("insert into person(id,address) values(?,?) if not exists", ps -> ps.bind(Integer.parseInt(id), address),rs->{return rs.wasApplied();});
        } else {
            cqlTemplate.query("update person set address=? where id = ?", ps -> ps.bind("atp", 4), row -> {});
            Thread.sleep(2000);
        }
        log.info("=========================================");
        log.info(b + "---" + address);
        return CompletableFuture.completedFuture("done");
    }
}