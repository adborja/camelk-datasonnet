package com.modusbox.kamelets.datasonnet.model;

import org.apache.camel.Message;

import java.util.Map;
import java.util.Objects;

public class MessageWrapper {
    private String id;
    private Long timestamp;
    private Map<String, Object> headers;
    private Object body;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    public Map<String, Object> getHeaders() {
        return headers;
    }

    public void setHeaders(Map<String, Object> headers) {
        this.headers = headers;
    }

    public Object getBody() {
        return body;
    }

    public void setBody(Object body) {
        this.body = body;
    }

    public static MessageWrapper fromMessage(final Message message) {
        var wrapper = new MessageWrapper();
        wrapper.setId(message.getMessageId());
        wrapper.setTimestamp(message.getMessageTimestamp());
        wrapper.setHeaders(message.getHeaders());
        wrapper.setBody(message.getBody());
        return wrapper;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MessageWrapper that = (MessageWrapper) o;
        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
