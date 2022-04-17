package com.modusbox.kamelets;

import org.apache.camel.Exchange;
import org.apache.camel.impl.DefaultCamelContext;
import org.apache.camel.support.DefaultExchange;
import org.apache.camel.support.DefaultMessage;
import org.hamcrest.CoreMatchers;
import org.hamcrest.MatcherAssert;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.util.Collections;
import java.util.UUID;

@ExtendWith(MockitoExtension.class)
public class ExtractPropertiesProcessorTest {
    @InjectMocks
    ExtractPropertiesProcessor processor;

    @Test
    void testProcess() throws IOException {
        var exchange = buildDefaultExchange(null);
        var property = "{'k1': 'v1', 'k2': 'v2'}";
        processor.setProperty(property);
        processor.process(exchange);

        MatcherAssert.assertThat(exchange.getProperties().size(), CoreMatchers.is(3));
    }

    private static Exchange buildDefaultExchange(Object body) {
        var timestamp = System.currentTimeMillis();
        var context = new DefaultCamelContext();
        var exchange = new DefaultExchange(context);
        var message = new DefaultMessage(context);
        message.setMessageId(UUID.randomUUID().toString());
        message.setBody(body);
        message.setHeader("header1", "hdr-value-1");
        message.setHeader(Exchange.MESSAGE_TIMESTAMP, timestamp);

        exchange.setException(null);
        exchange.setExchangeId(UUID.randomUUID().toString());
        exchange.setMessage(message);
        exchange.setProperties(Collections.singletonMap("prop1", "prop-value-1"));

        return exchange;
    }
}
