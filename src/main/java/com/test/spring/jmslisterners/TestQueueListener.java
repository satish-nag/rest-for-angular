package com.test.spring.jmslisterners;

import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.TextMessage;

@Component
public class TestQueueListener {

    @JmsListener(destination = "test")
    public void receiveMessage(TextMessage message) throws JMSException {
        System.out.println("message received "+message.getText());
    }
}
