package com.modusbox.kamelets.datasonnet.model;

import org.apache.camel.Exchange;

import java.util.Map;
import java.util.Objects;

public class ExchangeWrapper {
    private String id;
    private Exception exception;
    private Map<String, Object> properties;
    private MessageWrapper message;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Exception getException() {
        return exception;
    }

    public void setException(Exception exception) {
        this.exception = exception;
    }

    public MessageWrapper getMessage() {
        return message;
    }

    public void setMessage(MessageWrapper message) {
        this.message = message;
    }

    public void setProperties(Map<String, Object> properties) {
        this.properties = properties;
    }

    public Map<String, Object> getProperties() {
        return properties;
    }

    public static ExchangeWrapper fromExchange(final Exchange exchange) {
        var wrapper = new ExchangeWrapper();
        wrapper.setException(exchange.getException());
        wrapper.setId(exchange.getExchangeId());
        wrapper.setMessage(MessageWrapper.fromMessage(exchange.getMessage()));
        wrapper.setProperties(exchange.getProperties());
        return wrapper;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ExchangeWrapper that = (ExchangeWrapper) o;
        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
